package org.example.marketstock.models.company.builder;

import org.example.marketstock.models.company.Company;

import java.util.List;

/**
 * A builder for the {@link Company} class.
 *
 * @author Dominik Szmyt
 * @see Company
 * @since 1.0.0
 */
public final class CompanyBuilder {

    private String name;
    private double currentRate;
    private double maxRate;
    private double minRate;
    private List<Double> rateChanges;
    private double margin;
    private int numberOfAssets;
    private String dateOfFirstValuation;
    private double openingQuotation;
    private double profit;
    private double revenue;
    private double equityCapital;
    private double openingCapital;
    private int volume;
    private double turnover;

    private CompanyBuilder() { }

    public static CompanyBuilder builder() {
        return new CompanyBuilder();
    }

    public CompanyBuilder withName(final String name) {
        this.name = name;
        return this;
    }

    public CompanyBuilder withCurrentRate(final double currentRate) {
        this.currentRate = currentRate;
        return this;
    }

    public CompanyBuilder withMaxRate(final double maxRate) {
        this.maxRate = maxRate;
        return this;
    }

    public CompanyBuilder withMinRate(final double minRate) {
        this.minRate = minRate;
        return this;
    }

    public CompanyBuilder withRateChanges(final List<Double> rateChanges) {
        this.rateChanges = rateChanges;
        return this;
    }

    public CompanyBuilder withMargin(final double stockMargin) {
        this.margin = stockMargin;
        return this;
    }

    public CompanyBuilder withNumberOfAssets(final int numberOfAssets) {
        this.numberOfAssets = numberOfAssets;
        return this;
    }

    public CompanyBuilder withDateOfFirstValuation(final String dateOfFirstValuation) {
        this.dateOfFirstValuation = dateOfFirstValuation;
        return this;
    }

    public CompanyBuilder withOpeningQuotation(final double openingQuotation) {
        this.openingQuotation = openingQuotation;
        return this;
    }

    public CompanyBuilder withProfit(final double profit) {
        this.profit = profit;
        return this;
    }

    public CompanyBuilder withRevenue(final double revenue) {
        this.revenue = revenue;
        return this;
    }

    public CompanyBuilder withEquityCapital(final double equityCapital) {
        this.equityCapital = equityCapital;
        return this;
    }

    public CompanyBuilder withOpeningCapital(final double openingCapital) {
        this.openingCapital = openingCapital;
        return this;
    }

    public CompanyBuilder withVolume(final int volume) {
        this.volume = volume;
        return this;
    }

    public CompanyBuilder withTurnover(final double turnover) {
        this.turnover = turnover;
        return this;
    }

    public Company build() {
        return new Company(
                name, currentRate, minRate, maxRate, rateChanges, margin,
                numberOfAssets,
                dateOfFirstValuation, openingQuotation,
                profit, revenue,
                equityCapital, openingCapital,
                volume, turnover

        );
    }
}
