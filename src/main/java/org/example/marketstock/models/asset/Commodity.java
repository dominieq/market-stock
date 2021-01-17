package org.example.marketstock.models.asset;

import java.io.Serializable;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.example.marketstock.models.asset.builder.CommodityBuilder;

/**
 *
 * @author Dominik
 * @since 1.0.0
 */
@JsonDeserialize(builder = CommodityBuilder.class)
public class Commodity extends AbstractAsset implements Serializable {
    
    private final String unitOfTrading;
    private final String currency;

    public Commodity(final String name,
                     final double currentRate,
                     final double minRate,
                     final double maxRate,
                     final double margin,
                     final List<Double> rateChanges,
                     final String unitOfTrading,
                     final String currency) {

        super(name, currentRate, minRate, maxRate, margin, rateChanges);

        this.unitOfTrading = unitOfTrading;
        this.currency = currency;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("name", name)
                .add("currentRate", currentRate)
                .add("minRate", minRate)
                .add("maxRate", maxRate)
                .add("margin", margin)
                .add("rateChanges", rateChanges)
                .add("unitOfTrading", unitOfTrading)
                .add("currency", currency)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Commodity commodity = (Commodity) o;
        return Objects.equal(name, commodity.name) &&
                Objects.equal(unitOfTrading, commodity.unitOfTrading) &&
                Objects.equal(currency, commodity.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name, unitOfTrading, currency);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public double getCurrentRate() {
        return currentRate;
    }

    @Override
    public double getMinRate() {
        return minRate;
    }

    @Override
    public double getMaxRate() {
        return maxRate;
    }

    @Override
    public double getMargin() {
        return margin;
    }

    @Override
    public List<Double> getRateChanges() {
        return rateChanges;
    }

    public String getUnitOfTrading() {
        return this.unitOfTrading;
    }

    public String getCurrency() {
        return this.currency;
    }
}
