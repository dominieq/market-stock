package org.example.marketstock.models.index;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.example.marketstock.models.asset.Asset;
import org.example.marketstock.models.index.builder.NumericMaxIndexBuilder;

/**
 * Represents a set of {@link Asset}s
 * that are listed in a certain {@link org.example.marketstock.models.exchange.Exchange}.
 * This implementation gathers assets that have the highest current rate.
 *
 * @since 1.0.0
 * @author Dominik Szmyt
 */
@JsonDeserialize(builder = NumericMaxIndexBuilder.class)
public class NumericMaxIndex extends NumericIndex implements Serializable {

    private final IndexType type = IndexType.NUMERIC_MAX;

    /**
     * Create a {@code NumericMaxIndex} with all necessary fields.
     * @param name1 The name of a {@code NumericMaxIndex}
     * @param size1 The size of a {@code NumericMaxIndex}
     * @param content1 The content of a {@code NumericMaxIndex}
     * @param value1 The value of a {@code NumericMaxIndex}
     */
    public NumericMaxIndex(final String name1,
                           final long size1,
                           final List<Asset> content1,
                           final double value1) {

        super(name1, size1, content1, value1);
    }

    /**
     * This implementation when used in sorted method should arrange provided content in descending order.
     *
     * @param asset1 First asset to compare.
     * @param asset2 Second asset to compare.
     * @return 0 when assets are equal; -1 when first asset has higher current rate than second; 1 otherwise.
     */
    @Override
    protected int compare(Asset asset1, Asset asset2) {
        if (asset1.getCurrentRate() == asset2.getCurrentRate()) {
            return 0;
        }

        return  asset1.getCurrentRate() > asset2.getCurrentRate() ? -1 : 1;
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
        if (!(o instanceof NumericMaxIndex)) return false;
        NumericMaxIndex index = (NumericMaxIndex) o;
        return type == index.type;
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
    public double getValue() {
        return value;
    }

    @Override
    public List<Asset> getContent() {
        return content;
    }
}
