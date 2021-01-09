package org.example.marketstock.models.entity;

import com.google.common.base.Objects;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Serializable;
import java.util.Random;

import org.example.marketstock.models.briefcase.Briefcase;
import org.example.marketstock.models.asset.*;

/**
 *
 * @author Dominik
 * @since 1.0.0
 */
public abstract class AbstractEntity implements Entity, Serializable {

    public static final double BUDGET_UPDATE_LOWER_BOUNDARY = 20_000D;
    public static final double BUDGET_UPDATE_UPPER_BOUNDARY = 50_000D;

    protected static Logger LOGGER = LogManager.getLogger(AbstractEntity.class);

    protected double budget;
    protected final Briefcase briefcase;

    public AbstractEntity(final double budget1,
                          final Briefcase briefcase1) {

        budget = budget1;
        briefcase = briefcase1;
    }

    @Override
    public void addAsset(final double price, final Asset asset, final Integer numberOfAsset) {
        if (budget - price < 0.0) {
            return;
        }

        budget -= price;
        briefcase.addOrIncrease(asset, numberOfAsset);
    }

    @Override
    public void subtractAsset(double price, Asset asset, Integer numberOfAsset) {
        final int numberOfRemovedAssets = briefcase.decreaseOrRemove(asset, numberOfAsset);

        if (numberOfRemovedAssets == numberOfAsset) {
            budget += price;
        }
    }
    
    /**
     * An entity adds a random amount of money to their budget.
     * The amount is drawn from a range between 20 000 and 50 000.
     */
    protected void increaseBudget() {
        final Random rand = new Random();

        budget += BUDGET_UPDATE_LOWER_BOUNDARY
                + (BUDGET_UPDATE_UPPER_BOUNDARY - BUDGET_UPDATE_LOWER_BOUNDARY) * rand.nextDouble();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractEntity)) return false;
        AbstractEntity that = (AbstractEntity) o;
        return Double.compare(that.budget, budget) == 0 &&
                Objects.equal(briefcase, that.briefcase);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(budget, briefcase);
    }

    @Override
    public double getBudget() {
        return budget;
    }

    @Override
    public Briefcase getBriefcase() {
        return briefcase;
    }
}
