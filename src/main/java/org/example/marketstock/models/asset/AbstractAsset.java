package org.example.marketstock.models.asset;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A simple implementation of {@link Asset}.
 *
 * @author Dominik
 * @since 1.0.0
 */
public abstract class AbstractAsset implements Asset, Serializable {

    protected static Logger LOGGER = LogManager.getLogger(AbstractAsset.class);

    protected final String name;
    protected volatile double currentRate;
    protected volatile double minRate;
    protected volatile double maxRate;
    protected final double margin;
    protected final List<Double> rateChanges;

    /**
     * Create an {@code AbstractAsset} with all necessary fields.
     * @param name1 The name of an {@code AbstractAsset}.
     * @param currentRate1 The current rate of an {@code AbstractAsset}.
     * @param minRate1 The minimum rate of an {@code AbstractAsset}.
     * @param maxRate1 The maximum rate of an {@code AbstractAsset}.
     * @param margin1 The margin of an {@code Exchange} that lists the {@code AbstractAsset}.
     * @param rateChanges1 The list of rate changes of an {@code AbstractAsset}.
     */
    public AbstractAsset(final String name1,
                         final double currentRate1,
                         final double minRate1,
                         final double maxRate1,
                         final double margin1,
                         final List<Double> rateChanges1) {

        name = name1;
        currentRate = currentRate1;
        minRate = minRate1;
        maxRate = maxRate1;
        margin = margin1;
        rateChanges = rateChanges1;
    }
    
    /**
     * At first, replaces old value with the new one and adds the new rate to rate changes.
     * In the end, calculates min and max rate.
     * <br>
     * <b>NOTE:</b> this implementation keeps track of only 10 rate changes.
     *
     * @param rate A new rate that is to replace the current rate.
     * @return A new value of current rate.
     */
    @Override
    public synchronized double updateRate(double rate) {
        LOGGER.info("[ASSET]: Rate changes from {} to {} in {}.", currentRate, rate, this);

        currentRate = rate;
        rateChanges.add(rate);

        if (rateChanges.size() >= 10) {
            rateChanges.remove(0);
        }

        maxRate = Collections.max(rateChanges);
        minRate = Collections.min(rateChanges);

        return currentRate;
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
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractAsset)) return false;
        AbstractAsset that = (AbstractAsset) o;
        return Objects.equal(name, that.name) &&
                Double.compare(that.margin, margin) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name, margin);
    }
}
