package org.example.marketstock.models.index;

import org.example.marketstock.models.asset.Asset;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

/**
 *
 * @author Dominik Szmyt
 * @since 1.1.0
 */
public abstract class NumericIndex implements Index {

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

    @Override
    public void updateIndex(List<Asset> content1) {
        content = new ArrayList<>();

        if (nonNull(content1) && !content1.isEmpty()) {
            content = content1.stream()
                    .sorted(this::compare)
                    .limit(size)
                    .collect(Collectors.toList());
        }

        calculateValue();
    }

    protected void calculateValue() {
        value = 0D;

        if (nonNull(content) && !content.isEmpty()) {
            value = content.stream()
                    .map(Asset::getCurrentRate)
                    .mapToDouble(Double::doubleValue)
                    .sum();
        }
    }

    abstract protected int compare(Asset asset1, Asset asset2);
}
