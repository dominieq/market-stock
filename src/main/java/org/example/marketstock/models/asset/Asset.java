package org.example.marketstock.models.asset;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.example.marketstock.models.company.Company;
import org.example.marketstock.models.entity.InvestmentFund;

import java.util.List;

/**
 * Represents the concept of a real life asset. An {@code Asset} can be bought and sold by investors.
 * It's rate should change with every performed transaction.
 *
 * @author Dominik Szmyt
 * @since 1.0.0
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Commodity.class, name = "commodity"),
        @JsonSubTypes.Type(value = Company.class, name = "company"),
        @JsonSubTypes.Type(value = Currency.class, name = "currency"),
        @JsonSubTypes.Type(value = InvestmentFund.class, name = "investmentFund")
})
public interface Asset {

    /**
     * Each {@code Asset} implementation should have a name.
     * @return The name of an {@code Asset}.
     */
    String getName();

    /**
     * Each {@code Asset} implementation should have a current rate.
     * @return The current rate of an {@code Asset}.
     */
    double getCurrentRate();

    /**
     * Each {@code Asset} implementation should store a minimum rate.
     * @return The minimum rate of an {@code Asset}.
     */
    double getMinRate();

    /**
     * Each {@code Asset} implementation should store a maximum rate.
     * @return The maximum rate of an {@code Asset}.
     */
    double getMaxRate();

    /**
     * Each {@code Asset} should store a list of it's rate changes.
     * @return The list of rate changes of an {@code Asset}.
     */
    List<Double> getRateChanges();

    /**
     * Each {@code Asset} should store a margin of an exchange that is listing it.
     * @return The margin of an exchange that is listing it.
     */
    double getMargin();

    /**
     * Each {@code Asset} implementation should provide a method to update it's rate.
     * @param rate A new rate that is to replace the current rate.
     * @return A new value of current rate.
     */
    double updateRate(double rate);
}
