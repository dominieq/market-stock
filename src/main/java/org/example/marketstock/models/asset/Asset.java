package org.example.marketstock.models.asset;

import java.util.List;

public interface Asset {

    String getName();
    double getCurrentRate();
    double getMinRate();
    double getMaxRate();
    List<Double> getRateChanges();
    double getMargin();
    double updateRate(double rate);
}
