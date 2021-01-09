package org.example.marketstock.models.asset.builder;

import org.example.marketstock.models.asset.Commodity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class CommodityBuilderTest {

    private CommodityBuilder subject;

    @BeforeEach
    public void setUp() {
        subject = CommodityBuilder.builder();
    }

    @Test
    public void should_build_valid_commodity() {

        // when
        final Commodity actual = subject
                .withName("commodity")
                .withCurrentRate(34D)
                .withMinRate(12D)
                .withMaxRate(56D)
                .withRateChanges(Arrays.asList(12D, 34D, 56D))
                .withMargin(0.05D)
                .withUnitOfTrading("unitOfTrading")
                .withCurrency("currency")
                .build();

        // then
        assertThat(actual)
                .hasFieldOrPropertyWithValue("name", "commodity")
                .hasFieldOrPropertyWithValue("currentRate", 34D)
                .hasFieldOrPropertyWithValue("minRate", 12D)
                .hasFieldOrPropertyWithValue("maxRate", 56D)
                .hasFieldOrPropertyWithValue("rateChanges", Arrays.asList(12D, 34D, 56D))
                .hasFieldOrPropertyWithValue("margin", 0.05D)
                .hasFieldOrPropertyWithValue("unitOfTrading", "unitOfTrading")
                .hasFieldOrPropertyWithValue("currency", "currency");
    }
}
