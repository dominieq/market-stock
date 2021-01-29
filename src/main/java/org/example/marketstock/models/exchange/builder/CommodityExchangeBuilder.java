package org.example.marketstock.models.exchange.builder;

import org.example.marketstock.models.asset.Commodity;
import org.example.marketstock.models.exchange.CommodityExchange;

import java.util.List;

/**
 * A builder for the {@link CommodityExchange} class.
 *
 * @author Dominik Szmyt
 * @see CommodityExchange
 * @since 1.0.0
 */
public final class CommodityExchangeBuilder {

    private List<Commodity> commodities;
    private String name;
    private String country;
    private String city;
    private String address;
    private String currency;
    private double margin;

    private CommodityExchangeBuilder() { }

    public static CommodityExchangeBuilder builder() {
        return new CommodityExchangeBuilder();
    }

    public CommodityExchangeBuilder withCommodities(final List<Commodity> commodities) {
        this.commodities = commodities;
        return this;
    }

    public CommodityExchangeBuilder withName(final String name) {
        this.name = name;
        return this;
    }

    public CommodityExchangeBuilder withCountry(final String country) {
        this.country = country;
        return this;
    }

    public CommodityExchangeBuilder withCity(final String city) {
        this.city = city;
        return this;
    }

    public CommodityExchangeBuilder withAddress(final String address) {
        this.address = address;
        return this;
    }

    public CommodityExchangeBuilder withCurrency(final String currency) {
        this.currency = currency;
        return this;
    }

    public CommodityExchangeBuilder withMargin(final double margin) {
        this.margin = margin;
        return this;
    }

    public CommodityExchange build() {
        return new CommodityExchange(name, country, city, address, currency, margin, commodities);
    }
}
