package org.example.marketstock.models.entity;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import io.vavr.Tuple3;
import org.example.marketstock.models.asset.Countable;
import org.example.marketstock.models.briefcase.Briefcase;
import org.example.marketstock.models.asset.Asset;
import org.example.marketstock.simulation.Simulation;

/**
 *
 * @author Dominik
 * @since 1.0.0
 */
public class InvestmentFund extends AbstractEntity implements Asset, Countable, Serializable, Runnable {

    protected final Object NUMBER_OF_ASSET = new Object();

    private final String name;
    private volatile double currentRate;
    private double minRate;
    private double maxRate;
    private final List<Double> rateChanges;
    private final double margin;
    private volatile int numberOfAssets;
    private final transient Simulation simulation;
    private volatile transient boolean active = true;
    private volatile transient boolean terminated = false;

    public InvestmentFund(final String name,
                          final double currentRate,
                          final double minRate,
                          final double maxRate,
                          final List<Double> rateChanges,
                          final double margin,
                          final int numberOfAssets,
                          final double budget,
                          final Briefcase briefcase,
                          final Simulation simulation) {

        super(budget, briefcase);

        this.name = name;
        this.currentRate = currentRate;
        this.minRate = minRate;
        this.maxRate = maxRate;
        this.rateChanges = rateChanges;
        this.margin = margin;
        this.numberOfAssets = numberOfAssets;
        this.simulation = simulation;
    }

    @Override
    public void run () {
        LOGGER.debug("Investment fund {} starts.", this);

        try {
            final Random rand = new Random();

            while (active) {
                sleep();
                if(!active) break;

                final int coinFlip = rand.nextInt(2);

                if(coinFlip == 0 || briefcase.isEmpty()) {
                    final Optional<Tuple3<Asset, Integer, Double>> selection = simulation.chooseAssetToBuy(this);

                    if (selection.isPresent()) {
                        final Tuple3<Asset, Integer, Double> tuple = selection.get();
                        simulation.buySelectedResource(tuple._1, tuple._2,  tuple._3, this);
                    }
                } else if (!briefcase.isEmpty()) {
                    final Optional<Tuple3<Asset, Integer, Double>> selection = simulation.chooseAssetToSell(this);

                    if (selection.isPresent()) {
                        final Tuple3<Asset, Integer, Double> tuple = selection.get();
                        simulation.sellSelectedResource(tuple._1, tuple._2, this);
                    }
                }

                sleep();
                if(!active) break;

                increaseBudget();
                issueAssets();
            }  
        } catch (InterruptedException exception) {
            active = false;
            LOGGER.debug("Investment fund {} stopped with InterruptedException.", this);
        }

        terminated = true;
        LOGGER.debug("Investment fund {} stops", this);
    }

    public void terminate () {
        active = false;
    }

    private void sleep() throws InterruptedException {
        final Random random = new Random();
        final int timeout = random.nextInt(6) + 10;

        LOGGER.debug("Investment fund sleeps for {} seconds.", timeout);

        TimeUnit.SECONDS.sleep(timeout);
    }
    
    /**
     * Investment fund draws a number of new investment units
     * and adds it to the current number of investment units.
     */
    protected void issueAssets() {
        Random rand = new Random();
        int numberOfNewInvestmentUnits = rand.nextInt(100000) + 100;

        increaseNumberOfAssets(numberOfNewInvestmentUnits);

        LOGGER.info("Investment fund {} issued {} new investment units.",
                new Object[]{this.name, numberOfNewInvestmentUnits});
    }

    @Override
    public double updateRate(double rate) {
        currentRate = rate;
        rateChanges.add(rate);

        if (rateChanges.size() >= 10) {
            rateChanges.remove(0);
        }

        maxRate = Collections.max(rateChanges);
        minRate = Collections.min(rateChanges);

        return currentRate;
    }

    @Override
    public double increaseNumberOfAssets(int addend) {
        synchronized (NUMBER_OF_ASSET) {
            numberOfAssets += addend;
            return numberOfAssets;
        }
    }

    @Override
    public double decreaseNumberOfAssets(int subtrahend) {
        synchronized (NUMBER_OF_ASSET) {
            if (numberOfAssets - subtrahend < 0) {
                return numberOfAssets;
            }

            numberOfAssets -= subtrahend;
            return numberOfAssets;
        }
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("name", name)
                .add("currentRate", currentRate)
                .add("minRate", minRate)
                .add("maxRate", maxRate)
                .add("rateChanges", rateChanges)
                .add("margin", margin)
                .add("numberOfAssets", numberOfAssets)
                .add("budget", budget)
                .add("briefcase", briefcase)
                .add("active", active)
                .add("terminated", terminated)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InvestmentFund)) return false;
        InvestmentFund that = (InvestmentFund) o;
        return Objects.equal(name, that.name) &&
                Double.compare(margin, that.margin) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name, margin);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public double getCurrentRate() {
        return currentRate;
    }

    @Override
    public double getMinRate() {
        return minRate;
    }

    @Override
    public double getMaxRate() {
        return maxRate;
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
        return this.numberOfAssets;
    }

    public Simulation getSimulation() {
        return simulation;
    }

    public boolean isActive() {
        return active;
    }

    public boolean isTerminated() {
        return terminated;
    }
}
