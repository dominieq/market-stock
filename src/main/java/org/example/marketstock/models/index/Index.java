package org.example.marketstock.models.index;

import org.example.marketstock.models.asset.Asset;

import java.util.List;

/**
 *
 * @author Dominik Szmyt
 * @since 1.1.0
 */
public interface Index {

    IndexType getType();
    String getName();
    long getSize();
    List<Asset> getContent();
    double getValue();
    void updateIndex(List<Asset> content);
}
