package org.example.marketstock.simulation;

import io.vavr.Tuple3;
import javafx.collections.FXCollections;
import org.example.marketstock.models.asset.Asset;
import org.example.marketstock.models.asset.Commodity;
import org.example.marketstock.models.asset.Currency;
import org.example.marketstock.models.asset.builder.CommodityBuilder;
import org.example.marketstock.models.asset.builder.CurrencyBuilder;
import org.example.marketstock.models.briefcase.builder.BriefcaseBuilder;
import org.example.marketstock.models.company.Company;
import org.example.marketstock.models.entity.Entity;
import org.example.marketstock.models.entity.InvestmentFund;
import org.example.marketstock.models.entity.Investor;
import org.example.marketstock.models.entity.Player;
import org.example.marketstock.models.entity.builder.InvestmentFundBuilder;
import org.example.marketstock.models.entity.builder.InvestorBuilder;
import org.example.marketstock.models.exchange.CommodityExchange;
import org.example.marketstock.models.exchange.CurrencyExchange;
import org.example.marketstock.models.exchange.StockExchange;
import org.example.marketstock.models.index.Index;
import org.example.marketstock.simulation.builder.SimulationBuilder;
import org.example.marketstock.simulation.croupier.builder.CroupierBuilder;
import org.example.marketstock.simulation.json.SimpleJsonReader;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.atIndex;

public class SimulationTest {

    private static Player player;
    private final List<String> currencies = new ArrayList<>(Arrays.asList("PLN", "EUR", "USD"));
    private final List<String> commodities = new ArrayList<>(Arrays.asList("gold;kg", "silver;kg", "copper;kg"));
    private Simulation subject;

    @BeforeAll
    public static void initialize() {
        player = Player.getInstance(
                "TestFirstName",
                "TestLastName",
                10_000D,
                BriefcaseBuilder.emptyBriefcase().build());
    }

    @BeforeEach
    public void setUp() {
        subject = SimulationBuilder.builder()
                .withPlayer(player)
                .withStockExchanges(FXCollections.observableArrayList())
                .withCurrencyExchanges(FXCollections.observableArrayList())
                .withCommodityExchange(FXCollections.observableArrayList())
                .withInvestors(FXCollections.observableArrayList())
                .withInvestmentFunds(FXCollections.observableArrayList())
                .withCommodityNames(commodities)
                .withCurrencyNames(currencies)
                .withCroupier(CroupierBuilder.builder()
                        .withJsonReader(new SimpleJsonReader())
                        .withRandom(new Random())
                        .build())
                .withMainCurrency(CurrencyBuilder.builder()
                        .withName("MainCurrency")
                        .withRateChanges(new ArrayList<>(Collections.singletonList(0.0)))
                        .withCountries(new ArrayList<>())
                        .build())
                .build();
    }

    @AfterEach
    public void cleanUp() {
        subject.getStockExchanges().stream()
                .map(StockExchange::getCompaniesService)
                .forEach(ExecutorService::shutdownNow);
        subject.getEntitiesService().shutdownNow();
    }

    @Test
    public void should_add_stock_exchange() {

        // when
        final StockExchange actual = subject.addStockExchange();

        // then
        assertThat(actual.getName()).isEqualTo("Stock exchange in " + actual.getCity());
        assertThat(actual.getCompanies()).isEmpty();
        assertThat(actual.getIndices()).isEmpty();
        assertThat(subject.getStockExchanges()).containsExactly(actual);
    }

    @Test
    public void should_remove_stock_exchange_with_present_company_and_index() {

        // given
        final StockExchange actual = subject.addStockExchange();
        subject.addCompany(actual);
        final Optional<Index> optionalIndex = subject.addIndex(actual);

        assertThat(optionalIndex).isPresent();

        // when
        subject.removeStockExchange(actual);

        // then
        assertThat(subject.getStockExchanges()).doesNotContain(actual);
    }

    @Test
    public void should_add_commodity_exchange() {

        // when
        final CommodityExchange actual = subject.addCommodityExchange();

        // then
        assertThat(actual.getName()).isEqualTo("Commodity exchange in " + actual.getCity());
        assertThat(actual.getCommodities()).isEmpty();
        assertThat(subject.getCommodityExchanges()).containsExactly(actual);
    }

    @Test
    public void should_remove_commodity_exchange_with_present_commodity() {

        // given
        final CommodityExchange actual = subject.addCommodityExchange();
        final Optional<Commodity> optionalCommodity = subject.addCommodity(actual);

        assertThat(optionalCommodity).isPresent();

        // when
        subject.removeCommodityExchange(actual);

        // then
        assertThat(subject.getCommodityExchanges()).doesNotContain(actual);
    }

    @Test
    public void should_add_currency_exchange() {

        // when
        final CurrencyExchange actual = subject.addCurrencyExchange();

        // then
        assertThat(actual.getName()).isEqualTo("Currency exchange in " + actual.getCity());
        assertThat(actual.getCurrencies()).isEmpty();
        assertThat(subject.getCurrencyExchanges()).containsExactly(actual);
    }

    @Test
    public void should_remove_currency_exchange_with_present_currency() {

        // given
        final CurrencyExchange actual = subject.addCurrencyExchange();
        final Optional<Currency> optionalCurrency = subject.addCurrency(actual);

        assertThat(optionalCurrency).isPresent();

        // when
        subject.removeCurrencyExchange(actual);

        // then
        assertThat(subject.getCurrencyExchanges()).doesNotContain(actual);
    }

    @Test
    public void should_add_company_with_index() {

        // given
        final StockExchange stockExchange = subject.addStockExchange();
        final Optional<Index> optionalIndex = subject.addIndex(stockExchange);

        assertThat(optionalIndex).isPresent();
        final Index index = optionalIndex.get();

        // when
        final Company actual = subject.addCompany(stockExchange);

        // then
        assertThat(stockExchange.getCompanies()).containsExactly(actual);
        assertThat(index.getContent()).containsExactly(actual);
        assertThat(actual.getMargin()).isEqualTo(stockExchange.getMargin());
        assertThat(actual.getCurrentRate())
                .isEqualTo(actual.getOpeningQuotation())
                .isEqualTo(actual.getMinRate())
                .isEqualTo(actual.getMaxRate())
                .isIn(actual.getRateChanges());
        assertThat(actual.getVolume()).isZero();
        assertThat(actual.getTurnover()).isZero();
    }

    @Test
    public void should_remove_company_with_index() throws InterruptedException {

        // given
        final StockExchange stockExchange = subject.addStockExchange();
        final Optional<Index> optionalIndex = subject.addIndex(stockExchange);

        assertThat(optionalIndex).isPresent();
        final Index index = optionalIndex.get();
        final Company actual = subject.addCompany(stockExchange);

        // when
        subject.removeCompany(actual, stockExchange);
        TimeUnit.SECONDS.sleep(5);

        // then
        assertThat(stockExchange.getCompanies()).doesNotContain(actual);
        assertThat(index.getContent()).doesNotContain(actual);
        assertThat(actual.isActive()).isFalse();
        assertThat(actual.isTerminated()).isTrue();
    }

    @Test
    public void should_add_commodity() {

        // given
        final CommodityExchange commodityExchange = subject.addCommodityExchange();

        // when
        final Optional<Commodity> optionalCommodity = subject.addCommodity(commodityExchange);

        // then
        assertThat(optionalCommodity).isPresent();
        final Commodity actual = optionalCommodity.get();

        assertThat(commodityExchange.getCommodities()).containsExactly(actual);
        assertThat(actual.getMargin()).isEqualTo(commodityExchange.getMargin());
        assertThat(actual.getCurrency()).isEqualTo(commodityExchange.getCurrency());
    }

    @Test
    public void should_remove_commodity() {

        // given
        final CommodityExchange commodityExchange = subject.addCommodityExchange();
        final Optional<Commodity> optionalCommodity = subject.addCommodity(commodityExchange);

        assertThat(optionalCommodity).isPresent();
        final Commodity commodity = optionalCommodity.get();

        // when
        subject.removeCommodity(commodity, commodityExchange);

        // then
        assertThat(commodityExchange.getCommodities()).doesNotContain(commodity);
    }

    @Test
    public void should_add_currency() {

        // given
        final CurrencyExchange currencyExchange = subject.addCurrencyExchange();

        // when
        final Optional<Currency> optionalCurrency = subject.addCurrency(currencyExchange);

        // then
        assertThat(optionalCurrency).isPresent();
        final Currency actual = optionalCurrency.get();

        assertThat(currencyExchange.getCurrencies()).containsExactly(actual);
        assertThat(actual.getMargin()).isEqualTo(currencyExchange.getMargin());
        assertThat(actual.getComparisonCurrency()).isEqualTo(subject.getMainCurrency());
    }

    @Test
    public void should_remove_currency() {

        // given
        final CurrencyExchange currencyExchange = subject.addCurrencyExchange();
        final Optional<Currency> optionalCurrency = subject.addCurrency(currencyExchange);

        assertThat(optionalCurrency).isPresent();
        final Currency currency = optionalCurrency.get();

        // when
        subject.removeCurrency(currency, currencyExchange);

        // then
        assertThat(currencyExchange.getCurrencies()).doesNotContain(currency);
    }

    @Test
    public void should_add_index_without_companies() {

        // given
        final StockExchange stockExchange = subject.addStockExchange();

        // when
        final Optional<Index> optionalIndex = subject.addIndex(stockExchange);

        // then
        assertThat(optionalIndex).isPresent();
        final Index actual = optionalIndex.get();

        assertThat(stockExchange.getIndices()).containsExactly(actual);
    }

    @Test
    public void should_remove_index_with_company() {

        // given
        final StockExchange stockExchange = subject.addStockExchange();
        final Company company = subject.addCompany(stockExchange);
        final Optional<Index> optionalIndex = subject.addIndex(stockExchange);

        assertThat(optionalIndex).isPresent();
        final Index actual = optionalIndex.get();

        // when
        subject.removeIndex(actual, stockExchange);

        // then
        assertThat(stockExchange.getIndices()).doesNotContain(actual);
        assertThat(stockExchange.getCompanies()).containsExactly(company);
    }

    @Test
    public void should_add_investor() {

        // when
        final Investor investor = subject.addInvestor();

        // then
        assertThat(subject.getInvestors()).containsExactly(investor);
        assertThat(investor.isActive()).isTrue();
        assertThat(investor.getSimulation()).isEqualTo(subject);
    }

    @Test
    public void should_remove_investor() throws InterruptedException {

        // given
        final Investor actual = subject.addInvestor();

        // when
        subject.removeInvestor(actual);
        TimeUnit.SECONDS.sleep(5);

        // then
        assertThat(subject.getInvestors()).doesNotContain(actual);
        assertThat(actual.isActive()).isFalse();
        assertThat(actual.isTerminated()).isTrue();
    }

    @Test
    public void should_add_investment_fund() {

        // when
        final InvestmentFund investmentFund = subject.addInvestmentFund();

        // then
        assertThat(subject.getInvestmentFunds()).containsExactly(investmentFund);
        assertThat(investmentFund.getCurrentRate())
                .isEqualTo(investmentFund.getMinRate())
                .isEqualTo(investmentFund.getMaxRate())
                .isIn(investmentFund.getRateChanges());
        assertThat(investmentFund.isActive()).isTrue();
        assertThat(investmentFund.getSimulation()).isEqualTo(subject);
    }

    @Test
    public void should_remove_investment_fund() throws InterruptedException {

        // given
        final InvestmentFund actual = subject.addInvestmentFund();

        // when
        subject.removeInvestmentFund(actual);
        TimeUnit.SECONDS.sleep(5);

        // then
        assertThat(subject.getInvestmentFunds()).doesNotContain(actual);
        assertThat(actual.isActive()).isFalse();
        assertThat(actual.isTerminated()).isTrue();
    }

    @Test
    public void should_buy_selected_asset_with_company_implementation() throws InterruptedException {

        // given
        final StockExchange stockExchange = subject.addStockExchange();
        final Optional<Index> optionalIndex = subject.addIndex(stockExchange);

        assertThat(optionalIndex).isPresent();
        final Index index = optionalIndex.get();

        final Company company = subject.addCompany(stockExchange);

        stockExchange.getCompaniesService().shutdownNow();
        while (!company.isTerminated()) {
            TimeUnit.SECONDS.sleep(1);
        }

        final double initialCurrentRate = company.getCurrentRate();
        final int initialNumberOfAssets = company.getNumberOfAssets();
        final double initialBudget = player.getBudget();

        final double newCurrentRate = initialCurrentRate + initialCurrentRate * 0.05;

        // when
        final int actual = subject.buySelectedResource(company, 1, initialCurrentRate, player);

        // then
        assertThat(actual).isEqualTo(1);

        final Map<Asset, Integer> map = new HashMap<>();
        map.put(company, 1);

        assertThat(company.getCurrentRate()).isEqualTo(newCurrentRate);
        assertThat(company.getMinRate()).isEqualTo(initialCurrentRate);
        assertThat(company.getMaxRate()).isEqualTo(newCurrentRate);
        assertThat(company.getRateChanges())
                .hasSize(2)
                .contains(initialCurrentRate, atIndex(0))
                .contains(newCurrentRate, atIndex(1));
        assertThat(company.getNumberOfAssets()).isEqualTo(initialNumberOfAssets - 1);
        assertThat(index.getValue()).isEqualTo(newCurrentRate);
        assertThat(player.getBriefcase().getMap()).containsAllEntriesOf(map);
        assertThat(player.getBudget()).isEqualTo(initialBudget - initialCurrentRate);
    }

    @ParameterizedTest
    @CsvSource({
            "100, 5, 2, 4",
            "100, 5, 4, 2",
            "25, 5, 3, 5",
            "20, 5, 4, 1"
    })
    public void should_buy_different_number_because_rate_has_changed_before_purchase(final double budget,
                                                                                     final int numberOfAsset,
                                                                                     final double startRate,
                                                                                     final double finalRate) {

        // given
        final Investor investor = InvestorBuilder.builder()
                .withFirstName("Jordan")
                .withLastName("Belfort")
                .withBudget(budget)
                .withBriefcase(BriefcaseBuilder.emptyBriefcase().build())
                .build();

        final Commodity commodity = CommodityBuilder.builder()
                .withName("Gold")
                .withUnitOfTrading("kg")
                .withCurrentRate(startRate)
                .withMinRate(startRate)
                .withMaxRate(startRate)
                .withRateChanges(new ArrayList<>(Collections.singletonList(startRate)))
                .withCurrency("USD")
                .build();

        // when
        commodity.updateRate(finalRate);
        final double actualNumberOfAsset = subject.buySelectedResource(commodity, numberOfAsset, startRate, investor);

        // then
        final int expectedNumberOfAsset = (int) Math.floor((numberOfAsset * startRate) / finalRate);

        assertThat(actualNumberOfAsset).isEqualTo(expectedNumberOfAsset);
        assertThat(investor.getBudget()).isEqualTo(budget - (expectedNumberOfAsset * finalRate));
        assertThat(investor.getBriefcase().contains(commodity, expectedNumberOfAsset)).isTrue();
    }

    @ParameterizedTest
    @CsvSource({
            "10, 2, 2, 5",
            "20, 5, 3, 16",
    })
    public void should_not_buy_asset_after_rate_change_because_not_enough_budget(final double budget,
                                                                                 final int numberOfAsset,
                                                                                 final double startRate,
                                                                                 final double finalRate) {

        final Investor investor = InvestorBuilder.builder()
                .withFirstName("John")
                .withLastName("Broke")
                .withBudget(budget)
                .withBriefcase(BriefcaseBuilder.emptyBriefcase().build())
                .build();

        final Commodity commodity = CommodityBuilder.builder()
                .withName("Iron")
                .withUnitOfTrading("kg")
                .withCurrentRate(startRate)
                .withMinRate(startRate)
                .withMaxRate(startRate)
                .withRateChanges(new ArrayList<>(Collections.singletonList(startRate)))
                .withCurrency("USD")
                .build();

        // when
        commodity.updateRate(finalRate);
        final int actualNumberOfAsset = subject.buySelectedResource(commodity, numberOfAsset, startRate, investor);

        // then
        assertThat(actualNumberOfAsset).isZero();
        assertThat(investor.getBudget()).isEqualTo(budget);
        assertThat(investor.getBriefcase().isEmpty()).isTrue();
    }

    @Test
    public void should_sell_selected_asset_with_company_implementation() throws InterruptedException {

        // given
        final StockExchange stockExchange = subject.addStockExchange();
        final Optional<Index> optionalIndex = subject.addIndex(stockExchange);

        assertThat(optionalIndex).isPresent();
        final Index index = optionalIndex.get();

        final Company company = subject.addCompany(stockExchange);

        stockExchange.getCompaniesService().shutdownNow();
        while (!company.isTerminated()) {
            TimeUnit.SECONDS.sleep(1);
        }

        subject.buySelectedResource(company, 2, company.getCurrentRate(), player);

        final double rateAfterPurchase = company.getCurrentRate();
        final int numberOfAssetsAfterPurchase = company.getNumberOfAssets();
        final double budgetAfterPurchase = player.getBudget();

        final double newCurrentRate = rateAfterPurchase - rateAfterPurchase * 0.05;
        final double newCurrentRateMinusMargin = rateAfterPurchase - rateAfterPurchase * company.getMargin();

        // when
        subject.sellSelectedResource(company, 1, player);

        // then
        final Map<Asset, Integer> map = new HashMap<>();
        map.put(company, 1);

        assertThat(company.getCurrentRate()).isEqualTo(newCurrentRate);
        assertThat(company.getMaxRate()).isEqualTo(rateAfterPurchase);
        assertThat(company.getRateChanges())
                .hasSize(3)
                .contains(rateAfterPurchase, atIndex(1))
                .contains(newCurrentRate, atIndex(2));
        assertThat(company.getNumberOfAssets()).isEqualTo(numberOfAssetsAfterPurchase + 1);
        assertThat(index.getValue()).isEqualTo(newCurrentRate);
        assertThat(player.getBriefcase().getMap()).containsAllEntriesOf(map);
        assertThat(player.getBudget()).isEqualTo(budgetAfterPurchase + newCurrentRateMinusMargin);
    }

    @Test
    public void should_choose_the_cheapest_asset_to_buy() {

        // when
        final Investor investor = InvestorBuilder.builder()
                .withFirstName("Scrooge")
                .withLastName("McQuack")
                .withBudget(10_000)
                .withBriefcase(BriefcaseBuilder.emptyBriefcase().build())
                .build();

        final CommodityExchange commodityExchange = subject.addCommodityExchange();

        for (double i = 1; i <= 3; i++) {
            commodityExchange.addResource(CommodityBuilder.builder()
                    .withName("TestCommodity" + i)
                    .withCurrentRate(i * 2D)
                    .build()
            );
        }

        // when
        final Optional<Tuple3<Asset, Integer, Double>> selection = subject.chooseAssetToBuy(investor);

        // then
        assertThat(selection).isPresent();
        final Tuple3<Asset, Integer, Double> actual = selection.get();
        final Asset expectedAsset = commodityExchange.getCommodities().get(0);

        assertThat(actual._1).isEqualTo(expectedAsset);
        assertThat(actual._2).isEqualTo((int) (Math.floor(investor.getBudget() / expectedAsset.getCurrentRate())));
        assertThat(actual._3).isEqualTo(expectedAsset.getCurrentRate());
    }

    @Test
    public void should_choose_more_expensive_asset_with_investment_fund_impl() {

        // given
        final InvestmentFund investmentFund = InvestmentFundBuilder.builder()
                .withName("InvestmentFund")
                .withBudget(100D)
                .withCurrentRate(1D)
                .build();

        subject.getInvestmentFunds().add(investmentFund);

        final CommodityExchange commodityExchange = subject.addCommodityExchange();
        final Commodity expectedAsset = CommodityBuilder.builder()
                .withName("TestCommodity")
                .withCurrentRate(2D)
                .build();

        commodityExchange.addResource(expectedAsset);

        assertThat(subject.getAvailableAssets()).hasSameElementsAs(Arrays.asList(investmentFund, expectedAsset));

        // when
        final Optional<Tuple3<Asset, Integer, Double>> selection = subject.chooseAssetToBuy(investmentFund);

        // then
        assertThat(selection).isPresent();
        final Tuple3<Asset, Integer, Double> actual = selection.get();

        assertThat(actual._1).isEqualTo(expectedAsset);
        assertThat(actual._2).isEqualTo((int) (Math.floor(investmentFund.getBudget()) / expectedAsset.getCurrentRate()));
        assertThat(actual._3).isEqualTo(expectedAsset.getCurrentRate());
    }

    @Test
    public void should_not_choose_asset_to_buy_when_no_assets_available() {

        // given
        final Entity entity = InvestmentFundBuilder.builder().build();

        // when
        final Optional<Tuple3<Asset, Integer, Double>> actual = subject.chooseAssetToBuy(entity);

        // then
        assertThat(actual).isEmpty();
    }

    @ParameterizedTest
    @CsvSource({
            "1, 5",
            "4, 5",
            "0.1, 0.2",
            "100.1, 100.2"
    })
    public void should_not_choose_asset_to_buy_when_not_enough_budget(final double budget,
                                                                      final double currentRate) {

        // given
        final Investor investor = InvestorBuilder.builder()
                .withFirstName("John")
                .withLastName("Broke")
                .withBudget(budget)
                .withBriefcase(BriefcaseBuilder.emptyBriefcase().build())
                .build();

        final CommodityExchange commodityExchange = subject.addCommodityExchange();
        commodityExchange.addResource(CommodityBuilder.builder()
                .withName("TestCommodity")
                .withCurrentRate(currentRate)
                .build());

        // when
        final Optional<Tuple3<Asset, Integer, Double>> actual = subject.chooseAssetToBuy(investor);

        // then
        assertThat(actual).isEmpty();
    }

    @Test
    public void should_choose_the_most_expensive_asset_to_sell() {

        // given
        final Investor investor = InvestorBuilder.builder()
                .withFirstName("Scrooge")
                .withLastName("McQuack")
                .withBudget(10_000)
                .withBriefcase(BriefcaseBuilder.builder()
                        .withMap(new LinkedHashMap<>())
                        .build())
                .build();

        for (double i = 1; i <= 3; i++) {
            investor.getBriefcase().addOrIncrease(CommodityBuilder.builder()
                    .withName("TestCommodity" + i)
                    .withCurrentRate(i * 2D)
                    .build(), 100);
        }

        // when
        final Optional<Tuple3<Asset, Integer, Double>> selection = subject.chooseAssetToSell(investor);

        // then
        assertThat(selection).isPresent();
        final Tuple3<Asset, Integer, Double> actual = selection.get();
        final Asset expectedAsset = investor.getBriefcase().getAssets().get(2);

        assertThat(actual._1).isEqualTo(expectedAsset);
        assertThat(actual._2).isBetween(1, 100);
        assertThat(actual._3).isEqualTo(expectedAsset.getCurrentRate());
    }

    @Test
    public void should_not_choose_asset_to_sell_when_no_assets_available() {

        // given
        final Investor investor = InvestorBuilder.builder()
                .withFirstName("John")
                .withLastName("Broke")
                .withBriefcase(BriefcaseBuilder.emptyBriefcase().build())
                .build();

        // when
        final Optional<Tuple3<Asset, Integer, Double>> actual = subject.chooseAssetToSell(investor);

        // then
        assertThat(actual).isEmpty();
    }

    @Test
    public void should_get_available_assets() {

        // given
        final StockExchange stockExchange = subject.addStockExchange();
        final Company company = subject.addCompany(stockExchange);

        final CommodityExchange commodityExchange = subject.addCommodityExchange();
        final Optional<Commodity> optionalCommodity = subject.addCommodity(commodityExchange);

        assertThat(optionalCommodity).isPresent();
        final Commodity commodity = optionalCommodity.get();

        final CurrencyExchange currencyExchange = subject.addCurrencyExchange();
        final Optional<Currency> optionalCurrency = subject.addCurrency(currencyExchange);

        assertThat(optionalCurrency).isPresent();
        final Currency currency = optionalCurrency.get();

        final List<Asset> expected = new ArrayList<>(Arrays.asList(company, commodity, currency));

        // when
        final List<Asset> actual = subject.getAvailableAssets();

        // then
        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }
}
