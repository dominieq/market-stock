package org.example.marketstock.models.exchange;

import java.io.Serializable;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

/**
 * Represents a real life exchange where investors can buy or sell assets listed there.
 *
 * @author Dominik Szmyt
 * @since 1.0.0
 */
public abstract class Exchange implements Serializable {

    protected final String name;
    protected final String country;
    protected final String city;
    protected final String address;
    protected final String currency;
    protected final double margin;

    /**
     * Create an {@code Exchange} with all necessary fields.
     * @param name The name of an {@code Exchange}.
     * @param country The country in which an {@code Exchange} is located.
     * @param city The city in which an {@code Exchange} is located.
     * @param address The address of an {@code Exchange} location
     * @param currency The currency used in transactions on an {@code Exchange}.
     * @param margin The margin of an {@code Exchange}.
     */
    public Exchange(final String name,
                    final String country,
                    final String city,
                    final String address,
                    final String currency,
                    final double margin) {

        this.name = name;
        this.country = country;
        this.city = city;
        this.address = address;
        this.currency = currency;
        this.margin = margin;
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
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Exchange)) return false;
        Exchange exchange = (Exchange) o;
        return Objects.equal(name, exchange.name) &&
                Objects.equal(country, exchange.country) &&
                Objects.equal(city, exchange.city) &&
                Objects.equal(address, exchange.address) &&
                Objects.equal(currency, exchange.currency) &&
                Double.compare(exchange.margin, margin) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name, country, city, address, currency, margin);
    }

    public String getName() {
        return this.name;
    }

    public String getCountry(){
        return this.country;
    }

    public String getCity() {
        return this.city;
    }

    public String getAddress() {
        return this.address;
    }

    public String getCurrency(){
        return this.currency;
    }

    public double getMargin() {
        return this.margin;
    }
}
