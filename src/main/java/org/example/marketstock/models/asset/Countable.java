package org.example.marketstock.models.asset;

public interface Countable {

    double increaseNumberOfAssets(final int addend);
    double decreaseNumberOfAssets(final int subtrahend);
    int getNumberOfAssets();
}
