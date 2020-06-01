package org.example.marketstock.models.entity;

import org.example.marketstock.app.MarketApp;

import java.io.Serializable;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author Dominik
 * @since 1.0.0
 */
public class Investor extends Entity implements Serializable, Runnable {

    private String PESEL;
    private transient volatile Boolean active = true;

    @Deprecated
    public Investor() {
        this.PESEL = "11111111111";
    }

    @Override
    public void run () {
        LOGGER.info( "Investor {} {} starts working.", new Object[]{this.getFirstName(), this.getLastName()});

        try {
            Random rand = new Random();

            while (this.active) {
                if (!this.active) break;
                Thread.sleep(rand.nextInt(14000) + 10000);

                if (rand.nextInt(1) == 0) {
                    this.chooseAndBuyAsset();
                } else {
                    this.chooseAndSellAsset();
                }

                if (!this.active) break;
                Thread.sleep(rand.nextInt(14000) + 10000);

                this.increaseBudget();
            }  
        } catch (InterruptedException exception) {
            this.active = false;

            LOGGER.warn("InterruptedException in {} {}.", new Object[]{this.getFirstName(), this.getLastName()});
            LOGGER.error(exception.getMessage(), exception);
        }
    }

    /**
     * Terminates a 'run' method in Investor.
     */
    public void terminate () {
        this.active = false;
    }

    @Deprecated
    public void initialize(MarketApp marketApp) {
        this.drawAndSetName();
        this.setBudget(this.drawBudget());
        this.setPESEL(String.valueOf(this.drawPESEL()));
        this.setMarketApp(marketApp);
    }

    @Deprecated
    public Long drawPESEL() {
        String upperBoundary = "99999999999";
        String lowerBoundary= "11111111111";

        return ThreadLocalRandom.current().nextLong(Long.parseLong(lowerBoundary), Long.parseLong(upperBoundary));
    }    

    public void setPESEL(String PESEL) {
        this.PESEL = PESEL;
    }

    public String getPESEL() {
        return PESEL;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}