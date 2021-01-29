package org.example.marketstock.models.entity.builder;

import org.example.marketstock.models.briefcase.Briefcase;
import org.example.marketstock.models.entity.Investor;
import org.example.marketstock.simulation.Simulation;

import static java.util.Objects.isNull;

/**
 * A builder for the {@code Investor} class.
 *
 * @author Dominik Szmyt
 * @see Investor
 * @since 1.0.0
 */
public final class InvestorBuilder {

    private String firstName;
    private String lastName;
    private double budget;
    private Briefcase briefcase;
    private String PESEL;
    private Simulation simulation;

    private InvestorBuilder() { }

    public static InvestorBuilder builder() {
        return new InvestorBuilder();
    }

    public InvestorBuilder withFirstName(final String firstName) {
        this.firstName = firstName;
        return this;
    }

    public InvestorBuilder withLastName(final String lastName) {
        this.lastName = lastName;
        return this;
    }

    public InvestorBuilder withBudget(final double budget) {
        this.budget = budget;
        return this;
    }

    public InvestorBuilder withBriefcase(final Briefcase briefcase) {
        this.briefcase = briefcase;
        return this;
    }

    public InvestorBuilder withPESEL(final String PESEL) {
        this.PESEL = PESEL;
        return this;
    }

    public InvestorBuilder withSimulation(final Simulation simulation) {
        this.simulation = simulation;
        return this;
    }

    public Investor build() {
        return new Investor(firstName, lastName, budget, briefcase, PESEL, simulation);
    }
}
