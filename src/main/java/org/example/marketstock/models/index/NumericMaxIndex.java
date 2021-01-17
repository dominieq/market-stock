package org.example.marketstock.models.index;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.example.marketstock.models.asset.Asset;
import org.example.marketstock.models.index.builder.NumericMaxIndexBuilder;

/**
 *
 * @author Dominik Szmyt
 * @since 1.1.0
 */
@JsonDeserialize(builder = NumericMaxIndexBuilder.class)
public class NumericMaxIndex extends NumericIndex implements Serializable {

    private final IndexType type = IndexType.MAX;

    public NumericMaxIndex(final String name1,
                           final long size1,
                           final List<Asset> content1,
                           final double value1) {

        super(name1, size1, content1, value1);
    }

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
