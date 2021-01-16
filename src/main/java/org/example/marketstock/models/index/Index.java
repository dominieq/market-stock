package org.example.marketstock.models.index;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.example.marketstock.models.asset.Asset;

import java.util.List;

/**
 *
 * @author Dominik Szmyt
 * @since 1.1.0
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = NumericMaxIndex.class, name = "numericMaxIndex"),
        @JsonSubTypes.Type(value = NumericMinIndex.class, name = "numericMinIndex")
})
public interface Index {

    IndexType getType();
    String getName();
    long getSize();
    List<Asset> getContent();
    double getValue();
    void updateIndex(List<Asset> content);
}
