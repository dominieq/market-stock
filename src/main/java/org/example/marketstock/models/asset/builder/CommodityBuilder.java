package org.example.marketstock.models.asset.builder;

import org.example.marketstock.models.asset.Commodity;

import java.util.List;

public final class CommodityBuilder {

    private String name;
    private double currentRate;
    private double minRate;
    private double maxRate;
    private double margin;
    private List<Double> rateChanges;
    private String unitOfTrading;
    private String currency;

    private CommodityBuilder() { }

    public static CommodityBuilder builder() {
        return new CommodityBuilder();
    }

    public CommodityBuilder withName(final String name) {
        this.name = name;
        return this;
    }

    public CommodityBuilder withCurrentRate(final double currentRate) {
        this.currentRate = currentRate;
        return this;
    }

    public CommodityBuilder withMinRate(final double minRate) {
        this.minRate = minRate;
        return this;
    }

    public CommodityBuilder withMaxRate(final double maxRate) {
        this.maxRate = maxRate;
        return this;
    }

    public CommodityBuilder withMargin(final double exchangeMargin) {
        this.margin = exchangeMargin;
        return this;
    }

    public CommodityBuilder withRateChanges(final List<Double> rateChangeArrayList) {
        this.rateChanges = rateChangeArrayList;
        return this;
    }

    public CommodityBuilder withUnitOfTrading(final String unitOfTrading) {
        this.unitOfTrading = unitOfTrading;
        return this;
    }

    public CommodityBuilder withCurrency(final String currency) {
        this.currency = currency;
        return this;
    }

    public Commodity build() {
        return new Commodity(name, currentRate, minRate, maxRate, margin, rateChanges, unitOfTrading, currency);
    }
}
