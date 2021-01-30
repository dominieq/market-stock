package org.example.marketstock.models.asset;

import org.example.marketstock.models.company.builder.CompanyBuilder;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

public class CountableAssetIntegrationTest {

    @Test
    public void should_increase_number_of_assets_in_multiple_threads()
            throws InterruptedException {

        // given
        final CountableAsset subject = CompanyBuilder.builder()
                .withName("TestCompany")
                .withNumberOfAssets(100)
                .build();

        final ExecutorService service = Executors.newFixedThreadPool(5);
        final CountDownLatch countDownLatch = new CountDownLatch(5);

        // when
        for (int i = 0; i < 5; i++ ) {
            service.submit(() -> {
                subject.increaseNumberOfAssets(100);
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();

        // then
        assertThat(subject.getNumberOfAssets()).isEqualTo(600);
    }


    @Test
    public void should_decrease_number_of_assets_in_multiple_threads()
            throws InterruptedException {

        // given
        final CountableAsset subject = CompanyBuilder.builder()
                .withName("TestCompany")
                .withNumberOfAssets(600)
                .build();

        final ExecutorService service = Executors.newFixedThreadPool(5);
        final CountDownLatch countDownLatch = new CountDownLatch(5);

        // when
        for (int i = 0; i < 5; i++) {
            service.submit(() -> {
                subject.decreaseNumberOfAssets(100);
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();

        // then
        assertThat(subject.getNumberOfAssets()).isEqualTo(100);
    }

    @Test
    public void should_decrease_number_of_assets_only_once()
            throws InterruptedException {

        // given
        final CountableAsset subject = CompanyBuilder.builder()
                .withName("TestCompany")
                .withNumberOfAssets(100)
                .build();

        final ExecutorService service = Executors.newFixedThreadPool(5);
        final CountDownLatch countDownLatch = new CountDownLatch(5);

        // when
        for (int i = 0; i < 5; i++) {
            service.submit(() -> {
                subject.decreaseNumberOfAssets(100);
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();

        // then
        assertThat(subject.getNumberOfAssets()).isEqualTo(0);
    }
}
