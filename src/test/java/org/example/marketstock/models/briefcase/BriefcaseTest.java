package org.example.marketstock.models.briefcase;

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

import java.util.*;

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
