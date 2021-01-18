package org.example.marketstock.models.company;

import java.io.Serializable;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.marketstock.models.asset.CountableAsset;
import org.example.marketstock.models.company.builder.CompanyBuilder;

/**
 *
 * @author Dominik
 * @since 1.0.0
 */
@JsonDeserialize(builder = CompanyBuilder.class)
public class Company extends CountableAsset implements Serializable, Runnable {

    private static final Logger LOGGER = LogManager.getLogger(Company.class);

    private final String dateOfFirstValuation;
    private final double openingQuotation;
    private double profit;
    private double revenue;
    private final double equityCapital;
    private final double openingCapital;
    private volatile int volume;
    private volatile double turnover;

    @JsonIgnore
    private volatile transient boolean active = true;

    @JsonIgnore
    private volatile transient boolean terminated = false;

    public Company(final String name,
                   final double currentRate,
                   final double minRate,
                   final double maxRate,
                   final List<Double> rateChanges,
                   final double margin,
                   final int numberOfAssets,
                   final String dateOfFirstValuation,
                   final double openingQuotation,
                   final double profit,
                   final double revenue,
                   final double equityCapital,
                   final double openingCapital,
                   final int volume,
                   final double turnover) {

        super(name, currentRate, minRate, maxRate, margin, rateChanges, numberOfAssets);

        this.dateOfFirstValuation = dateOfFirstValuation;
        this.openingQuotation = openingQuotation;
        this.profit = profit;
        this.revenue = revenue;
        this.equityCapital = equityCapital;
        this.openingCapital = openingCapital;
        this.volume = volume;
        this.turnover = turnover;
    }
    
    @Override
    public void run () {
        LOGGER.debug("[THREAD]: Company {} starts.", this);

        try {
            while(active) {
                sleep();
                if(!active) break;

                updateRevenueAndProfit();

                sleep();
                if(!active) break;

                issueAssets();
            }
        } catch (InterruptedException exception) {
            active = false;
            LOGGER.debug("[THREAD]: Company {} stopped with InterruptedException.", this);
        }

        terminated = true;
        LOGGER.debug("[THREAD]: Company {} stops.", this);
    }
    
    public void terminate () {
        this.active = false;
    }

    private void sleep() throws InterruptedException {
        final Random random = new Random();
        final int timeout = random.nextInt(15) + 1;

        LOGGER.debug("[THREAD]: Company {} sleeps for {}.", this, timeout);

        TimeUnit.SECONDS.sleep(timeout);
    }

    /**
     * Company updates rate of their shares and increases turnover and volume.
     * @param turnover1 A double variable which is going to be added to turnover.
     * @param volume1 An integer variable which is going to be added to volume.
     */
    public synchronized void updateTurnoverAndVolume(double turnover1, int volume1) {
        final double INITIAL_TURNOVER = turnover;
        turnover += turnover1;
        LOGGER.info("[COMPANY]: Turnover changes from {} to {} in {}.", INITIAL_TURNOVER, turnover, this);

        final double INITIAL_VOLUME = volume;
        volume += volume1;
        LOGGER.info("[COMPANY]: Volume changes from {} to {} in {}. ", INITIAL_VOLUME, volume, this);
    }
    
    /**
     * Company increases or diminishes revenue and profit.
     */
    public void updateRevenueAndProfit() {
        final Random random = new Random();
        final double LOWER_BOUNDARY = 0.01;
        final double UPPER_BOUNDARY = 0.2;
        double PERCENT = LOWER_BOUNDARY + (UPPER_BOUNDARY - LOWER_BOUNDARY) * random.nextDouble();

        final double INITIAL_REVENUE = revenue;
        if (random.nextInt(2) == 0) {
            revenue -= revenue * PERCENT;
            LOGGER.info("[COMPANY]: Revenue decreases from {} to {} in {}.", INITIAL_REVENUE, revenue, this);
        } else {
            revenue += revenue * PERCENT;
            LOGGER.info("[COMPANY]: Revenue increases from {} to {} in {}.", INITIAL_REVENUE, revenue, this);
        }

        final double INITIAL_PROFIT = profit;
        if (random.nextInt(2) == 0) {
            profit -= profit * PERCENT;
            LOGGER.info("[COMPANY]: Profit decreases from {} to {} in {}.", INITIAL_PROFIT, profit, this);
        } else {
            profit += profit * PERCENT;
            LOGGER.info("[COMPANY]: Profit increases from {} to {} in {}.", INITIAL_PROFIT, profit, this);
        }
    }
    
    /**
     * Company issues a random number of shares.
     */
    public void issueAssets() {
        final Random rand = new Random();
        final int number = rand.nextInt(99_901) + 100;

        increaseNumberOfAssets(number);
        LOGGER.info("[COMPANY]: {} new shares issued by {}.", number, this);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("name", name)
                .add("currentRate", currentRate)
                .add("minRate", minRate)
                .add("maxRate", maxRate)
                .add("margin", margin)
                .add("rateChanges", rateChanges)
                .add("numberOfAssets", numberOfAssets)
                .add("dateOfFirstValuation", dateOfFirstValuation)
                .add("openingQuotation", openingQuotation)
                .add("profit", profit)
                .add("revenue", revenue)
                .add("equityCapital", equityCapital)
                .add("openingCapital", openingCapital)
                .add("volume", volume)
                .add("turnover", turnover)
                .add("active", active)
                .add("terminated", terminated)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Company)) return false;
        if (!super.equals(o)) return false;
        Company company = (Company) o;
        return Objects.equal(dateOfFirstValuation, company.dateOfFirstValuation) &&
                Double.compare(company.openingQuotation, openingQuotation) == 0 &&
                Double.compare(company.equityCapital, equityCapital) == 0 &&
                Double.compare(company.openingCapital, openingCapital) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(super.hashCode(), dateOfFirstValuation, openingQuotation, equityCapital, openingCapital);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public double getCurrentRate() {
        return this.currentRate;
    }

    @Override
    public double getMinRate() {
        return this.minRate;
    }

    @Override
    public double getMaxRate() {
        return this.maxRate;
    }

    @Override
    public List<Double> getRateChanges() {
        return rateChanges;
    }

    @Override
    public double getMargin() {
        return margin;
    }

    @Override
    public int getNumberOfAssets() {
        return numberOfAssets;
    }

    public String getDateOfFirstValuation() {
        return this.dateOfFirstValuation;
    }

    public double getOpeningQuotation() {
        return this.openingQuotation;
    }

    public double getProfit() {
        return this.profit;
    }

    public double getRevenue() {
        return this.revenue;
    }

    public double getEquityCapital() {
        return this.equityCapital;
    }

    public double getOpeningCapital() {
        return this.openingCapital;
    }

    public int getVolume() {
        return this.volume;
    }

    public double getTurnover() {
        return this.turnover;
    }

    public boolean isActive() {
        return active;
    }

    public boolean isTerminated() {
        return terminated;
    }
}
