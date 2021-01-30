package org.example.marketstock.simulation;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.vavr.Tuple3;
import javafx.collections.ObservableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

/**
 * Represents a simplified world where entities can buy and sell assets.
 * Takes care of adding or removing new objects safely.
 * On top of that, provides methods for entities to choose assets and perform transactions.
 *
 * @since 1.0.0
 * @author Domink Szmyt
 */
@JsonSerialize(using = SimulationSerializer.class)
@JsonDeserialize(using = SimulationDeserializer.class)
public class Simulation {

    private static final Logger LOGGER = LogManager.getLogger(Simulation.class);

    private final Player player;
    private final ObservableList<StockExchange> stockExchanges;
    private final ObservableList<CurrencyExchange> currencyExchanges;
    private final ObservableList<CommodityExchange> commodityExchanges;
    private final ObservableList<Investor> investors;
    private final ObservableList<InvestmentFund> investmentFunds;
    private final List<String> commodityNames;
    private final List<String> currencyNames;
    private final Croupier croupier;
    private final Currency mainCurrency;
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

    /**
     * Performs a purchase operation. The purchase operation consists of several steps:
     * <ol>
     *     <li>Evaluating the final number of a selected asset.</li>
     *     <li>Calculating price - result is used to change entity's budget and company's turnover.</li>
     *     <li>Changing the rate of a selected asset.</li>
     *     <li>Decreasing the number of a selected asset if necessary.</li>
     *     <li>Updating company's turnover and volume using calculated price and final number.</li>
     *     <li>Updating indices if necessary.</li>
     * </ol>
     * Since there is a chance that the rate or count of a selected asset may change between choosing and buying it,
     * method changes the final number of asset if necessary.
     *
     * @param asset An asset to buy.
     * @param number The number of a selected asset.
     * @param originalRate The original rate of a selected asset.
     * @param entity An entity that wants to buy selected asset.
     * @return The number of successfully purchased asset.
     */
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
            LOGGER.warn("[PURCHASE][RATE_WARN]: Number has changed from {} to {} when purchasing {} for {}.",
                    number, finalNumber, asset, entity);

            if (finalNumber == 0) return finalNumber;
            else if (finalNumber < 0) {
                LOGGER.error("[PURCHASE][RATE_ERROR]: Negative number of {}, to purchase by {}.", asset, entity);
                // FIXME should throw exception because unacceptable
                return 0;
            }
        }

        // If available number has changed between choosing an asset and buying it,
        // assign new possible number to final number of asset.
        if (asset instanceof Countable && number > ((Countable) asset).getNumberOfAssets()) {
            finalNumber = ((Countable) asset).getNumberOfAssets();
            LOGGER.warn("[PURCHASE][NUMBER_WARN]: Number has changed from {} to {} when purchasing {} for {}.",
                    number, finalNumber, asset, entity);

            if (finalNumber == 0) return finalNumber;
            else if (finalNumber < 0) {
                LOGGER.error("[PURCHASE][NUMBER_ERROR]: Negative number of {}, to purchase by {}.", asset, entity);
                // FIXME should throw exception because unacceptable
                return 0;
            }
        }

        final double price = currentRate * finalNumber;
        entity.addAsset(price, asset, finalNumber);
        LOGGER.debug("[PURCHASE]: {} bought {} of {} for {}.", entity, finalNumber, asset, price);

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

    /**
     * Performs a sell operation. The sell operation consists of several steps:
     * <ol>
     *     <li>Calculating price - result is used to change company's turnover.</li>
     *     <li>Calculating price minus asset's margin - result is used to increase entity's budget.</li>
     *     <li>Changing the rate of a selected asset.</li>
     *     <li>Increasing the number of a selected asset if necessary.</li>
     *     <li>Updating company's turnover and volume using calculated price and initial number.</li>
     *     <li>Updating indices if necessary.</li>
     * </ol>
     *
     * @param asset An asset to sell.
     * @param number The number of a selected asset
     * @param entity An entity that wants to sell the selected asset.
     */
    public synchronized void sellSelectedResource(final Asset asset,
                                                  final int number,
                                                  final Entity entity) {

        final double currentRate = asset.getCurrentRate();
        final double price = currentRate * number;
        final double priceMinusMargin = price - price * asset.getMargin();
        entity.subtractAsset(priceMinusMargin, asset, number);
        LOGGER.debug("[PURCHASE]: {} sold {} of {} for {}.", entity, asset, number, priceMinusMargin);

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

    /**
     * Performs a purchase selection operation. The operation consists of several steps:
     * <ol>
     *     <li>Removing the entity from the list of available assets.</li>
     *     <li>Selecting an asset with the lowest current rate at that moment.</li>
     *     <li>Calculating the number of a selected asset while taking into account entity's budget and available stock.</li>
     * </ol>
     * Since there is a chance that an entity may be an asset as well,
     * method removes entity from available assets just in case.
     *
     * @param entity An entity that wants to choose asset.
     * @return A tuple where the first value is the selected asset; second - the selected number; third - original rate.
     */
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

        if (possibleNumber == 0) return Optional.empty();

        final int chosenNumber = croupier.getRandom().nextInt(possibleNumber) + 1;
        LOGGER.debug("[SELECTION]: {} chose {} of {} to buy.", entity, chosenNumber, chosenAsset);
        return Optional.of(new Tuple3<>(chosenAsset, chosenNumber, chosenAsset.getCurrentRate()));
    }

    /**
     * Performs a sell selection operation. The operation consists of several steps:
     * <ol>
     *     <li>Selecting an asset with the highest current rate at that moment.</li>
     *     <li>Calculating the number of a selected asset while taking into account available stock.</li>
     * </ol>
     *
     * @param entity An entity that wants to choose asset to sell.
     * @return A tuple where the first value is the selected asset; second - the number of asset; third - original rate.
     */
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
        LOGGER.debug("[SELECTION]: {} chose {} of {} to sell.", entity, chosenNumber, chosenAsset);
        return Optional.of(new Tuple3<>(chosenAsset, chosenNumber, chosenAsset.getCurrentRate()));
    }

    /**
     * Adds a random investor and investment fund when number of available assets is divisible by 5.
     * Method is used in {@link org.example.marketstock.fxml.SimulationLayoutController} after the user adds a random asset.
     */
    public synchronized void issueEntities() {
        if (getAvailableAssets().size() % 5 == 0) {
            addInvestor();
            addInvestmentFund();
        }
    }

    /**
     * Creates the list of available assets from exchanges as well as investment funds.
     * @return A list of available assets.
     */
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

    /**
     * Creates a new {@link Investor} with random values generated by {@link Croupier} and starts new thread.
     * @return A new investor with random values.
     */
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
        LOGGER.info("[ADDED]: {}", investor);
        return investor;
    }

    /**
     * Gracefully stops investor's thread, then removes them from the {@link Simulation}.
     * @param investor An investor that will be removed from the {@link Simulation}.
     */
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

    /**
     * Creates new {@link InvestmentFund} with random value generated by {@link Croupier} and starts new thread.
     * @return A new investment fund with random values.
     */
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
        LOGGER.info("[ADDED]: {}", investmentFund);
        return investmentFund;
    }

    /**
     * Gracefully stops investment fund's thread, then removes it from the {@link Simulation}
     * and briefcases that belong to each entity in the {@link Simulation}.
     * @param investmentFund An investment fund that will be removed from the {@link Simulation}.
     */
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

    /**
     * Removes the selected asset from briefcases that belong to each entity in the {@link Simulation}.
     * @param asset An asset that will be removed from all briefcases.
     */
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
     * Creates new {@link StockExchange} with random values generated by {@link Croupier}.
     * @return A new stock exchange with random values.
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

        LOGGER.debug("[CREATED]: {}", stockExchange);
        stockExchanges.add(stockExchange);
        return stockExchange;
    }

    /**
     * Shuts down every company's thread and removes all companies from briefcases
     * that belong to each entity in the {@link Simulation}.
     * Then, removes selected stock exchange from the {@link Simulation}.
     * @param stockExchange A stock exchange that will be removed from the {@link Simulation}.
     */
    public void removeStockExchange(final StockExchange stockExchange) {
        stockExchange.getCompaniesService().shutdownNow();
        stockExchanges.remove(stockExchange);

        synchronized (this) {
            stockExchange.getCompanies().forEach(this::removeAssetFromBriefcase);
        }
    }

    /**
     * Creates a new {@link CurrencyExchange} with random values generated by {@link Croupier}.
     * @return A new currency exchange with random values.
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

        LOGGER.debug("[CREATED]: {}", currencyExchange);
        currencyExchanges.add(currencyExchange);
        return currencyExchange;
    }

    /**
     * Removes the selected currency exchange from the {@link Simulation}.
     * Then, removes every currency from briefcases that belong to each entity in the {@link Simulation}.
     * @param currencyExchange A currency exchange that will be removed from the {@link Simulation}.
     */
    public void removeCurrencyExchange(final CurrencyExchange currencyExchange) {
        currencyExchanges.remove(currencyExchange);

        synchronized (this) {
            currencyExchange.getCurrencies().forEach(this::removeAssetFromBriefcase);
        }
    }

    /**
     * Creates a new {@link CommodityExchange} with random values generated by {@link Croupier}.
     * @return A new commodity exchange with random values.
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

        LOGGER.debug("[CREATED]: {}", commodityExchange);
        commodityExchanges.add(commodityExchange);
        return commodityExchange;
    }

    /**
     * Removes the selected commodity exchange from the {@link Simulation}.
     * Then, removes every commodity from briefcases that belong to each entity in the {@link Simulation}.
     * @param commodityExchange A commodity exchange that will be removed from the {@link Simulation}.
     */
    public void removeCommodityExchange(final CommodityExchange commodityExchange) {
        commodityExchanges.remove(commodityExchange);

        synchronized (this) {
            commodityExchange.getCommodities().forEach(this::removeAssetFromBriefcase);
        }
    }

    /**
     * Creates a new {@link Company} with random values generated by {@link Croupier}.
     * The company is then added to the selected {@link StockExchange} and a new thread is started.
     * @param stockExchange - A selected stock exchange that will own the company.
     * @return A new company with random values.
     */
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

        LOGGER.debug("[CREATED]: {}", company);
        stockExchange.getCompaniesService().submit(company);
        stockExchange.addCompany(company);
        stockExchange.updateIndices();
        return company;
    }

    /**
     * Gracefully stops company's thread, then removes it from the selected {@link StockExchange}
     * and briefcases that belong to each entity in the {@link Simulation}.
     * @param company A company that will be removed from the selected stock exchange.
     * @param stockExchange A stock exchange that will have one of it's companies removed.
     */
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

    /**
     * Creates a new {@link Commodity} with random values generated by {@link Croupier}.
     * There is a chance that there won't be any available commodities for a selected commodity exchange.
     * In such case, an empty {@link Optional} is returned.
     * @param commodityExchange A commodity exchange that will own the commodity.
     * @return If there are no available names for commodity exchange, optional is empty.
     */
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

        LOGGER.debug("[CREATED]: {}", commodity);
        commodityExchange.addResource(commodity);
        return Optional.of(commodity);
    }

    /**
     * Removes a commodity from the selected {@link CommodityExchange}
     * and briefcases that belong to each entity in the {@link Simulation}.
     * @param commodity A commodity that will be removed from the selected commodity exchange.
     * @param commodityExchange A commodity exchange that will have one of it's commodities removed.
     */
    public synchronized void removeCommodity(final Commodity commodity, final CommodityExchange commodityExchange) {
        removeAssetFromBriefcase(commodity);
        commodityExchange.removeResource(commodity);
    }

    /**
     * Creates a new {@link Currency} with random values generated by {@link Croupier}.
     * There is a chance that there won't be any available currencies for a selected currency exchange.
     * In such case, an empty {@link Optional} is returned.
     * @param currencyExchange A currency exchange that will own the currency.
     * @return If there are no available names for currency exchange, optional is empty.
     */
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

        LOGGER.debug("[CREATED]: {}", currency);
        currencyExchange.addCurrency(currency);
        return Optional.of(currency);
    }

    /**
     * Removes a currency from the selected {@link CurrencyExchange}
     * and briefcases that belong to each entity in the {@link Simulation}.
     * @param currency A currency that will be removed from the selected currency exchange.
     * @param currencyExchange A currency exchange that will have one of it's currencies removed.
     */
    public synchronized void removeCurrency(final Currency currency, final CurrencyExchange currencyExchange) {
        removeAssetFromBriefcase(currency);
        currencyExchange.removeCurrency(currency);
    }

    /**
     * Creates a new {@link Index} with random values generated by {@link Croupier}.
     * There is a chance that there won't be any available indices for the selected stock exchange.
     * In such case, an empty {@link Optional} is returned.
     * @param stockExchange A stock exchange that will own the index.
     * @return If there are no available indices for stock exchange, optional is empty.
     */
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
            case NUMERIC_MAX: {
                final NumericMaxIndex index = NumericMaxIndexBuilder.builder()
                        .withName("Top 5 companies")
                        .withSize(5)
                        .withContent(new ArrayList<>(stockExchange.getCompanies()))
                        .withValue(0.0D)
                        .build();

                LOGGER.debug("[CREATED]: {}", index);
                stockExchange.addIndex(index);
                return Optional.of(index);
            }
            case NUMERIC_MIN: {
                final NumericMinIndex index = NumericMinIndexBuilder.builder()
                        .withName("Last 5 companies")
                        .withSize(5)
                        .withContent(new ArrayList<>(stockExchange.getCompanies()))
                        .withValue(0.0D)
                        .build();

                LOGGER.debug("[CREATED]: {}", index);
                stockExchange.addIndex(index);
                return Optional.of(index);
            }
            default:
                return Optional.empty();
        }
    }

    /**
     * Removes an index from the selected {@link StockExchange}.
     * @param index An index that will be removed from the selected stock exchange.
     * @param stockExchange A stock exchange that will have one of it's indices removed.
     */
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

    public ExecutorService getEntitiesService() {
        return entitiesService;
    }
}
