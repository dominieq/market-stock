package org.example.marketstock.models.index.builder;

import org.example.marketstock.models.index.NumericMaxIndex;
import org.example.marketstock.models.asset.Asset;

import java.util.List;

public final class NumericMaxIndexBuilder {

    private String name;
    private long size;
    private List<Asset> content;
    private double value;

    private NumericMaxIndexBuilder() { }

    public static NumericMaxIndexBuilder builder() {
        return new NumericMaxIndexBuilder();
    }

    public NumericMaxIndexBuilder withName(final String name) {
        this.name = name;
        return this;
    }

    public NumericMaxIndexBuilder withSize(final long size) {
        this.size = size;
        return this;
    }

    public NumericMaxIndexBuilder withValue(final double value) {
        this.value = value;
        return this;
    }

    public NumericMaxIndexBuilder withContent(final List<Asset> companies) {
        this.content = companies;
        return this;
    }

    public NumericMaxIndex build() {
        return new NumericMaxIndex(name, size, content, value);
    }
}
