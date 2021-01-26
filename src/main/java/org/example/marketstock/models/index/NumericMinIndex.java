package org.example.marketstock.models.index;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.example.marketstock.models.asset.Asset;
import org.example.marketstock.models.index.builder.NumericMinIndexBuilder;

import java.util.List;

/**
 * Represents a set of {@link Asset}s
 * that are listed in a certain {@link org.example.marketstock.models.exchange.Exchange}.
 * This implementation gathers assets that have the lowest current rate.
 *
 * @since 1.0.0
 * @author Dominik Szmyt
 */
@JsonDeserialize(builder = NumericMinIndexBuilder.class)
public class NumericMinIndex extends NumericIndex {

    private final IndexType type = IndexType.MIN;

    public NumericMinIndex(final String name,
                           final long size,
                           final List<Asset> content,
                           final double value) {

        super(name, size, content, value);
    }

    /**
     * This implementation when used in sorted method should arrange provided content in ascending order.
     *
     * @param asset1 - First asset to compare.
     * @param asset2 - Second asset to compare.
     * @return int - 0 when assets are equal; 1 when first asset has higher current rate than second; -1 otherwise.
     */
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
