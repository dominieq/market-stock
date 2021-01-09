package org.example.marketstock.models.entity;

import org.example.marketstock.models.briefcase.Briefcase;
import org.example.marketstock.models.asset.Asset;

public interface Entity {

    void addAsset(double price, Asset asset, Integer numberOfAsset);
    void subtractAsset(double price, Asset asset, Integer numberOfAsset);
    double getBudget();
    Briefcase getBriefcase();
}
