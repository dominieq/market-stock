package org.example.marketstock.models.company;

import org.example.marketstock.models.company.builder.CompanyBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CompanyTest {

    private Company subject;

    @BeforeEach
    public void setUp() {
        subject = CompanyBuilder.builder()
                .withName("TestCompany")
                .withNumberOfAssets(100)
                .withProfit(100D)
                .withRevenue(100D)
                .withTurnover(100D)
                .withVolume(100)
                .build();
    }

    @Test
    public void should_update_turnover_and_volume() {

        // given
        final double INITIAL_TURNOVER = subject.getTurnover();
        final int INITIAL_VOLUME = subject.getVolume();

        // when
        subject.updateTurnoverAndVolume(100D, 100);

        // then
        assertThat(subject.getTurnover()).isEqualTo(INITIAL_TURNOVER + 100D);
        assertThat(subject.getVolume()).isEqualTo(INITIAL_VOLUME + 100);
    }

    @Test
    public void should_update_revenue_and_profit() {

        // given
        final double INITIAL_PROFIT = subject.getProfit();
        final double INITIAL_REVENUE = subject.getRevenue();

        // when
        subject.updateRevenueAndProfit();

        // then
        assertThat(subject.getRevenue()).isBetween(INITIAL_REVENUE * 0.8, INITIAL_REVENUE * 1.2);
        assertThat(subject.getProfit()).isBetween(INITIAL_PROFIT * 0.8, INITIAL_PROFIT * 1.2);
    }

    @Test
    public void should_issue_assets() {

        // given
        final int INITIAL_NUMBER_OF_ASSETS = subject.getNumberOfAssets();

        // when
        subject.issueAssets();

        // then
        assertThat(subject.getNumberOfAssets()).isBetween(INITIAL_NUMBER_OF_ASSETS + 100, INITIAL_NUMBER_OF_ASSETS + 100_000);
    }
}
