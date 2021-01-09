package org.example.marketstock.models.exchange;

import java.io.Serializable;
import java.util.List;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.example.marketstock.models.asset.Currency;

/**
 *
 * @author Dominik
 * @since 1.0.0
 */
public class CurrencyExchange extends Exchange implements Serializable {

    private final List<Currency> currencies;

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
