package org.example.marketstock.models.entity;

import org.example.marketstock.models.asset.Asset;
import org.example.marketstock.models.asset.builder.CurrencyBuilder;
import org.example.marketstock.models.briefcase.builder.BriefcaseBuilder;
import org.example.marketstock.models.entity.builder.InvestorBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

public class AbstractEntityTest {

    private AbstractEntity subject;

    @BeforeEach
    public void setUp() {
        subject = InvestorBuilder.builder()
                .withBudget(1000D)
                .withBriefcase(BriefcaseBuilder.builder()
                        .withMap(new HashMap<>())
                        .build()
                ).build();
    }

    @ParameterizedTest
    @ValueSource(doubles = { 500D, 1000D })
    public void should_add_asset_with_investor_implementation(final double price) {

        // given
        final double initialPrice = subject.getBudget();
        final Asset asset = CurrencyBuilder.builder().withName("TestCurrency").build();
        final int numberOfAsset = 100;

        // when
        subject.addAsset(price, asset, numberOfAsset);

        // then
        final Map<Asset, Integer> map = new HashMap<>();
        map.put(asset, numberOfAsset);

        assertThat(subject.getBriefcase().getMap()).containsAllEntriesOf(map);
        assertThat(subject.getBudget()).isEqualTo(initialPrice - price);
    }

    @ParameterizedTest
    @ValueSource(doubles = { 1001D, 2000D })
    public void should_not_add_asset_with_investor_implementation(final double price) {

        // given
        final double initialPrice = subject.getBudget();
        final Asset asset = CurrencyBuilder.builder().withName("TestCurrency").build();
        final int numberOfAsset = 100;

        // when
        subject.addAsset(price, asset, numberOfAsset);

        // then
        assertThat(subject.getBriefcase().getMap()).isEmpty();
        assertThat(subject.getBudget()).isEqualTo(initialPrice);
    }

    @ParameterizedTest
    @CsvSource({
            "1000, 1000, 500",
            "2000, 1000, 999",
    })
    public void should_subtract_asset_with_investor_implementation(final double price,
                                                                   final int initialNumberOfAsset,
                                                                   final int numberOfAsset) {

        // given
        final double initialPrice = subject.getBudget();
        final Asset asset = CurrencyBuilder.builder().withName("TestCurrency").build();

        subject.getBriefcase().addOrIncrease(asset, initialNumberOfAsset);

        // when
        subject.subtractAsset(price, asset, numberOfAsset);

        // then
        final Map<Asset, Integer> map = new HashMap<>();
        map.put(asset, initialNumberOfAsset - numberOfAsset);

        assertThat(subject.getBriefcase().getMap()).containsAllEntriesOf(map);
        assertThat(subject.getBudget()).isEqualTo(initialPrice + price);
    }

    @ParameterizedTest
    @CsvSource({
            "1000, 1000, 1001",
            "2000, 1000, 2000"
    })
    public void should_not_subtract_asset_with_investor_implementation(final double price,
                                                                       final int initialNumberOfAsset,
                                                                       final int numberOfAsset) {

        // given
        final double initialPrice = subject.getBudget();
        final Asset asset = CurrencyBuilder.builder().withName("TestCurrency").build();

        subject.getBriefcase().addOrIncrease(asset, initialNumberOfAsset);

        // when
        subject.subtractAsset(price, asset, numberOfAsset);

        // then
        final Map<Asset, Integer> map = new HashMap<>();
        map.put(asset, initialNumberOfAsset);

        assertThat(subject.getBriefcase().getMap()).containsAllEntriesOf(map);
        assertThat(subject.getBudget()).isEqualTo(initialPrice);
    }

    @Test
    public void should_increase_budget() {

        // given
        final double initialBudget = subject.getBudget();

        // when
        subject.increaseBudget();

        // then
        assertThat(subject.getBudget()).isBetween(initialBudget + AbstractEntity.BUDGET_UPDATE_LOWER_BOUNDARY,
                initialBudget + AbstractEntity.BUDGET_UPDATE_UPPER_BOUNDARY);
    }
}
