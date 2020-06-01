package org.example.marketstock.models.company;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.marketstock.models.asset.Share;
import org.example.marketstock.models.exchange.StockExchange;

/**
 *
 * @author Dominik
 * @since 1.0.0
 */
public class Company implements Serializable, Runnable {

    private static final Logger LOGGER = LogManager.getLogger(Company.class);
    private String name;
    private String dateOfFirstValuation;
    private double openingQuotation;
    private double currentRate;
    private double minRate;
    private double maxRate;
    private volatile int numberOfAssets;
    private volatile double profit;
    private volatile double revenue;
    private double equityCapital;
    private double openingCapital;
    private int volume;
    private double turnover;
    private volatile Share share;
    private StockExchange stock;
    private transient volatile long threadId;
    private transient volatile Boolean active = true;

    @Override
    public String toString() {
        return "Company " + this.name + " in " + this.stock.getName();
    }
    
    @Deprecated
    public Company() {
        this.name = "name";
        this.dateOfFirstValuation = "data";
        this.openingQuotation = 0.0;
        this.currentRate = 0.0;
        this.minRate = 0.0;
        this.maxRate = 0.0;
        this.numberOfAssets = 0;
        this.profit = 0.0;
        this.revenue = 0.0;
        this.openingCapital = 0.0;
        this.equityCapital = 0.0;
        this.volume = 0;
        this.turnover = 0.0;
        this.stock = new StockExchange();
        this.share = new Share();

        LOGGER.warn("Company used a deprecated constructor.");
    }
    
    @Override
    public void run () {
        LOGGER.info("Company {} starts working.", this.name);
        try {
            Random rand = new Random();

            while(this.active) {
                if(!this.active) break;
                Thread.sleep(rand.nextInt(14000) + 1000);

                this.changeRevenueAndProfit();

                if(!this.active) break;
                Thread.sleep(15000);

                this.issueShares();
            }
        } catch (InterruptedException exception) {
            this.active = false;
            LOGGER.warn("InterruptedException in {}", this.name);
            LOGGER.error(exception.getMessage(), exception);
        }            
    }
    
    public void terminate () {
        this.active = false;
    }

    @Deprecated
    public void initialize(StockExchange stockExchange) {
        this.drawName();

        Random rand = new Random();

        int day = rand.nextInt(28);
        int month = rand.nextInt(12);
        int year = rand.nextInt(2017);
        this.dateOfFirstValuation = year + "." + month + "." + day;

        double lowerBoundary;
        double upperBoundary;

        lowerBoundary = 1.0;
        upperBoundary = 7.0;
        double openingQuotation = lowerBoundary + (upperBoundary - lowerBoundary) * rand.nextDouble();

        this.openingQuotation = openingQuotation;
        this.currentRate = openingQuotation;
        this.minRate = 0.0;
        this.maxRate = 0.0;
        this.numberOfAssets = rand.nextInt(1000000) + 1000;

        lowerBoundary = 100000000.0;
        upperBoundary = 999999999.0;
        this.revenue = lowerBoundary + (upperBoundary - lowerBoundary) * rand.nextDouble();

        lowerBoundary = 0.005;
        upperBoundary = 0.25;
        this.profit = this.revenue * lowerBoundary + (upperBoundary - lowerBoundary) * rand.nextDouble();

        lowerBoundary = 100000.0;
        upperBoundary = 9999999999.0;
        this.equityCapital = lowerBoundary + (upperBoundary - lowerBoundary) * rand.nextDouble();

        lowerBoundary = 50000.0;
        upperBoundary = 9999999999.0;
        this.openingCapital = lowerBoundary + (upperBoundary - lowerBoundary) * rand.nextDouble();

        this.turnover = 0.0;
        this.volume = 0;

        this.stock = stockExchange;
        this.getShare().initialize(this.getName(), this.getOpeningQuotation(), this);
    }

    @Deprecated
    public void drawName() {
        Random rand = new Random();
        String [] namesStringArray = new String [] {"Randy Stodola","Ross the Boss",
            "New York Dolls","The Electric Eels","Screeching Weasels",
            "Dead Kennedys","Boris the Spider and Friends","Demons, Pills, Etc.",
            "Chemical Borthers","Black Dog Inc","Moby Dick and Bonzo Inc.",
            "Rocket Queen","Jesus Chrysler Inc.","The Sleeping Martyr",
            "Africa Unite","The White Riot","Girls and Boys","The Last Candle",
            "Odin ans Sons Inc.","Children of the Grave","Neon Knights",
            "Devil and Daughters","The Gates of Hell","Maxwell's Silver Hammer Inc.",
            "Keep It In the Family","Belly of the Beast","Angry Chair","Big Balls",
            "Rats in the Cellar","Kings and Queens"};

        ArrayList<String> namesArrayList = new ArrayList<>(Arrays.asList(namesStringArray));
        this.name = namesArrayList.get(rand.nextInt(namesArrayList.size()));
    }
        
    /**
     * Company updates rate of their shares and increases turnover and volume.
     * @param turnover A double variable which is going to be added to turnover.
     * @param volume An integer variable which is going to be added to volume.
     */
    public synchronized void updateCurrentRate(double turnover, int volume) {
        this.currentRate = this.share.getCurrentRate();
        this.maxRate = this.share.getMaxRate();
        this.minRate = this.share.getMinRate();

        this.turnover += turnover;
        LOGGER.info("Company {} added {} to it's turnover which now equals to {}",
                new Object[]{this.name, turnover, this.turnover});

        this.volume += volume;
        LOGGER.info("Company {} added {} to it's volume which now equals to {}",
                new Object[]{this.name, volume, this.volume});
    }
    
    /**
     * Company increases or diminishes revenue and profit.
     */
    public synchronized void changeRevenueAndProfit() {
        Random rand = new Random();
        double lowerBoundary = 0.01;
        double upperBoundary = 0.2;
        double percent = lowerBoundary + (upperBoundary - lowerBoundary) * rand.nextDouble();

        if(rand.nextInt(2) == 0) {
            double value = this.getRevenue() - this.getRevenue() * percent;
            this.revenue = value;

            LOGGER.info("{} was subtracted from {}'s revenue.", new Object[]{value, this.name});
        } else {
            double value = this.getRevenue() + this.getRevenue() * percent;
            this.revenue = value;

            LOGGER.info("{} was added to {}'s revenue.", new Object[]{value, this.name});
        }

        if(rand.nextInt(2) == 0) {
            double value = this.getProfit() - this.getProfit() * percent;
            this.profit = value;

            LOGGER.info("{} was subtracted from {}'s profit.", new Object[]{value, this.name});
        } else {
            double value = this.getProfit() - this.getProfit() * percent;
            this.profit = value;

            LOGGER.info("{} was added to {}'s profit.", new Object[]{value, this.name});
        }
    }
    
    /**
     * Company issues a random number of shares.
     */
    public synchronized void issueShares() {
        Random rand = new Random();
        int number = rand.nextInt(100000) + 100;

        this.numberOfAssets += number;
        LOGGER.info("Company {} issued {} new shares.", new Object[]{this.name, number});
    }

    public StringProperty nameProperty() {
        return new SimpleStringProperty(name);
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setDateOfFirstValuation(String dateOfFirstValuation) {
        this.dateOfFirstValuation = dateOfFirstValuation;
    }

    public String getDateOfFirstValuation() {
        return this.dateOfFirstValuation;
    }

    public void setOpeningQuotation(double openingQuotation) {
        this.openingQuotation = openingQuotation;
    }

    public double getOpeningQuotation() {
        return this.openingQuotation;
    }

    public void setCurrentRate(double currentRate) {
        this.currentRate = currentRate;
    }

    public double getCurrentRate() {
        return this.currentRate;
    }

    public void setMinRate(double minRate) {
        this.minRate = minRate;
    }

    public double getMinRate() {
        return this.minRate;
    }

    public void setMaxRate(double maxRate) {
        this.maxRate = maxRate ;
    }

    public double getMaxRate() {
        return this.maxRate;
    }

    public void setNumberOfAssets(int numberOfAssets) {
        this.numberOfAssets = numberOfAssets;
    }

    public int getNumberOfAssets() {
        return this.numberOfAssets;
    }

    public void setProfit(double profit) {
        this.profit = profit;
    }

    public double getProfit() {
        return this.profit;
    }

    public void setRevenue(double revenue) {
        this.revenue = revenue;
    }

    public double getRevenue() {
        return this.revenue;
    }

    public void setEquityCapital(double equityCapital) {
        this.equityCapital = equityCapital;
    }

    public double getEquityCapital() {
        return this.equityCapital;
    }

    public void setOpeningCapital(double openingCapital) {
        this.openingCapital = openingCapital;
    }

    public double getOpeningCapital() {
        return this.openingCapital;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public int getVolume() {
        return this.volume;
    }

    public void setTurnover(double turnover) {
        this.turnover = turnover;
    }

    public double getTurnover() {
        return this.turnover;
    }

    public Share getShare() {
        return this.share;
    }

    public void setShare(Share share) {
        this.share = share;
    }

    public StockExchange getStock() {
        return stock;
    }

    public void setStock(StockExchange stock) {
        this.stock = stock;
    }

    public long getThreadId() {
        return threadId;
    }

    public void setThreadId(long threadId) {
        this.threadId = threadId;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}