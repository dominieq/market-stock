package org.example.marketstock.models.entity.builder;

import org.example.marketstock.models.briefcase.Briefcase;
import org.example.marketstock.models.briefcase.builder.BriefcaseBuilder;
import org.example.marketstock.models.entity.InvestmentFund;
import org.example.marketstock.simulation.Simulation;
import org.example.marketstock.simulation.builder.SimulationBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class InvestmentFundBuilderTest {

    private InvestmentFundBuilder subject;

    @BeforeEach
    public void setUp() {
        subject = InvestmentFundBuilder.builder();
    }

    @Test
    public void should_build_valid_investment_fund() {

        // given
        final Briefcase briefcase = BriefcaseBuilder.builder().build();
        final Simulation simulation = SimulationBuilder.builder().build();

        final InvestmentFund actual = subject
                .withName("TestInvestmentFund")
                .withCurrentRate(34D)
                .withMinRate(12D)
                .withMaxRate(56D)
                .withRateChanges(Arrays.asList(56D, 12D, 34D))
                .withMargin(0.05)
                .withNumberOfAssets(100)
                .withBudget(1000D)
                .withBriefcase(briefcase)
                .withSimulation(simulation)
                .build();

        assertThat(actual)
                .hasFieldOrPropertyWithValue("name", "TestInvestmentFund")
                .hasFieldOrPropertyWithValue("currentRate", 34D)
                .hasFieldOrPropertyWithValue("minRate", 12D)
                .hasFieldOrPropertyWithValue("maxRate", 56D)
                .hasFieldOrPropertyWithValue("rateChanges", Arrays.asList(56D, 12D, 34D))
                .hasFieldOrPropertyWithValue("margin", 0.05)
                .hasFieldOrPropertyWithValue("numberOfAssets", 100)
                .hasFieldOrPropertyWithValue("budget", 1000D)
                .hasFieldOrPropertyWithValue("briefcase", briefcase)
                .hasFieldOrPropertyWithValue("simulation", simulation);
    }
}
