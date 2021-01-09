package org.example.marketstock.models.exchange.builder;

import org.example.marketstock.models.company.Company;
import org.example.marketstock.models.exchange.StockExchange;
import org.example.marketstock.models.index.Index;

import java.util.List;

public final class StockExchangeBuilder {

    private List<Index> indices;
    private List<Company> companies;
    private String name;
    private String country;
    private String city;
    private String address;
    private String currency;
    private double margin;

    private StockExchangeBuilder() { }

    public static StockExchangeBuilder builder() {
        return new StockExchangeBuilder();
    }

    public StockExchangeBuilder withIndices(final List<Index> indices) {
        this.indices = indices;
        return this;
    }

    public StockExchangeBuilder withCompanies(final List<Company> companies) {
        this.companies = companies;
        return this;
    }

    public StockExchangeBuilder withName(final String name) {
        this.name = name;
        return this;
    }

    public StockExchangeBuilder withCountry(final String country) {
        this.country = country;
        return this;
    }

    public StockExchangeBuilder withCity(final String city) {
        this.city = city;
        return this;
    }

    public StockExchangeBuilder withAddress(final String address) {
        this.address = address;
        return this;
    }

    public StockExchangeBuilder withCurrency(final String currency) {
        this.currency = currency;
        return this;
    }

    public StockExchangeBuilder withMargin(final double margin) {
        this.margin = margin;
        return this;
    }

    public StockExchange build() {
        return new StockExchange(name, country, city, address, currency, margin, indices, companies);
    }
}
