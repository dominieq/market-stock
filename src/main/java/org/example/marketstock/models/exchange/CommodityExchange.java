package org.example.marketstock.models.exchange;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.MoreObjects;
import org.example.marketstock.models.asset.Commodity;
import org.example.marketstock.models.exchange.builder.CommodityExchangeBuilder;

/**
 * Represents a real life commodity exchange where investors can buy and sell commodities listed there.
 *
 * @author Dominik Szmyt
 * @since 1.0.0
 */
@JsonDeserialize(builder = CommodityExchangeBuilder.class)
public class CommodityExchange extends Exchange implements Serializable {

    private final List<Commodity> commodities;

    /**
     * Create an {@code CommodityExchange} with all necessary fields.
     * @param name The name of an {@code CommodityExchange}.
     * @param country The country in which an {@code CommodityExchange} is located.
     * @param city The city in which an {@code CommodityExchange} is located.
     * @param address The address of an {@code CommodityExchange} location
     * @param currency The currency used in transactions on an {@code CommodityExchange}.
     * @param margin The margin of an {@code CommodityExchange}.
     * @param commodities A list of commodities listed by an {@code CommodityExchange}.
     */
    public CommodityExchange(final String name,
                             final String country,
                             final String city,
                             final String address,
                             final String currency,
                             final double margin,
                             final List<Commodity> commodities) {

        super(name, country, city, address, currency, margin);

        this.commodities = commodities;
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
                .add("commodities", commodities)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CommodityExchange)) return false;
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public List<Commodity> getCommodities() {
        return commodities;
    }

    public void addResource(Commodity resource) {
        this.commodities.add(resource);
    }

    public void removeResource(Commodity resource) {
        this.commodities.remove(resource);
    }
}
