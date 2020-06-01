package org.example.marketstock.models.entity;

import org.example.marketstock.app.MarketApp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.example.marketstock.models.asset.InvestmentUnit;

/**
 *
 * @author Dominik
 * @since 1.0.0
 */
public class InvestmentFund extends Entity implements Serializable, Runnable {

    private String name;
    private volatile double margin;
    private volatile int numberOfInvestmentUnits;
    private volatile InvestmentUnit investmentUnit;
    private transient volatile Boolean active = true;

    @Override
    public String toString() {
        return this.name + " with budget " + this.getBudget();
    }

    @Deprecated
    public InvestmentFund() {
        this.name = "name";
        this.numberOfInvestmentUnits = 0;
        this.margin = 0.0;
        this.investmentUnit = new InvestmentUnit();

        LOGGER.warn("Investment fund used a deprecated constructor.");
    }

    @Override
    public void run () {
        LOGGER.info("Investment fund {} starts working.", this.name);

        try {
            Random rand = new Random();

            while (this.active) {
                if(!this.active) break;
                Thread.sleep(rand.nextInt(14000) + 10000);

                if(rand.nextInt(1) == 0) {
                    this.chooseAndBuyAsset();
                } else {
                    this.chooseAndSellAsset();
                }

                if(!this.active) break;
                Thread.sleep(rand.nextInt(14000) + 10000);

                this.increaseBudget();
                this.issueInvestmentUnits();
            }  
        }catch (InterruptedException exception) {
            this.active = false;

            LOGGER.warn("InterruptedException in {}.", this.name);
            LOGGER.error(exception.getMessage(), exception);
        }
    }

    /**
     * Terminates a 'run' method in InvestmentFund.
     */
    public void terminate () {
        this.active = false;
    }
    
    /**
     * Investment fund draws a number of new investment units
     * and adds it to the current number of investment units.
     */
    public synchronized void issueInvestmentUnits() {
        Random rand = new Random();
        int numberOfNewInvestmentUnits = rand.nextInt(100000) + 100;
        this.numberOfInvestmentUnits += numberOfNewInvestmentUnits;

        LOGGER.info("Investment fund {} issued {} new investment units.",
                new Object[]{this.name, numberOfNewInvestmentUnits});
    }
    
    @Deprecated
    public void initialize(MarketApp marketApp) {
        this.drawAndSetName();
        this.drawBudget();

        Random rand = new Random();

        String [] nameStringArray = new String [] {"Kupuj Bezpiecznie","Strength and Honour",
            "Brothers and Sisters","Fifty-fifty","Ludzie niepowszedni",
            "Fool's Paradise"};


        ArrayList<String> nameArrayList = new ArrayList<>(Arrays.asList(nameStringArray));
        this.setName(nameArrayList.get( rand.nextInt(nameArrayList.size()) ));

        double upperBoundary = 10.0;
        double lowerBoundary = 1.0;
        double margin = lowerBoundary + (upperBoundary - lowerBoundary) * rand.nextDouble();

        this.setMargin(margin);

        this.setNumberOfInvestmentUnits(rand.nextInt(1000000) + 1000);

        this.getInvestmentUnit().initialize(this.getName(), this);
        this.setMarketApp(marketApp);
    }

    public String getName() {
        return this.name;
    }

    public StringProperty getNameProperty() {
        return new SimpleStringProperty(this.name);
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumberOfInvestmentUnits() {
        return this.numberOfInvestmentUnits;
    }

    public void setNumberOfInvestmentUnits(int numberOfInvestmentUnits) {
        this.numberOfInvestmentUnits = numberOfInvestmentUnits;
    }

    public double getMargin() {
        return margin;
    }

    public void setMargin(double margin) {
        this.margin = margin;
    }

    public InvestmentUnit getInvestmentUnit() {
        return this.investmentUnit;
    }

    public void setInvestmentUnit(InvestmentUnit investmentUnit) {
        this.investmentUnit = investmentUnit;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
