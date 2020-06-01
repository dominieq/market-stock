package org.example.marketstock.models.asset;

import java.io.Serializable;
import java.util.ArrayList;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Dominik
 * @since 1.0.0
 */
public abstract class Asset implements Serializable {
    
    protected static Logger LOGGER = LogManager.getLogger(Asset.class);
    private String name;
    private volatile double currentRate;
    private volatile double minRate;
    private volatile double maxRate;
    private volatile double exchangeMargin;
    private ArrayList<Double> rateChangeArrayList;

    @Override
    public String toString() {
        return this.name + " with current rate " + this.currentRate;
    }

    @Deprecated
    public Asset() {
        this.name = "name";
        this.currentRate = 0.0;
        this.maxRate = 0.0;
        this.minRate = 0.0;
        this.exchangeMargin = 0.0;
        this.rateChangeArrayList = new ArrayList<>();

        LOGGER.warn("Asset used a deprecated constructor.");
    }
    
    /**
     * Updates rate changes for asset chart.
     * @param rate new rate value based on number of people buying
     */
    public synchronized void updateRate(Double rate) {
        LOGGER.info("{} changes old rate {} to new rate {}", new Object[]{this.name, this.currentRate, rate});

        if (this.getRateChangeArrayList().size() >= 10) {
            this.getRateChangeArrayList().remove(0);
        }

        this.rateChangeArrayList.add(rate);
        this.currentRate = rate;

        this.maxRate = this.findMaxRate();
        this.minRate = this.findMinRate();
    }
    
    /**
     * Finds maximum in rateObservableArray
     * @return max - maximum found in rateObservableArray
     */
    protected Double findMaxRate() {
        Double maxRate = Double.MIN_VALUE;

        for (Double rate : this.rateChangeArrayList) {
            if (rate > maxRate) {
                maxRate = rate;
            }
        }

        return maxRate;
    }
    
    /**
     * Finds minimum in rateObservableArray.
     * @return min - minimum found in rateObservableArray
     */
    protected Double findMinRate() {
        Double minRate = Double.MAX_VALUE;

        for (Double rate : this.rateChangeArrayList) {
            if (rate < minRate) {
                minRate = rate;
            }
        }

        return minRate;
    }

    public String getName() {
        return this.name;
    }

    public StringProperty getNameProperty() {
        return new SimpleStringProperty(name);
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCurrentRate() {
        return this.currentRate;
    }

    public void setCurrentRate(double currentRate) {
        this.currentRate = currentRate;
    }

    public double getMinRate() {
        return this.minRate;
    }

    public void setMinRate(double minRate) {
        this.minRate = minRate;
    }

    public double getMaxRate() {
        return this.maxRate;
    }

    public void setMaxRate(double maxRate) {
        this.maxRate = maxRate;
    }

    public double getExchangeMargin() {
        return exchangeMargin;
    }

    public void setExchangeMargin(double exchangeMargin) {
        this.exchangeMargin = exchangeMargin;
    }

    public ArrayList<Double> getRateChangeArrayList() {
        return this.rateChangeArrayList;
    }

    public ObservableList<Double> getRateChangeObservableArrayList() {
        return FXCollections.observableArrayList(this.rateChangeArrayList);
    }

    public void setRateChangeArrayList(ArrayList<Double> rateChangeArrayList) {
        this.rateChangeArrayList = rateChangeArrayList;
    }

    public void addRate(double rate) {
        this.rateChangeArrayList.add(rate);
    }

    public void deleteRate(double rate) {
        this.rateChangeArrayList.remove(rate);
    }
}
