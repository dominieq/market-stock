package org.example.marketstock.models.briefcase;

import java.io.Serializable;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.marketstock.exceptions.BuyingException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.marketstock.models.asset.Asset;

/**
 *
 * @author Dominik
 * @since 1.0.0
 */
public class Briefcase implements Serializable {

    private static final Logger LOGGER = LogManager.getLogger(Briefcase.class);
    private ArrayList<Asset> assets;
    private ArrayList<Integer> numbersOfAssets;

    public Briefcase() {
        this.assets = new ArrayList<>();
        this.numbersOfAssets = new ArrayList<>();
    }
    
    /**
     * Functions increases number of given Asset's instance or adds it to Briefcase.
     * @param asset - Asset from player's Briefcase or new Asset
     * @param number - Number of asset's instances
     */
    public void addAsset(Asset asset, Integer number) {
        if(this.assets.contains(asset)) {
            int index = this.assets.indexOf(asset);
            int value = this.numbersOfAssets.get(index);

            this.numbersOfAssets.set(index, value + number);
        } else {
            this.assets.add(asset);
            this.numbersOfAssets.add(number);
        }

        LOGGER.info("{} of {} was added to briefcase.", new Object[]{number, asset.getName()});
    }
    
    /**
     * Functions reduces number of given Asset's instance or removes it when it's number equals 0.
     * @param asset - Asset from player's Briefcase.
     * @param number - Number of asset's instances.
     * @throws BuyingException when there is going to be a negative number of selected asset.
     */
    public void deleteAsset(Asset asset, Integer number) throws BuyingException {
        int index = this.assets.indexOf(asset);
        int value = this.numbersOfAssets.get(index);

        if (value - number == 0) {
            assets.remove(index);
            numbersOfAssets.remove(index);
        } else if (value - number > 0) {
            numbersOfAssets.set(index, value - number);
        } else {
            throw new BuyingException("Negative number of asset");
        }

        LOGGER.info("{} of {} was removed from briefcase.", new Object[]{number, asset.getName()});
    }

    public ArrayList<Asset> getAssets() {
        return this.assets;
    }

    public ObservableList<Asset> getAssetsObservableArrayList() {
        return FXCollections.observableArrayList(this.assets);
    }

    public void setAssets(ArrayList<Asset> assets) {
        this.assets = assets;
    }

    public ArrayList<Integer> getNumbersOfAssets() {
        return this.numbersOfAssets;
    }

    public ObservableList<Integer> getNumbersOfAssetsObservableArrayList() {
        return FXCollections.observableArrayList(this.numbersOfAssets);
    }

    public void setNumbersOfAssets(ArrayList<Integer> numbersOfAssets) {
        this.numbersOfAssets = numbersOfAssets;
    }
}
