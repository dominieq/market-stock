package org.example.marketstock.models.entity;

import org.example.marketstock.models.briefcase.Briefcase;
import org.example.marketstock.models.asset.Asset;

/**
 * Represents an entity that can buy and sell assets listed in many exchanges.
 *
 * @author Dominik Szmyt
 * @since 1.0.0
 */
public interface Entity {

    /**
     * Increases number of a given {@link Asset} or adds it to a {@link Briefcase}.
     * Subtracts the price from entity's budget.
     *
     * @param price The total cost of added assets.
     * @param asset An asset that is to has it's number increased.
     * @param numberOfAsset The number of a given asset.
     */
    void addAsset(double price, Asset asset, Integer numberOfAsset);

    /**
     * Decreases number of a given {@link Asset} or removes it from a {@link Briefcase}.
     * Adds the price to entity's budget.
     *
     * @param price The total cost of subtracted assets.
     * @param asset An asset that is to have it's number decreased.
     * @param numberOfAsset The number of a given asset.
     */
    void subtractAsset(double price, Asset asset, Integer numberOfAsset);

    /**
     * Each {@code Entity} implementation should have a budget.
     * @return The budget of an {@code Entity}.
     */
    double getBudget();

    /**
     * Each {@code Entity} implementation should have a {@code Briefcase}
     * @return A {@code Briefcase} that belongs to an {@code Entity}
     * @see Briefcase
     */
    Briefcase getBriefcase();
}
