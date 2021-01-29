package org.example.marketstock.models.company.builder;

import org.example.marketstock.models.company.Company;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class CompanyBuilderTest {

    private CompanyBuilder subject;

    @BeforeEach
    public void setUp() {
        subject = CompanyBuilder.builder();
    }

    @Test
    public void should_build_valid_company() {

        // when
        final Company company = subject
                .withName("company")
                .withCurrentRate(34D)
                .withMinRate(12D)
                .withMaxRate(56D)
                .withRateChanges(Arrays.asList(12D, 56D, 34D))
                .withMargin(0.05D)
                .withNumberOfAssets(100)
                .withDateOfFirstValuation("2020-11-23")
                .withOpeningQuotation(12D)
                .withProfit(34D)
                .withRevenue(56D)
                .withEquityCapital(78D)
                .withOpeningCapital(90D)
                .withVolume(78)
                .withTurnover(56D)
                .build();

        // then
        assertThat(company)
                .hasFieldOrPropertyWithValue("name", "company")
                .hasFieldOrPropertyWithValue("currentRate", 34D)
                .hasFieldOrPropertyWithValue("minRate", 12D)
                .hasFieldOrPropertyWithValue("maxRate", 56D)
                .hasFieldOrPropertyWithValue("rateChanges", Arrays.asList(12D, 56D, 34D))
                .hasFieldOrPropertyWithValue("margin", 0.05)
                .hasFieldOrPropertyWithValue("dateOfFirstValuation", "2020-11-23")
                .hasFieldOrPropertyWithValue("openingQuotation", 12D)
                .hasFieldOrPropertyWithValue("profit", 34D)
                .hasFieldOrPropertyWithValue("revenue", 56D)
                .hasFieldOrPropertyWithValue("equityCapital", 78D)
                .hasFieldOrPropertyWithValue("openingCapital", 90D)
                .hasFieldOrPropertyWithValue("volume", 78)
                .hasFieldOrPropertyWithValue("turnover", 56D);
    }
}
