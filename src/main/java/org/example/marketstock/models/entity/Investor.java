package org.example.marketstock.models.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import io.vavr.Tuple3;
import javafx.util.Pair;
import org.example.marketstock.models.asset.Asset;
import org.example.marketstock.models.briefcase.Briefcase;
import org.example.marketstock.models.entity.builder.InvestorBuilder;
import org.example.marketstock.simulation.Simulation;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Dominik
 * @since 1.0.0
 */
@JsonDeserialize(builder = InvestorBuilder.class)
public class Investor extends AbstractInvestor implements Serializable, Runnable {

    private final String PESEL;

    @JsonIgnore
    private final transient Simulation simulation;

    @JsonIgnore
    private volatile transient boolean active = true;

    @JsonIgnore
    private volatile transient boolean terminated = false;

    public Investor(final String firstName,
                    final String lastName,
                    final double budget,
                    final Briefcase briefcase,
                    final String PESEL,
                    final Simulation simulation) {

        super(firstName, lastName, budget, briefcase);

        this.PESEL = PESEL;
        this.simulation = simulation;
    }

    @Override
    public void run () {
        LOGGER.info( "Investor {} starts.", this);

        try {
            Random rand = new Random();

            while (active) {
                sleep();
                if (!active) break;

                if (rand.nextInt(2) == 0) {
                    final Optional<Tuple3<Asset, Integer, Double>> selection = simulation.chooseAssetToBuy(this);

                    if (selection.isPresent()) {
                        final Tuple3<Asset, Integer, Double> tuple = selection.get();
                        simulation.buySelectedResource(tuple._1, tuple._2, tuple._3, this);
                    }
                } else {
                    final Optional<Tuple3<Asset, Integer, Double>> selection = simulation.chooseAssetToSell(this);

                    if (selection.isPresent()) {
                        final Tuple3<Asset, Integer, Double> tuple = selection.get();
                        simulation.sellSelectedResource(tuple._1, tuple._2, this);
                    }
                }

                sleep();
                if (!active) break;

                increaseBudget();
            }  
        } catch (InterruptedException exception) {
            active = false;
            LOGGER.warn("Investor {} stopped with InterruptedException.", this);
        }

        terminated = true;
        LOGGER.info("Investor {} stops.", this);
    }

    public void terminate () {
        active = false;
    }

    private void sleep() throws InterruptedException {
        final Random random = new Random();
        final int timeout = random.nextInt(6) + 10;

        LOGGER.info("Investor sleeps for {} seconds.", timeout);

        TimeUnit.SECONDS.sleep(timeout);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("firstName", firstName)
                .add("lastName", lastName)
                .add("PESEL", PESEL)
                .add("budget", budget)
                .add("briefcase", briefcase)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Investor)) return false;
        if (!super.equals(o)) return false;
        Investor investor = (Investor) o;
        return Objects.equal(PESEL, investor.PESEL);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(super.hashCode(), PESEL);
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    public String getPESEL() {
        return PESEL;
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
