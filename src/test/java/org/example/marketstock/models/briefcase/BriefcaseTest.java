package org.example.marketstock.models.briefcase;

import io.vavr.Tuple2;
import org.example.marketstock.models.asset.Asset;
import org.example.marketstock.models.asset.Commodity;
import org.example.marketstock.models.asset.Currency;
import org.example.marketstock.models.asset.builder.CommodityBuilder;
import org.example.marketstock.models.asset.builder.CurrencyBuilder;
import org.example.marketstock.models.briefcase.builder.BriefcaseBuilder;
import org.example.marketstock.models.company.Company;
import org.example.marketstock.models.company.builder.CompanyBuilder;
import org.example.marketstock.models.entity.InvestmentFund;
import org.example.marketstock.models.entity.builder.InvestmentFundBuilder;
import org.example.marketstock.simulation.builder.SimulationBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class BriefcaseTest {

    private Briefcase subject;

    @BeforeEach
    public void setUp() {
        subject = BriefcaseBuilder.builder()
                .withMap(new LinkedHashMap<>())
                .build();
    }

    @Test
    public void should_add_new_asset() {

        // given
        final Commodity commodity = getTestCommodityBuilder(1).build();
        final Currency currency = getTestCurrencyBuilder(1).build();
        final Company company = getTestCompanyBuilder(1).build();
        final InvestmentFund investmentFund = getTestInvestmentFundBuilder(1).build();

        // when
        subject.addOrIncrease(commodity, 1);
        subject.addOrIncrease(currency, 2);
        subject.addOrIncrease(company, 3);
        subject.addOrIncrease(investmentFund, 4);

        // then
        final Map<Asset, Integer> map = new HashMap<>();
        map.put(commodity, 1);
        map.put(currency, 2);
        map.put(company, 3);
        map.put(investmentFund, 4);

        assertThat(subject.getMap()).containsAllEntriesOf(map);
    }

    @Test
    public void should_increase_number_of_assets() {

        // given
        final Commodity commodity = getTestCommodityBuilder(1).build();
        final Currency currency = getTestCurrencyBuilder(1).build();
        final Company company = getTestCompanyBuilder(1).build();
        final InvestmentFund investmentFund = getTestInvestmentFundBuilder(1).build();

        // when
        subject.addOrIncrease(commodity, 1);
        subject.addOrIncrease(currency, 2);
        subject.addOrIncrease(company, 3);
        subject.addOrIncrease(investmentFund, 4);

        subject.addOrIncrease(commodity, 2);
        subject.addOrIncrease(currency, 3);
        subject.addOrIncrease(company, 4);
        subject.addOrIncrease(investmentFund, 5);

        // then
        final Map<Asset, Integer> map = new HashMap<>();
        map.put(commodity, 3);
        map.put(currency, 5);
        map.put(company, 7);
        map.put(investmentFund, 9);

        assertThat(subject.getMap()).containsAllEntriesOf(map);
    }

    @Test
    public void should_decrease_number_of_assets() {

        // given
        final Commodity commodity = getTestCommodityBuilder(1).build();
        final Currency currency = getTestCurrencyBuilder(1).build();
        final Company company = getTestCompanyBuilder(1).build();
        final InvestmentFund investmentFund = getTestInvestmentFundBuilder(1).build();

        // when
        subject.addOrIncrease(commodity, 3);
        subject.addOrIncrease(currency, 5);
        subject.addOrIncrease(company, 7);
        subject.addOrIncrease(investmentFund, 9);

        subject.decreaseOrRemove(commodity, 2);
        subject.decreaseOrRemove(currency, 3);
        subject.decreaseOrRemove(company, 4);
        subject.decreaseOrRemove(investmentFund, 5);

        // then
        final Map<Asset, Integer> map = new HashMap<>();
        map.put(commodity, 1);
        map.put(currency, 2);
        map.put(company, 3);
        map.put(investmentFund, 4);

        assertThat(subject.getMap()).containsAllEntriesOf(map);
    }

    @Test
    public void should_remove_asset_after_decreasing_number() {

        // given
        final Commodity commodity = getTestCommodityBuilder(1).build();
        final Currency currency = getTestCurrencyBuilder(1).build();
        final Company company = getTestCompanyBuilder(1).build();
        final InvestmentFund investmentFund = getTestInvestmentFundBuilder(1).build();

        // when
        subject.addOrIncrease(commodity, 1);
        subject.addOrIncrease(currency, 2);
        subject.addOrIncrease(company, 3);
        subject.addOrIncrease(investmentFund, 4);

        subject.decreaseOrRemove(commodity, 1);
        subject.decreaseOrRemove(currency, 2);
        subject.decreaseOrRemove(company, 3);
        subject.decreaseOrRemove(investmentFund, 4);

        // then
        assertThat(subject.getMap()).isEmpty();
    }

    @ParameterizedTest
    @CsvSource({
            "1, 1, 100, 100, true",
            "2, 1, 100, 100, false",
            "1, 1, 200, 100, false",
            "2, 1, 200, 100, false"
    })
    public void should_correctly_asses_that_briefcase_contains_asset(final int actualIndex,
                                                                     final int expectedIndex,
                                                                     final int actualNumber,
                                                                     final int expectedNumber,
                                                                     final boolean expectedResult) {

        // given
        final Map<Asset, Integer> map = new LinkedHashMap<>();
        map.put(getTestCommodityBuilder(actualIndex).build(), actualNumber);

        subject = BriefcaseBuilder.builder().withMap(map).build();

        // when
        final boolean actualResult = subject.contains(getTestCommodityBuilder(expectedIndex).build(), expectedNumber);

        // then
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    public void should_create_valid_stream() {

        // given
        final Map<Asset, Integer> map = new LinkedHashMap<>();
        map.put(getTestCommodityBuilder(1).build(), 100);

        subject = BriefcaseBuilder.builder().withMap(map).build();

        // when
        final List<Tuple2<Asset, Integer>> actual = subject.stream()
                .collect(Collectors.toList());

        // then
        final List<Tuple2<Asset, Integer>> expected = map.entrySet().stream()
                .map(entry -> new Tuple2<>(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());

        assertThat(actual).hasSameElementsAs(expected);
    }

    @Test
    public void should_return_list_of_assets() {

        // given
        final Map<Asset, Integer> map = new LinkedHashMap<>();
        map.put(getTestCommodityBuilder(1).build(), 100);

        subject = BriefcaseBuilder.builder().withMap(map).build();

        // when
        final List<Asset> actual = subject.getAssets();

        // then
        final List<Asset> expected = new ArrayList<>(map.keySet());

        assertThat(actual).hasSameElementsAs(expected);
    }

    @Test
    public void should_return_list_of_numbers() {

        // given
        final Map<Asset, Integer> map = new LinkedHashMap<>();
        map.put(getTestCommodityBuilder(1).build(), 100);

        subject = BriefcaseBuilder.builder().withMap(map).build();

        // when
        final List<Integer> actual = subject.getNumbers();

        // then
        final List<Integer> expected = new ArrayList<>(map.values());

        assertThat(actual).hasSameElementsAs(expected);
    }

    @Test
    public void should_get_count_of_asset() {

        // given
        final Asset asset = getTestCommodityBuilder(1).build();

        final Map<Asset, Integer> map = new LinkedHashMap<>();
        map.put(asset, 100);

        subject = BriefcaseBuilder.builder().withMap(map).build();

        // when
        final int count = subject.getCount(asset);

        // then
        assertThat(count).isEqualTo(map.get(asset));
    }

    @Test
    public void should_get_zero_when_asset_not_present() {

        // given
        final Asset asset = getTestCommodityBuilder(1).build();

        // when
        final int count = subject.getCount(asset);

        // then
        assertThat(count).isZero();
    }

    @Test
    public void should_correctly_asses_that_briefcase_is_empty() {

        // given
        final Map<Asset, Integer> map = new LinkedHashMap<>();
        subject = BriefcaseBuilder.builder().withMap(map).build();

        // when
        final boolean actual = subject.isEmpty();

        // then
        assertThat(actual).isEqualTo(map.isEmpty());
    }

    @Test
    public void should_remove_asset_entirely() {

        // given
        final Asset asset = getTestCommodityBuilder(1).build();
        final int numberOfAsset = 100;

        final Map<Asset, Integer> map = new LinkedHashMap<>();
        map.put(asset, numberOfAsset);

        subject = BriefcaseBuilder.builder().withMap(map).build();

        // when
        final int actual = subject.removeEntirely(asset);

        // then
        assertThat(actual).isEqualTo(numberOfAsset);
        assertThat(subject.contains(asset)).isFalse();
    }

    private CommodityBuilder getTestCommodityBuilder(int index) {
        return CommodityBuilder.builder()
                .withName("TestCommodity" + index)
                .withRateChanges(new ArrayList<>(Collections.singletonList(0.0)))
                .withMargin(0.05)
                .withUnitOfTrading("UnitOfTrading" + index)
                .withCurrency("TestCurrencyForCommodity");
    }

    private CurrencyBuilder getTestCurrencyBuilder(int index) {
        return CurrencyBuilder.builder()
                .withName("TestCurrency" + index)
                .withRateChanges(new ArrayList<>(Collections.singletonList(0.0)))
                .withMargin(0.05)
                .withCountries(new ArrayList<>())
                .withComparisonCurrency(CurrencyBuilder.builder().build());
    }

    private CompanyBuilder getTestCompanyBuilder(int index) {
        return CompanyBuilder.builder()
                .withName("TestCompany" + index)
                .withRateChanges(new ArrayList<>(Collections.singletonList(0.0)))
                .withMargin(0.05)
                .withNumberOfAssets(100)
                .withDateOfFirstValuation("2020-11-25");
    }

    private InvestmentFundBuilder getTestInvestmentFundBuilder(int index) {
        return InvestmentFundBuilder.builder()
                .withName("TestInvestmentFund" + index)
                .withRateChanges(new ArrayList<>(Collections.singletonList(0.0)))
                .withMargin(0.05)
                .withNumberOfAssets(100)
                .withBudget(1000)
                .withBriefcase(BriefcaseBuilder.builder().build())
                .withSimulation(SimulationBuilder.builder().build());
    }
}
