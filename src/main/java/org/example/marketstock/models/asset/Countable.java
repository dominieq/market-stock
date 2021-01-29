package org.example.marketstock.models.asset;

/**
 * Represents the concept of countability. A {@code Countable} class needs to store a number of it's assets
 * and supply external users with methods to change that value.
 * <br>
 * <b>NOTE:</b> it is possible for a {@code Countable} class to have an unlimited number of assets.
 *
 * @author Dominik Szmyt
 * @since 1.0.0
 */
public interface Countable {


    /**
     * Each {@code Countable} implementation should provide a method to increase the number of it's assets.
     * @param addend A value that is to be added to the number of assets.
     * @return The number of assets after addition.
     */
    double increaseNumberOfAssets(final int addend);

    /**
     * Each {@code Countable} implementation should provide a method to decrease the number of it's assets.
     * @param subtrahend A value that is to be subtracted from the number of assets.
     * @return The number of assets after subtraction.
     */
    double decreaseNumberOfAssets(final int subtrahend);

    /**
     * Each {@code Countable} implementation should store a number of it's assets.
     * @return The number of assets.
     */
    int getNumberOfAssets();
}
