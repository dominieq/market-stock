package org.example.marketstock.models.index;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.marketstock.models.asset.Asset;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

/**
 * Represents a set of {@link Asset}s that are listed in a certain {@link org.example.marketstock.models.exchange.Exchange}.
 * Any numeric characteristic describing the {@link Asset} can become a main criterion when assigning an asset to this index.
 *
 * @since 1.0.0
 * @author Dominik Szmyt
 */
public abstract class NumericIndex implements Index {

    protected static final Logger LOGGER = LogManager.getLogger(NumericIndex.class);

    protected String name;
    protected long size;
    protected List<Asset> content;
    protected double value;

    protected NumericIndex(final String name1,
                           final long size1,
                           final List<Asset> content1,
                           final double value1) {

        name = name1;
        size = size1;
        content = content1;
        value = value1;

        updateIndex(content1);
    }

    /**
     * Updates content by sorting provided list using {@link #compare(Asset, Asset)}} method
     * and then limits the size of a result list to the size of a specific index.
     * @param content1 - The list of assets that may replace the old content.
     */
    @Override
    public void updateIndex(List<Asset> content1) {
        content = new ArrayList<>();

        if (nonNull(content1) && !content1.isEmpty()) {
            content = content1.stream()
                    .sorted(this::compare)
                    .limit(size)
                    .collect(Collectors.toList());
            LOGGER.debug("[INDEX]: Content changes in {}", this);
        }

        calculateValue();
    }

    /**
     * Calculates the value of an index which is the sum of current rates
     * of each asset that belongs to the index.
     */
    protected void calculateValue() {
        value = 0D;

        if (nonNull(content) && !content.isEmpty()) {
            value = content.stream()
                    .map(Asset::getCurrentRate)
                    .mapToDouble(Double::doubleValue)
                    .sum();
            LOGGER.debug("[INDEX]: Value changes to {} in {}.", value, this);
        }
    }

    /**
     * Each {@link NumericIndex} extension should provide a method that compares two assets.
     * The method should return 0 when assets are equal and -1 or 1 when they aren't.
     * @param asset1 - First asset to compare.
     * @param asset2 - Second asset to compare.
     * @return int - 0 when assets are equal; -1 or 1 depending on the intended order.
     */
    abstract protected int compare(Asset asset1, Asset asset2);
}
