package org.example.marketstock.models.exchange;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.MoreObjects;
import org.example.marketstock.models.asset.Currency;
import org.example.marketstock.models.exchange.builder.CurrencyExchangeBuilder;

/**
 * Represents a real life currency exchange where investors can buy and sell currencies listed there.
 *
 * @author Dominik Szmyt
 * @since 1.0.0
 */
@JsonDeserialize(builder = CurrencyExchangeBuilder.class)
public class CurrencyExchange extends Exchange implements Serializable {

    private final List<Currency> currencies;

    /**
     * Create an {@code CurrencyExchange} with all necessary fields.
     * @param name The name of an {@code CurrencyExchange}.
     * @param country The country in which an {@code CurrencyExchange} is located.
     * @param city The city in which an {@code CurrencyExchange} is located.
     * @param address The address of an {@code CurrencyExchange} location
     * @param currency The currency used in transactions on an {@code CurrencyExchange}.
     * @param margin The margin of an {@code CurrencyExchange}.
     * @param currencies A list of currencies listed by an {@code CurrencyExchange}.
     */
    public CurrencyExchange(final String name,
                            final String country,
                            final String city,
                            final String address,
                            final String currency,
                            final double margin,
                            final List<Currency> currencies) {

        super(name, country, city, address, currency, margin);

        this.currencies = currencies;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("name", name)
                .add("country", country)
                .add("city", city)
                .add("address", address)
                .add("currency", currency)
                .add("margin", margin)
                .add("currencies", currencies)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CurrencyExchange)) return false;
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public List<Currency> getCurrencies() {
        return this.currencies;
    }

    public void addCurrency(Currency currency) {
        currencies.add(currency);
    }

    public void removeCurrency(Currency currency) {
        currencies.remove(currency);
    }
}
