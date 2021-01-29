package org.example.marketstock.models.entity.builder;

import org.example.marketstock.models.briefcase.Briefcase;
import org.example.marketstock.models.briefcase.builder.BriefcaseBuilder;
import org.example.marketstock.models.entity.Investor;
import org.example.marketstock.simulation.Simulation;
import org.example.marketstock.simulation.builder.SimulationBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class InvestorBuilderTest {

    private InvestorBuilder subject;

    @BeforeEach
    public void setUp() {
        subject = InvestorBuilder.builder();
    }

    @Test
    public void should_build_valid_investor() {

        // given
        final Briefcase briefcase = BriefcaseBuilder.builder().build();
        final Simulation simulation = SimulationBuilder.builder().build();

        final Investor actual = subject
                .withFirstName("TestFirstName")
                .withLastName("TestLastName")
                .withBudget(1000D)
                .withBriefcase(briefcase)
                .withPESEL("TestPESEL")
                .withSimulation(simulation)
                .build();

        // then
        assertThat(actual)
                .hasFieldOrPropertyWithValue("firstName", "TestFirstName")
                .hasFieldOrPropertyWithValue("lastName", "TestLastName")
                .hasFieldOrPropertyWithValue("budget", 1000D)
                .hasFieldOrPropertyWithValue("briefcase", briefcase)
                .hasFieldOrPropertyWithValue("PESEL", "TestPESEL")
                .hasFieldOrPropertyWithValue("simulation", simulation);
    }
}
