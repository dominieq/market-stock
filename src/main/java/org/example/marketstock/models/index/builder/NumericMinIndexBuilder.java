package org.example.marketstock.models.index.builder;

import org.example.marketstock.models.index.NumericMinIndex;
import org.example.marketstock.models.asset.Asset;

import java.util.List;

public final class NumericMinIndexBuilder {

    private String name;
    private long size;
    private List<Asset> content;
    private double value;

    private NumericMinIndexBuilder() { }

    public static NumericMinIndexBuilder builder() {
        return new NumericMinIndexBuilder();
    }

    public NumericMinIndexBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public NumericMinIndexBuilder withSize(long size) {
        this.size = size;
        return this;
    }

    public NumericMinIndexBuilder withContent(List<Asset> content) {
        this.content = content;
        return this;
    }

    public NumericMinIndexBuilder withValue(double value) {
        this.value = value;
        return this;
    }

    public NumericMinIndex build() {
        return new NumericMinIndex(name, size, content, value);
    }
}
