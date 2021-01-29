package org.example.marketstock.models.asset;

import com.google.common.base.MoreObjects;

import java.util.List;

/**
 * Represents a simple and countable {@code Asset}.
 *
 * @author Dominik Szmyt
 * @since 1.0.0
 */
public abstract class CountableAsset extends AbstractAsset implements Countable {

    protected final Object NUMBER_OF_ASSETS_LOCK = new Object();
    protected volatile int numberOfAssets;

    /**
     * Create a {@code CountableAsset} with all necessary fields.
     * @param name1 The name of a {@code CountableAsset}.
     * @param currentRate1 The current rate of a {@code CountableAsset}.
     * @param minRate1 The minimum rate of a {@code CountableAsset}.
     * @param maxRate1 The maximum rate of a {@code CountableAsset}.
     * @param margin1 The margin of an {@code Exchange} that lists the {@code CountableAsset}.
     * @param rateChanges1 The list of rate changes of a {@code CountableAsset}.
     * @param numberOfAssets1 The number of {@code CountableAsset}.
     */
    public CountableAsset(final String name1,
                          final double currentRate1,
                          final double minRate1,
                          final double maxRate1,
                          final double margin1,
                          final List<Double> rateChanges1,
                          final int numberOfAssets1) {

        super(name1, currentRate1, minRate1, maxRate1, margin1, rateChanges1);

        numberOfAssets = numberOfAssets1;
    }

    /**
     * Increases number of assets by a given value.
     * @param addend A value that is to be added to the number of assets.
     * @return The number of assets after addition.
     */
    @Override
    public double increaseNumberOfAssets(int addend) {
        synchronized (NUMBER_OF_ASSETS_LOCK) {
            final int sum = numberOfAssets + addend;
            LOGGER.info("[COUNTABLE]: Number increases from {} to {} in {}.", numberOfAssets, sum, this);
            numberOfAssets = sum;
            return numberOfAssets;
        }
    }

    /**
     * Decreases number of assets by a given value if the value isn't greater than the original number.
     * @param subtrahend A value that is to be subtracted from the number of assets.
     * @return The number of assets after possible subtraction.
     */
    @Override
    public double decreaseNumberOfAssets(int subtrahend) {
        synchronized (NUMBER_OF_ASSETS_LOCK) {
            if (numberOfAssets - subtrahend < 0) {
                LOGGER.warn("[COUNTABLE]: Aborting decrease in {} because negative difference.", this);
                return numberOfAssets;
            }

            final int difference = numberOfAssets - subtrahend;
            LOGGER.info("[COUNTABLE]: Number decreases from {} to {} in {}.", numberOfAssets, difference, this);
            numberOfAssets = difference;
            return numberOfAssets;
        }
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("numberOfAssets", numberOfAssets)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CountableAsset)) return false;
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
