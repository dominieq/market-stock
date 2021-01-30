package org.example.marketstock.models.asset;

import org.example.marketstock.models.asset.builder.CommodityBuilder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.atIndex;

public class AbstractAssetTest {

    private AbstractAsset subject;

    @ParameterizedTest
    @ValueSource(doubles = { 0.8, 1.0, 1.2, 1.4, 1.6 })
    public void should_add_rate_and_change_max(double rate) {

        // given
        subject = CommodityBuilder.builder()
                .withName("TestCommodity")
                .withCurrentRate(0.4D)
                .withMinRate(0.2D)
                .withMaxRate(0.6D)
                .withRateChanges(new ArrayList<>(Arrays.asList(0.6D, 0.2D, 0.4D)))
                .build();

        // when
        subject.updateRate(rate);

        // then
        assertThat(subject.getCurrentRate()).isEqualTo(rate);
        assertThat(subject.getMinRate()).isEqualTo(0.2D);
        assertThat(subject.getMaxRate()).isEqualTo(rate);
        assertThat(subject.getRateChanges()).containsExactly(0.6D, 0.2D, 0.4D, rate);
    }

    @ParameterizedTest
    @ValueSource(doubles = { 0.2, 0.4, 0.6, 0.8, 1.0 })
    public void should_add_rate_and_change_min(double rate) {

        // given
        subject = CommodityBuilder.builder()
                .withName("TestCommodity")
                .withCurrentRate(1.4)
                .withMinRate(1.2)
                .withMaxRate(1.6)
                .withRateChanges(new ArrayList<>(Arrays.asList(1.6D, 1.2D, 1.4D)))
                .build();

        // when
        subject.updateRate(rate);

        // then
        assertThat(subject.getCurrentRate()).isEqualTo(rate);
        assertThat(subject.getMinRate()).isEqualTo(rate);
        assertThat(subject.getMaxRate()).isEqualTo(1.6D);
        assertThat(subject.getRateChanges()).containsExactly(1.6D, 1.2D, 1.4D, rate);
    }

    @ParameterizedTest
    @ValueSource(doubles = { 1.2, 0.2, 2.2, 1.8, 0.6})
    public void should_add_rate_and_remove_first_rate(double rate) {

        // given
        final List<Double> rateChanges = new ArrayList<>();
        for (double i = 0.2D; i <= 2D; i += 0.2D) {
            rateChanges.add(i);
        }

        assertThat(rateChanges).hasSize(10);

        subject = CommodityBuilder.builder()
                .withName("TestCommodity")
                .withCurrentRate(rateChanges.get(9))
                .withMinRate(Collections.min(rateChanges))
                .withMaxRate(Collections.max(rateChanges))
                .withRateChanges(new ArrayList<>(rateChanges))
                .build();

        // when
        subject.updateRate(rate);

        // then
        assertThat(subject.getCurrentRate()).isEqualTo(rate);
        assertThat(subject.getRateChanges())
                .hasSameSizeAs(rateChanges)
                .doesNotContain(rateChanges.get(0), atIndex(0))
                .contains(rate, atIndex(9));
    }
}
