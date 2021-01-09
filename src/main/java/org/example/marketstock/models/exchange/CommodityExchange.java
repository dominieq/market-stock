package org.example.marketstock.models.exchange;

import java.io.Serializable;
import java.util.List;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.example.marketstock.models.asset.Commodity;

/**
 *
 * @author Dominik
 * @since 1.0.0
 */
public class CommodityExchange extends Exchange implements Serializable {

    private final List<Commodity> commodities;

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
