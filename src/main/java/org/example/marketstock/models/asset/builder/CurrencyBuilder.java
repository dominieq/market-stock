package org.example.marketstock.models.asset.builder;

import org.example.marketstock.models.asset.Currency;

import java.util.List;

public final class CurrencyBuilder {

    private String name;
    private double currentRate;
    private double minRate;
    private double maxRate;
    private double margin;
    private List<Double> rateChanges;
    private Currency comparisonCurrency;
    private List<String> countries;

    private CurrencyBuilder() { }

    public static CurrencyBuilder builder() {
        return new CurrencyBuilder();
    }

    public CurrencyBuilder withName(final String name) {
        this.name = name;
        return this;
    }

    public CurrencyBuilder withCurrentRate(final double currentRate) {
        this.currentRate = currentRate;
        return this;
    }

    public CurrencyBuilder withMinRate(final double minRate) {
        this.minRate = minRate;
        return this;
    }

    public CurrencyBuilder withMaxRate(final double maxRate) {
        this.maxRate = maxRate;
        return this;
    }

    public CurrencyBuilder withMargin(final double exchangeMargin) {
        this.margin = exchangeMargin;
        return this;
    }

    public CurrencyBuilder withRateChanges(final List<Double> rateChangeArrayList) {
        this.rateChanges = rateChangeArrayList;
        return this;
    }

    public CurrencyBuilder withComparisonCurrency(final Currency comparisonCurrency) {
        this.comparisonCurrency = comparisonCurrency;
        return this;
    }

    public CurrencyBuilder withCountries(final List<String> countriesArrayList) {
        this.countries = countriesArrayList;
        return this;
    }

    public Currency build() {
        return new Currency(name, currentRate, minRate, maxRate, margin, rateChanges, comparisonCurrency, countries);
    }
}
