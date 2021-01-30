package org.example.marketstock.models.company;

import org.example.marketstock.models.company.builder.CompanyBuilder;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

public class CompanyIntegrationTest {

    @Test
    public void should_update_turnover_and_volume_in_multiple_threads()
            throws InterruptedException {

        final Company subject = CompanyBuilder.builder()
                .withName("TestCompany")
                .withTurnover(100D)
                .withVolume(100)
                .build();

        final ExecutorService service = Executors.newFixedThreadPool(5);
        final CountDownLatch countDownLatch = new CountDownLatch(5);

        for (int i = 0; i < 5; i++) {
            service.submit(() -> {
                subject.updateTurnoverAndVolume(100D, 100);
                countDownLatch.countDown();
            });
        }

        countDownLatch.await();

        assertThat(subject.getTurnover()).isEqualTo(600D);
        assertThat(subject.getVolume()).isEqualTo(600);
    }
}
