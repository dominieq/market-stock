package org.example.marketstock.models.index;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.example.marketstock.models.asset.Asset;

import java.util.List;

/**
 * Represents a named set of {@link Asset}s
 * that are listed in a certain {@link org.example.marketstock.models.exchange.Exchange}.
 *
 * @since 1.0.0
 * @author Dominik Szmyt
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = NumericMaxIndex.class, name = "NUMERIC_MAX"),
        @JsonSubTypes.Type(value = NumericMinIndex.class, name = "NUMERIC_MIN")
})
public interface Index {

    /**
     * Each {@code Index} implementation should have a unique type.
     * @return The type of an index
     */
    IndexType getType();

    /**
     * Each {@code Index} implementation should have a name that isn't required to be unique.
     * @return The name of an index.
     */
    String getName();

    /**
     * Each {@code Index} implementation should have a size which is the maximum size of it's content.
     * @return The size of an index.
     */
    long getSize();

    /**
     * Each {@code Index} implementation should have a content which is a list of assets.
     * The list should have a size equal to the size of an index.
     * Each index should create content according to it's intended use.
     * @return The content of an index.
     */
    List<Asset> getContent();

    /**
     * Each {@code Index} implementation should have a value
     * which is the sum of current rates of each asset that belongs to an index.
     * @return The value of an index.
     */
    double getValue();

    /**
     * Each {@code Index} implementation should provide a method that will update it's content.
     * Method should take raw list of assets and modify it's content according to it's intended use and size.
     * @param content The list of assets that may replace the old content.
     */
    void updateIndex(List<Asset> content);
}
