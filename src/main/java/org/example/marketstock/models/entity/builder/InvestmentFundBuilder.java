package org.example.marketstock.models.entity.builder;

import org.example.marketstock.models.briefcase.Briefcase;
import org.example.marketstock.models.entity.InvestmentFund;
import org.example.marketstock.simulation.Simulation;

import java.util.List;

public final class InvestmentFundBuilder {

    private String name;
    private double currentRate;
    private double minRate;
    private double maxRate;
    private List<Double> rateChanges;
    private double margin;
    private int numberOfAssets;
    private double budget;
    private Briefcase briefcase;
    private Simulation simulation;

    private InvestmentFundBuilder() { }

    public static InvestmentFundBuilder builder() {
        return new InvestmentFundBuilder();
    }

    public InvestmentFundBuilder withName(final String name) {
        this.name = name;
        return this;
    }

    public InvestmentFundBuilder withCurrentRate(final double currentRate) {
        this.currentRate = currentRate;
        return this;
    }

    public InvestmentFundBuilder withMinRate(final double minRate) {
        this.minRate = minRate;
        return this;
    }

    public InvestmentFundBuilder withMaxRate(final double maxRate) {
        this.maxRate = maxRate;
        return this;
    }

    public InvestmentFundBuilder withRateChanges(final List<Double> rateChanges) {
        this.rateChanges = rateChanges;
        return this;
    }

    public InvestmentFundBuilder withMargin(final double margin) {
        this.margin = margin;
        return this;
    }

    public InvestmentFundBuilder withNumberOfAssets(final int numberOfAssets) {
        this.numberOfAssets = numberOfAssets;
        return this;
    }

    public InvestmentFundBuilder withBudget(final double budget) {
        this.budget = budget;
        return this;
    }

    public InvestmentFundBuilder withBriefcase(final Briefcase briefcase) {
        this.briefcase = briefcase;
        return this;
    }

    public InvestmentFundBuilder withSimulation(final Simulation simulation) {
        this.simulation = simulation;
        return this;
    }

    public InvestmentFund build() {
        return new InvestmentFund(
                name, currentRate, minRate, maxRate, rateChanges, margin,
                numberOfAssets,
                budget, briefcase,
                simulation
        );
    }
}
