package org.example.marketstock.models.index;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.example.marketstock.models.asset.Asset;

import java.util.List;

/**
 *
 * @author Dominik Szmyt
 * @since 1.1.0
 */
public class NumericMinIndex extends NumericIndex {

    private final IndexType type = IndexType.MIN;

    public NumericMinIndex(final String name,
                           final long size,
                           final List<Asset> content,
                           final double value) {

        super(name, size, content, value);
    }

    @Override
    protected int compare(Asset asset1, Asset asset2) {
        if (asset1.getCurrentRate() == asset2.getCurrentRate()) {
            return 0;
        }

        return asset1.getCurrentRate() > asset2.getCurrentRate() ? 1 : -1;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("type", type)
                .add("name", name)
                .add("size", size)
                .add("content", content)
                .add("value", value)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NumericMinIndex)) return false;
        NumericMinIndex that = (NumericMinIndex) o;
        return type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(type);
    }

    @Override
    public IndexType getType() {
        return type;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public long getSize() {
        return size;
    }

    @Override
    public List<Asset> getContent() {
        return content;
    }

    @Override
    public double getValue() {
        return value;
    }
}
