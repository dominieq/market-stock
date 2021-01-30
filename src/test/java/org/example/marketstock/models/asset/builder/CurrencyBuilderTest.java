package org.example.marketstock.models.asset.builder;

import org.example.marketstock.models.asset.Currency;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class CurrencyBuilderTest {

    private CurrencyBuilder subject;

    @BeforeEach
    public void setUp() {
        subject = CurrencyBuilder.builder();
    }

    @Test
    public void should_build_valid_currency() {

        // given
        final Currency comparisonCurrency = getComparisonCurrency();

        // when
        final Currency actual = subject
                .withName("currency")
                .withCurrentRate(34D)
                .withMinRate(12D)
                .withMaxRate(56D)
                .withRateChanges(Arrays.asList(12D, 34D, 56D))
                .withMargin(0.05D)
                .withComparisonCurrency(comparisonCurrency)
                .withCountries(Arrays.asList("Poland", "Germany", "Lithuania"))
                .build();

        // then
        assertThat(actual)
                .hasFieldOrPropertyWithValue("name", "currency")
                .hasFieldOrPropertyWithValue("currentRate", 34D)
                .hasFieldOrPropertyWithValue("minRate", 12D)
                .hasFieldOrPropertyWithValue("maxRate", 56D)
                .hasFieldOrPropertyWithValue("rateChanges", Arrays.asList(12D, 34D, 56D))
                .hasFieldOrPropertyWithValue("margin", 0.05D)
                .hasFieldOrPropertyWithValue("comparisonCurrency", comparisonCurrency)
                .hasFieldOrPropertyWithValue("countries", Arrays.asList("Poland", "Germany", "Lithuania"));
    }


    private Currency getComparisonCurrency() {
        return CurrencyBuilder.builder().build();
    }
}
