package org.example.marketstock.models.asset;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
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
     * Updates current rate and adds new rate to rate changes.
     * Calculates min and max rate.
     * @param rate - New rate calculated after an investor bought this asset.
     * @return Updated current rate.
     */
    @Override
    public synchronized double updateRate(double rate) {
        LOGGER.info("{} changes rate from {} to {}", name, currentRate, rate);

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
