package org.example.marketstock.models.asset;

import org.example.marketstock.models.asset.builder.CommodityBuilder;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.atIndex;

public class AbstractAssetIntegrationTest {

    @Test
    public void should_update_rate_of_single_asset_in_multiple_threads()
            throws InterruptedException {

        // given
        final List<Double> rateChanges = new ArrayList<>();
        rateChanges.add(0.5);

        final AbstractAsset subject = CommodityBuilder.builder()
                .withName("TestCommodity")
                .withCurrentRate(0.5)
                .withMinRate(0.5)
                .withMaxRate(0.5)
                .withRateChanges(new ArrayList<>(rateChanges))
                .build();

        final ExecutorService service = Executors.newFixedThreadPool(5);
        final CountDownLatch countDownLatch = new CountDownLatch(5);

        // when
        getValues().forEach(value -> service.submit(() -> {
            final double currentRate = subject.updateRate(value);
            countDownLatch.countDown();
            assertThat(currentRate).isEqualTo(value);
        }));
        countDownLatch.await();

        // then
        rateChanges.addAll(getValues());
        assertThat(subject.getRateChanges())
                .contains(0.5D, atIndex(0))
                .containsExactlyInAnyOrderElementsOf(rateChanges);
    }

    private List<Double> getValues() {
        final List<Double> values = new ArrayList<>();

        for (double i = 1D; i <= 5D; i++) {
            values.add(i);
        }

        return values;
    }
}
