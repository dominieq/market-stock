package org.example.marketstock.models.entity;

import com.google.common.base.Objects;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Serializable;
import java.util.Random;

import org.example.marketstock.models.briefcase.Briefcase;
import org.example.marketstock.models.asset.*;

/**
 * A simple implementation of an {@code Entity}.
 *
 * @author Dominik Szmyt
 * @since 1.0.0
 */
public abstract class AbstractEntity implements Entity, Serializable {

    public static final double BUDGET_UPDATE_LOWER_BOUNDARY = 20_000D;
    public static final double BUDGET_UPDATE_UPPER_BOUNDARY = 50_000D;

    protected static Logger LOGGER = LogManager.getLogger(AbstractEntity.class);

    protected double budget;
    protected final Briefcase briefcase;

    /**
     * Create an {@code AbstractEntity} with all necessary fields.
     * @param budget1 The budget of an {@code AbstractEntity}.
     * @param briefcase1 A {@code Briefcase} that belongs to an {@code AbstractEntity}.
     */
    public AbstractEntity(final double budget1,
                          final Briefcase briefcase1) {

        budget = budget1;
        briefcase = briefcase1;
    }

    /**
     * At first, checks whether an {@code AbstractEntity} can afford this transaction.
     * If an {@code AbstractEntity} has enough money, subtracts the price from entity's budget.
     * In the end, increases the number of an {@link Asset} or adds it to a {@link Briefcase}.
     *
     * @param price The total cost of added assets.
     * @param asset An asset that is to has it's number increased.
     * @param numberOfAsset The number of a given asset.
     */
    @Override
    public void addAsset(final double price, final Asset asset, final Integer numberOfAsset) {
        if (budget - price < 0.0) {
            LOGGER.warn("[PURCHASE]: Not enough budget to add {} by {}.", asset, this);
            return;
        }

        budget -= price;
        briefcase.addOrIncrease(asset, numberOfAsset);
        LOGGER.info("[PURCHASE]: {} bought {} of {} for {} and their budget equals {}.",
                this, numberOfAsset, asset, price, budget);
    }

    /**
     * Tries to decrease the number of a given {@link Asset} or to remove it from a {@link Briefcase}.
     * If the number of successfully removed assets is equal to the provided number of assets,
     * completes transaction by adding the price to entity's budget.
     *
     * @param price The total cost of subtracted assets.
     * @param asset An asset that is to have it's number decreased.
     * @param numberOfAsset The number of a given asset.
     */
    @Override
    public void subtractAsset(double price, Asset asset, Integer numberOfAsset) {
        final int numberOfRemovedAssets = briefcase.decreaseOrRemove(asset, numberOfAsset);

        if (numberOfRemovedAssets == numberOfAsset) {
            budget += price;
            LOGGER.info("[PURCHASE]: {} sold {} of {} for {} and their budget equals {}.",
                    this, numberOfAsset, asset, price, budget);
        } else if (numberOfRemovedAssets == 0) {
            LOGGER.warn("[PURCHASE]: {} didn't sell {} because they didn't have it.", this, asset);
        } else if (numberOfRemovedAssets < 0) {
            LOGGER.error("[PURCHASE]: {} didn't sell {} because they didn't have enough.", this, asset);
        }
    }
    
    /**
     * Adds a random amount of money to entity's budget.
     * The amount is drawn from a range between 20 000 and 50 000.
     */
    protected void increaseBudget() {
        final Random rand = new Random();
        final double sum = budget + BUDGET_UPDATE_LOWER_BOUNDARY
                + (BUDGET_UPDATE_UPPER_BOUNDARY - BUDGET_UPDATE_LOWER_BOUNDARY) * rand.nextDouble();

        LOGGER.info("[ENTITY]: Budget increases from {} to {} in {}.", budget, sum, this);
        budget = sum;
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
