package org.example.marketstock.models.exchange.builder;

import org.example.marketstock.models.asset.Currency;
import org.example.marketstock.models.exchange.CurrencyExchange;

import java.util.List;

/**
 * A builder for the {@link CurrencyExchange} class.
 *
 * @author Dominik Szmyt
 * @see CurrencyExchange
 * @since 1.0.0
 */
public final class CurrencyExchangeBuilder {

    private String name;
    private String country;
    private String city;
    private String address;
    private String currency;
    private double margin;
    private List<Currency> currencies;

    private CurrencyExchangeBuilder() { }

    public static CurrencyExchangeBuilder builder() {
        return new CurrencyExchangeBuilder();
    }

    public CurrencyExchangeBuilder withName(final String name) {
        this.name = name;
        return this;
    }

    public CurrencyExchangeBuilder withCountry(final String country) {
        this.country = country;
        return this;
    }

    public CurrencyExchangeBuilder withCity(final String city) {
        this.city = city;
        return this;
    }

    public CurrencyExchangeBuilder withAddress(final String address) {
        this.address = address;
        return this;
    }

    public CurrencyExchangeBuilder withCurrency(final String currency) {
        this.currency = currency;
        return this;
    }

    public CurrencyExchangeBuilder withMargin(final double margin) {
        this.margin = margin;
        return this;
    }

    public CurrencyExchangeBuilder withCurrencies(final List<Currency> currencies) {
        this.currencies = currencies;
        return this;
    }

    public CurrencyExchange build() {
        return new CurrencyExchange(name, country, city, address, currency, margin, currencies);
    }
}
