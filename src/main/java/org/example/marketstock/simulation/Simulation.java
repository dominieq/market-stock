package org.example.marketstock.simulation;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.vavr.Tuple3;
import javafx.collections.ObservableList;
import org.example.marketstock.models.asset.AbstractAsset;
import org.example.marketstock.models.asset.Commodity;
import org.example.marketstock.models.asset.Currency;
import org.example.marketstock.models.asset.builder.CommodityBuilder;
import org.example.marketstock.models.asset.builder.CurrencyBuilder;
import org.example.marketstock.models.briefcase.builder.BriefcaseBuilder;
import org.example.marketstock.models.company.Company;
import org.example.marketstock.models.company.builder.CompanyBuilder;
import org.example.marketstock.models.entity.Entity;
import org.example.marketstock.models.entity.InvestmentFund;
import org.example.marketstock.models.entity.Investor;
import org.example.marketstock.models.entity.Player;
import org.example.marketstock.models.entity.builder.InvestmentFundBuilder;
import org.example.marketstock.models.entity.builder.InvestorBuilder;
import org.example.marketstock.models.exchange.CommodityExchange;
import org.example.marketstock.models.exchange.CurrencyExchange;
import org.example.marketstock.models.exchange.StockExchange;
import org.example.marketstock.models.exchange.builder.CommodityExchangeBuilder;
import org.example.marketstock.models.exchange.builder.CurrencyExchangeBuilder;
import org.example.marketstock.models.exchange.builder.StockExchangeBuilder;
import org.example.marketstock.models.index.Index;
import org.example.marketstock.models.index.IndexType;
import org.example.marketstock.models.index.NumericMaxIndex;
import org.example.marketstock.models.index.NumericMinIndex;
import org.example.marketstock.models.index.builder.NumericMaxIndexBuilder;
import org.example.marketstock.models.index.builder.NumericMinIndexBuilder;
import org.example.marketstock.models.asset.Asset;
import org.example.marketstock.models.asset.Countable;
import org.example.marketstock.simulation.croupier.Croupier;
import org.example.marketstock.simulation.croupier.Croupiers;
import org.example.marketstock.simulation.serialization.SimulationDeserializer;
import org.example.marketstock.simulation.serialization.SimulationSerializer;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@JsonSerialize(using = SimulationSerializer.class)
@JsonDeserialize(using = SimulationDeserializer.class)
public class Simulation {

    private final Player player;
    private final ObservableList<StockExchange> stockExchanges;
    private final ObservableList<CurrencyExchange> currencyExchanges;
    private final ObservableList<CommodityExchange> commodityExchanges;
    private final ObservableList<Investor> investors;
    private final ObservableList<InvestmentFund> investmentFunds;
    private final List<String> commodityNames;
    private final List<String> currencyNames;
    private final Croupier croupier;
    private Currency mainCurrency;
    private final ExecutorService entitiesService = Executors.newFixedThreadPool(100);

    public Simulation(final Player player,
                      final ObservableList<StockExchange> stockExchanges,
                      final ObservableList<CurrencyExchange> currencyExchanges,
                      final ObservableList<CommodityExchange> commodityExchanges,
                      final ObservableList<Investor> investors,
                      final ObservableList<InvestmentFund> investmentFunds,
                      final List<String> commodityNames,
                      final List<String> currencyNames,
                      final Croupier croupier,
                      final Currency mainCurrency) {

        this.player = player;
        this.stockExchanges = stockExchanges;
        this.currencyExchanges = currencyExchanges;
        this.commodityExchanges = commodityExchanges;
        this.investors = investors;
        this.investmentFunds = investmentFunds;
        this.commodityNames = commodityNames;
        this.currencyNames = currencyNames;
        this.croupier = croupier;
        this.mainCurrency = mainCurrency;
    }

    public synchronized int buySelectedResource(final Asset asset,
                                                 final int number,
                                                 final double originalRate,
                                                 final Entity entity) {

        final double currentRate = asset.getCurrentRate();
        int finalNumber = number;

        // If asset's rate has changed between choosing an asset and buying it,
        // count how many asset can be purchased with current rate.
        if (Double.compare(currentRate, originalRate) != 0) {
            final double originalPrice = number * originalRate;
            finalNumber = (int) Math.floor(originalPrice / currentRate);

            if (finalNumber == 0) return finalNumber;
            else if (finalNumber < 0) {
                // FIXME should throw exception because unacceptable
                return 0;
            }
        }

        // If available number has changed between choosing an asset and buying it,
        // assign new possible number to final number of asset.
        if (asset instanceof Countable && number > ((Countable) asset).getNumberOfAssets()) {
            finalNumber = ((Countable) asset).getNumberOfAssets();

            if (finalNumber == 0) return finalNumber;
            else if (finalNumber < 0) {
                // FIXME should throw exception because unacceptable
                return 0;
            }
        }

        final double price = currentRate * finalNumber;
        entity.addAsset(price, asset, finalNumber);

        final double rate = currentRate + currentRate * 0.05;
        asset.updateRate(rate);

        if (asset instanceof Countable) {
            ((Countable) asset).decreaseNumberOfAssets(finalNumber);
        }

        if (asset instanceof Company) {
            ((Company) asset).updateTurnoverAndVolume(price, finalNumber);
            stockExchanges.forEach(StockExchange::updateIndices);
        }

        return finalNumber;
    }

    public synchronized void sellSelectedResource(final Asset asset,
                                                  final int number,
                                                  final Entity entity) {

        final double currentRate = asset.getCurrentRate();
        final double price = currentRate * number;
        final double priceMinusMargin = price - price * asset.getMargin();
        entity.subtractAsset(priceMinusMargin, asset, number);

        final double rate = currentRate  - currentRate * 0.05;
        asset.updateRate(rate);

        if (asset instanceof Countable) {
            ((Countable) asset).increaseNumberOfAssets(number);
        }

        if (asset instanceof Company) {
            ((Company) asset).updateTurnoverAndVolume(price, number);
            stockExchanges.forEach(StockExchange::updateIndices);
        }
    }

    public synchronized Optional<Tuple3<Asset, Integer, Double>> chooseAssetToBuy(final Entity entity) {
        final List<Asset> assets = getAvailableAssets();

        // Entity shouldn't buy itself so let's remove any possible entities that are assets as well.
        if (entity instanceof Asset) assets.remove(entity);

        if (assets.isEmpty()) return Optional.empty();

        final Asset chosenAsset = assets.stream()
                .min((asset1, asset2) -> {
                    if (asset1.getCurrentRate() == asset2.getCurrentRate()) {
                        return 0;
                    }

                    return asset1.getCurrentRate() > asset2.getCurrentRate() ? 1 : -1;
                }).get();

        int possibleNumber = (int) Math.floor(entity.getBudget() / chosenAsset.getCurrentRate());

        // Entity shouldn't buy more than it's currently available
        if (chosenAsset instanceof Countable && possibleNumber > ((Countable) chosenAsset).getNumberOfAssets()) {
            possibleNumber = ((Countable) chosenAsset).getNumberOfAssets();
        }

        final int chosenNumber = possibleNumber > 0 ? croupier.getRandom().nextInt(possibleNumber) + 1 : 0;

        return chosenNumber > 0 ?
                Optional.of(new Tuple3<>(chosenAsset, chosenNumber, chosenAsset.getCurrentRate())) : Optional.empty();
    }

    public synchronized Optional<Tuple3<Asset, Integer, Double>> chooseAssetToSell(final Entity entity) {
        final List<Asset> assets = entity.getBriefcase().getAssets();

        if (assets.isEmpty()) return Optional.empty();

        final Asset chosenAsset = assets.stream()
                .max((asset1, asset2) -> {
                    if (asset1.getCurrentRate() == asset2.getCurrentRate()) {
                        return 0;
                    }

                    return asset1.getCurrentRate() > asset2.getCurrentRate() ? 1 : -1;
                }).get();

        final int chosenNumber = croupier.getRandom().nextInt(entity.getBriefcase().getCount(chosenAsset)) + 1;
        return Optional.of(new Tuple3<>(chosenAsset, chosenNumber, chosenAsset.getCurrentRate()));
    }

    public synchronized void issueEntities() {
        if (getAvailableAssets().size() % 5 == 0) {
            addInvestor();
            addInvestmentFund();
        }
    }

    public List<Asset> getAvailableAssets() {
        final List<Asset> availableAssets = new ArrayList<>();

        availableAssets.addAll(stockExchanges.stream()
                .map(StockExchange::getCompanies)
                .flatMap(Collection::stream)
                .collect(Collectors.toList())
        );

        availableAssets.addAll(currencyExchanges.stream()
                .map(CurrencyExchange::getCurrencies)
                .flatMap(Collection::stream)
                .collect(Collectors.toList())
        );

        availableAssets.addAll(commodityExchanges.stream()
                .map(CommodityExchange::getCommodities)
                .flatMap(Collection::stream)
                .collect(Collectors.toList())
        );

        availableAssets.addAll(investmentFunds);

        return availableAssets;
    }

    public Investor addInvestor() {
        final Investor investor = InvestorBuilder.builder()
                .withFirstName(croupier.drawFirstName())
                .withLastName(croupier.drawLastName())
                .withPESEL(croupier.drawPESEL())
                .withBudget(croupier.drawBudget())
                .withBriefcase(BriefcaseBuilder.builder()
                        .withMap(new HashMap<>())
                        .build())
                .withSimulation(this)
                .build();

        entitiesService.submit(investor);
        investors.add(investor);

        return investor;
    }

    public synchronized void removeInvestor(final Investor investor) {
        if (!investor.isActive()) {
            return;
        }

        investor.terminate();

        final ExecutorService service = Executors.newSingleThreadExecutor();
        service.submit(() -> {
            try {
                while (!investor.isTerminated()) {
                    TimeUnit.SECONDS.sleep(5);
                }
            } catch (InterruptedException ignored) {

            } finally {
                investors.remove(investor);
            }
        });
    }

    public InvestmentFund addInvestmentFund() {
        final double currentRate = croupier.drawCurrentRate();

        final InvestmentFund investmentFund = InvestmentFundBuilder.builder()
                .withName(croupier.drawInvestmentFundName())
                .withCurrentRate(currentRate)
                .withMinRate(currentRate)
                .withMaxRate(currentRate)
                .withRateChanges(new ArrayList<>(Collections.singletonList(currentRate)))
                .withMargin(croupier.drawMargin() * 100D)
                .withBudget(croupier.drawBudget())
                .withNumberOfAssets(croupier.drawNumberOfAssets())
                .withBriefcase(BriefcaseBuilder.builder()
                        .withMap(new HashMap<>())
                        .build())
                .withSimulation(this)
                .build();

        entitiesService.submit(investmentFund);
        investmentFunds.add(investmentFund);

        return investmentFund;
    }

    public void removeInvestmentFund(final InvestmentFund investmentFund) {
        if (!investmentFund.isActive()) {
            return;
        }

        investmentFund.terminate();

        final ExecutorService service = Executors.newSingleThreadExecutor();
        service.submit(() -> {
            try {
                while (!investmentFund.isTerminated()) {
                    TimeUnit.SECONDS.sleep(5);
                }
            } catch (InterruptedException ignored) {

            } finally {
                synchronized (this) {
                    removeAssetFromBriefcase(investmentFund);
                    investmentFunds.remove(investmentFund);
                }
            }
        });
    }

    public void removeAssetFromBriefcase(final Asset asset) {
        synchronized (this) {
            investors.stream()
                    .map(Entity::getBriefcase)
                    .forEach(briefcase -> briefcase.removeEntirely(asset));

            investmentFunds.stream()
                    .map(Entity::getBriefcase)
                    .forEach(briefcase -> briefcase.removeEntirely(asset));
        }

        player.getBriefcase().removeEntirely(asset);
    }

    /**
     * Creates new Stock Exchange with random values generated by Croupier.
     * @return StockExchange with random values.
     */
    public StockExchange addStockExchange() {
        final String city = croupier.drawCity();

        StockExchange stockExchange = StockExchangeBuilder.builder()
                .withName("Stock exchange in " + city)
                .withCountry(croupier.drawCountry())
                .withCity(city)
                .withAddress(croupier.drawAddress())
                .withCurrency(croupier.drawCurrency())
                .withMargin(croupier.drawMargin())
                .withIndices(new ArrayList<>())
                .withCompanies(new ArrayList<>())
                .build();

        stockExchanges.add(stockExchange);
        return stockExchange;
    }

    public void removeStockExchange(final StockExchange stockExchange) {
        stockExchange.getCompaniesService().shutdownNow();
        stockExchanges.remove(stockExchange);

        synchronized (this) {
            stockExchange.getCompanies().forEach(this::removeAssetFromBriefcase);
        }
    }

    /**
     * Creates new Currency Exchange with random values generated by Exchange Croupier.
     * @return CurrencyExchange with random values.
     */
    public CurrencyExchange addCurrencyExchange() {
        final String city = croupier.drawCity();

        CurrencyExchange currencyExchange = CurrencyExchangeBuilder.builder()
                .withName("Currency exchange in " + city)
                .withCountry(croupier.drawCountry())
                .withCity(city)
                .withAddress(croupier.drawAddress())
                .withCurrency(croupier.drawCurrency())
                .withMargin(croupier.drawMargin())
                .withCurrencies(new ArrayList<>())
                .build();

        currencyExchanges.add(currencyExchange);
        return currencyExchange;
    }

    public void removeCurrencyExchange(final CurrencyExchange currencyExchange) {
        currencyExchanges.remove(currencyExchange);

        synchronized (this) {
            currencyExchange.getCurrencies().forEach(this::removeAssetFromBriefcase);
        }
    }

    /**
     * Creates new CommodityExchange with random values generated by Exchange Croupier.
     * @return CommodityExchange with random values.
     */
    public CommodityExchange addCommodityExchange() {
        final String city = croupier.drawCity();

        CommodityExchange commodityExchange = CommodityExchangeBuilder.builder()
                .withName("Commodity exchange in " + city)
                .withCountry(croupier.drawCountry())
                .withCity(city)
                .withAddress(croupier.drawAddress())
                .withCurrency(croupier.drawCurrency())
                .withMargin(croupier.drawMargin())
                .withCommodities(new ArrayList<>())
                .build();

        commodityExchanges.add(commodityExchange);
        return commodityExchange;
    }

    public void removeCommodityExchange(final CommodityExchange commodityExchange) {
        commodityExchanges.remove(commodityExchange);

        synchronized (this) {
            commodityExchange.getCommodities().forEach(this::removeAssetFromBriefcase);
        }
    }

    public Company addCompany(final StockExchange stockExchange) {
        double openingRate = croupier.drawOpeningRate();

        Company company = CompanyBuilder.builder()
                .withName(croupier.drawCompanyName())
                .withDateOfFirstValuation(croupier.drawDateOfFirstValuation())
                .withOpeningQuotation(openingRate)
                .withCurrentRate(openingRate)
                .withMaxRate(openingRate)
                .withMinRate(openingRate)
                .withRateChanges(new ArrayList<>(Collections.singletonList(openingRate)))
                .withNumberOfAssets(croupier.drawNumberOfAssets())
                .withProfit(croupier.drawProfit())
                .withRevenue(croupier.drawRevenue())
                .withEquityCapital(croupier.drawEquityCapital())
                .withOpeningCapital(croupier.drawOpeningCapital())
                .withVolume(0)
                .withTurnover(0)
                .withMargin(stockExchange.getMargin())
                .build();

        stockExchange.getCompaniesService().submit(company);
        stockExchange.addCompany(company);
        stockExchange.updateIndices();
        return company;
    }

    public void removeCompany(final Company company, final StockExchange stockExchange) {
        if (!company.isActive()) {
            return;
        }

        company.terminate();

        final ExecutorService service = Executors.newSingleThreadExecutor();
        service.submit(() -> {
            try {
                while (!company.isTerminated()) {
                    TimeUnit.SECONDS.sleep(5);
                }
            } catch (InterruptedException ignored) {

            } finally {
                synchronized (this) {
                    removeAssetFromBriefcase(company);
                    stockExchange.removeCompany(company);
                }

                stockExchange.updateIndices();
            }
        });
    }

    public Optional<Commodity> addCommodity(final CommodityExchange commodityExchange) {
        List<String> names = commodityExchange.getCommodities().stream()
                .map(commodity -> commodity.getName() + ";" + commodity.getUnitOfTrading())
                .collect(Collectors.toList());

        List<String> availableNames = new ArrayList<>(commodityNames);
        availableNames.removeAll(names);

        if (availableNames.isEmpty()) {
            return Optional.empty();
        }

        final String name = Croupiers.drawString(Arrays.copyOf(availableNames.toArray(), availableNames.size(), String[].class));
        final double currentRate = croupier.drawCurrentRate();

        final Commodity commodity = CommodityBuilder.builder()
                .withName(name.split(";")[0])
                .withCurrentRate(currentRate)
                .withMinRate(currentRate)
                .withMaxRate(currentRate)
                .withRateChanges(new ArrayList<>(Collections.singletonList(currentRate)))
                .withMargin(commodityExchange.getMargin())
                .withUnitOfTrading(name.split(";")[1])
                .withCurrency(commodityExchange.getCurrency())
                .build();

        commodityExchange.addResource(commodity);
        return Optional.of(commodity);
    }

    public synchronized void removeCommodity(final Commodity commodity, final CommodityExchange commodityExchange) {
        removeAssetFromBriefcase(commodity);
        commodityExchange.removeResource(commodity);
    }

    public Optional<Currency> addCurrency(final CurrencyExchange currencyExchange) {
        List<String> names = currencyExchange.getCurrencies().stream()
                .map(AbstractAsset::getName)
                .collect(Collectors.toList());

        List<String> availableNames = new ArrayList<>(currencyNames);
        availableNames.removeAll(names);

        if (availableNames.isEmpty()) {
            return Optional.empty();
        }

        final String name = Croupiers.drawString(Arrays.copyOf(availableNames.toArray(), availableNames.size(), String[].class));
        final double currentRate = croupier.drawCurrentRate();

        final Currency currency = CurrencyBuilder.builder()
                .withName(name)
                .withCurrentRate(currentRate)
                .withMinRate(currentRate)
                .withMaxRate(currentRate)
                .withRateChanges(new ArrayList<>(Collections.singletonList(currentRate)))
                .withMargin(currencyExchange.getMargin())
                .withCountries(Arrays.asList(croupier.drawCountries()))
                .withComparisonCurrency(mainCurrency)
                .build();

        currencyExchange.addCurrency(currency);
        return Optional.of(currency);
    }

    public synchronized void removeCurrency(final Currency currency, final CurrencyExchange currencyExchange) {
        removeAssetFromBriefcase(currency);
        currencyExchange.removeCurrency(currency);
    }

    public Optional<Index> addIndex(final StockExchange stockExchange) {
        final List<String> indices = stockExchange.getIndices().stream()
                .map(Index::getType)
                .map(Enum::name)
                .collect(Collectors.toList());

        final List<String> available = Stream.of(IndexType.values())
                .map(Enum::name)
                .collect(Collectors.toList());

        available.removeAll(indices);

        if (available.isEmpty()) {
            return Optional.empty();
        }

        final String typeValue = Croupiers.drawString(Arrays.copyOf(available.toArray(), available.size(), String[].class));
        final IndexType type = IndexType.valueOf(typeValue);

        switch (type) {
            case MAX: {
                final NumericMaxIndex index = NumericMaxIndexBuilder.builder()
                        .withName("Top 5 companies")
                        .withSize(5)
                        .withContent(new ArrayList<>(stockExchange.getCompanies()))
                        .withValue(0.0D)
                        .build();

                stockExchange.addIndex(index);
                return Optional.of(index);
            }
            case MIN: {
                final NumericMinIndex index = NumericMinIndexBuilder.builder()
                        .withName("Last 5 companies")
                        .withSize(5)
                        .withContent(new ArrayList<>(stockExchange.getCompanies()))
                        .withValue(0.0D)
                        .build();

                stockExchange.addIndex(index);
                return Optional.of(index);
            }
            default:
                return Optional.empty();
        }
    }

    public void removeIndex(final Index index, final StockExchange stockExchange) {
        stockExchange.removeIndex(index);
    }

    public Player getPlayer() {
        return this.player;
    }

    public ObservableList<StockExchange> getStockExchanges() {
        return stockExchanges;
    }

    public ObservableList<CurrencyExchange> getCurrencyExchanges() {
        return currencyExchanges;
    }

    public ObservableList<CommodityExchange> getCommodityExchanges() {
        return commodityExchanges;
    }

    public ObservableList<Investor> getInvestors() {
        return investors;
    }

    public ObservableList<InvestmentFund> getInvestmentFunds() {
        return investmentFunds;
    }

    public List<String> getCommodityNames() {
        return commodityNames;
    }

    public List<String> getCurrencyNames() {
        return currencyNames;
    }

    public Croupier getCroupier() {
        return croupier;
    }

    public Currency getMainCurrency() {
        return mainCurrency;
    }

    public void setMainCurrency(Currency mainCurrency) {
        this.mainCurrency = mainCurrency;
    }

    public ExecutorService getEntitiesService() {
        return entitiesService;
    }
}
