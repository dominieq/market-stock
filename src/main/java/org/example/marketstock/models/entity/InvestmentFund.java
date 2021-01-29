package org.example.marketstock.models.entity;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import io.vavr.Tuple3;
import org.example.marketstock.models.asset.Countable;
import org.example.marketstock.models.briefcase.Briefcase;
import org.example.marketstock.models.asset.Asset;
import org.example.marketstock.models.entity.builder.InvestmentFundBuilder;
import org.example.marketstock.simulation.Simulation;

/**
 * Represents a real life investment fund which buys and sells assets, apart from issuing it's own investment units.
 * An investment fund randomly increases it's budget what simulates external earnings.
 *
 * @author Dominik Szmyt
 * @since 1.0.0
 */
@JsonDeserialize(builder = InvestmentFundBuilder.class)
public class InvestmentFund extends AbstractEntity implements Asset, Countable, Serializable, Runnable {

    protected final Object NUMBER_OF_ASSET = new Object();

    private final String name;
    private volatile double currentRate;
    private double minRate;
    private double maxRate;
    private final List<Double> rateChanges;
    private final double margin;
    private volatile int numberOfAssets;

    @JsonIgnore
    private final transient Simulation simulation;

    @JsonIgnore
    private volatile transient boolean active = true;

    @JsonIgnore
    private volatile transient boolean terminated = false;

    /**
     * Create an {@code InvestmentFund} with all necessary fields.
     * @param name The name of an {@code InvestmentFund}
     * @param currentRate The current rate of an {@code InvestmentFund}.
     * @param minRate The minimum rate of an {@code InvestmentFund}.
     * @param maxRate The maximum rate of an {@code InvestmentFund}.
     * @param rateChanges The list of rate changes of {@code InvestmentFund}.
     * @param margin The margin of an {@code InvestmentFund}.
     * @param numberOfAssets The number of assets issued by an {@code InvestmentFund}.
     * @param budget The budget of an {@code InvestmentFund}.
     * @param briefcase A briefcase that belongs to an {@code InvestmentFund}.
     * @param simulation An instance of a {@code Simulation} that allows an {@code InvestmentFund} to perform transactions.
     */
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
        LOGGER.debug("[THREAD]: Investment fund {} starts.", this);

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
            LOGGER.debug("[THREAD]: Investment fund {} stopped with InterruptedException.", this);
        }

        terminated = true;
        LOGGER.debug("[THREAD]: Investment fund {} stops", this);
    }

    /**
     * Terminates an investment fund's thread by ending it's {@code while} loop.
     */
    public void terminate () {
        active = false;
    }

    /**
     * Sleeps for a random amount of time between 10 and 15 seconds.
     * @throws InterruptedException If any thread interrupted the current thread while the current thread was sleeping.
     */
    private void sleep() throws InterruptedException {
        final Random random = new Random();
        final int timeout = random.nextInt(6) + 10;

        LOGGER.debug("[THREAD]: Investment fund sleeps for {} seconds.", timeout);

        TimeUnit.SECONDS.sleep(timeout);
    }
    
    /**
     * Increases the number of available investment units by random number.
     */
    protected void issueAssets() {
        final Random rand = new Random();
        final int number = rand.nextInt(100_000) + 100;

        increaseNumberOfAssets(number);
        LOGGER.info("[INVESTMENT_FUND]: {} new investment units issued by {}.", number, this);
    }

    /**
     * At first, replaces old value with the new one and adds the new rate to rate changes.
     * In the end, calculates min and max rate.
     * <br>
     * <b>NOTE:</b> this implementation keeps track of only 10 rate changes.
     *
     * @param rate A new rate that is to replace the current rate.
     * @return A new value of current rate.
     */
    @Override
    public double updateRate(double rate) {
        LOGGER.info("[ASSET]: Rate changes from {} to {} in {}.", currentRate, rate, this);

        currentRate = rate;
        rateChanges.add(rate);

        if (rateChanges.size() >= 10) {
            rateChanges.remove(0);
        }

        maxRate = Collections.max(rateChanges);
        minRate = Collections.min(rateChanges);

        return currentRate;
    }

    /**
     * Increases number of assets by a given value.
     * @param addend A value that is to be added to the number of assets.
     * @return The number of assets after addition.
     */
    @Override
    public double increaseNumberOfAssets(int addend) {
        synchronized (NUMBER_OF_ASSET) {
            final int sum = numberOfAssets + addend;
            LOGGER.info("[COUNTABLE]: Number increases from {} to {} in {}.", numberOfAssets, sum, this);
            numberOfAssets = sum;
            return numberOfAssets;
        }
    }

    /**
     * Decreases number of assets by a given value if the value isn't greater than the original number.
     * @param subtrahend A value that is to be subtracted from the number of assets.
     * @return The number of assets after possible subtraction.
     */
    @Override
    public double decreaseNumberOfAssets(int subtrahend) {
        synchronized (NUMBER_OF_ASSET) {
            if (numberOfAssets - subtrahend < 0) {
                LOGGER.warn("[COUNTABLE]: Aborting decrease in {} because negative difference.", this);
                return numberOfAssets;
            }

            final int difference = numberOfAssets - subtrahend;
            LOGGER.info("[COUNTABLE]: Number decreases from {} to {} in {}.", numberOfAssets, difference, this);
            numberOfAssets = difference;
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
