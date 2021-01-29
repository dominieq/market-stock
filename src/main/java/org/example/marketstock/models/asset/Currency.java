package org.example.marketstock.models.asset;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.example.marketstock.models.asset.builder.CurrencyBuilder;

/**
 * Represents a real life currency that can be bought and sold by investors.
 *
 * @author Dominik Szmyt
 * @since 1.0.0
 */
@JsonDeserialize(builder = CurrencyBuilder.class)
public class Currency extends AbstractAsset implements Serializable {
    
    private final Currency comparisonCurrency;
    private final List<String> countries;

    /**
     * Create a {@code Currency} with all necessary fields.
     * @param name The name of a {@code Currency}.
     * @param currentRate The current rate of a {@code Currency}.
     * @param minRate The minimum rate of a {@code Currency}.
     * @param maxRate The maximum rate of a {@code Currency}.
     * @param margin The margin of an {@code Exchange} that lists the {@code Currency}.
     * @param rateChanges The list of rate changes of an {@code Currency}.
     * @param comparisonCurrency A currency in comparison to which a current rate is calculated.
     * @param countries The list of countries in which the {@code Currency} can be used.
     */
    public Currency(final String name,
                    final double currentRate,
                    final double minRate,
                    final double maxRate,
                    final double margin,
                    final List<Double> rateChanges,
                    final Currency comparisonCurrency,
                    final List<String> countries) {

        super(name, currentRate, minRate, maxRate, margin, rateChanges);

        this.comparisonCurrency = comparisonCurrency;
        this.countries = countries;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("name", name)
                .add("currentRate", currentRate)
                .add("minRate", minRate)
                .add("maxRate", maxRate)
                .add("rateChanges", rateChanges)
                .add("margin", margin)
                .add("countries", countries)
                .add("comparisonCurrency", comparisonCurrency)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Currency)) return false;
        if (!super.equals(o)) return false;
        Currency currency = (Currency) o;
        return Objects.equal(comparisonCurrency, currency.comparisonCurrency) &&
                Objects.equal(countries, currency.countries);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(super.hashCode(), comparisonCurrency, countries);
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

    public List<String> getCountries() {
        return this.countries;
    }

    public Currency getComparisonCurrency() {
        return this.comparisonCurrency;
    }
}
