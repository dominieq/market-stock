package org.example.marketstock.models.entity;

/**
 *
 * TODO Player should be a singleton
 * @author Dominik Szmyt
 * @since 1.0.0
 */
public class Player extends Entity {
    
    @Deprecated
    public Player() {}

    @Deprecated
    public void createPlayer(String firstName, String lastName, Double budget) {
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setBudget(budget);
        this.setMarketApp(null);
    }   
}
