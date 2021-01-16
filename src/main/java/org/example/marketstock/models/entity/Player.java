package org.example.marketstock.models.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.example.marketstock.models.briefcase.Briefcase;

/**
 * @author Dominik Szmyt
 * @since 1.0.0
 */
public class Player extends AbstractInvestor {

    @JsonIgnore
    private static Player PLAYER;

    private Player(final String firstName,
                   final String lastName,
                   final double budget,
                   final Briefcase briefcase) {

        super(firstName, lastName, budget, briefcase);
    }

    public static Player getInstance(String firstName, String lastName, double budget, Briefcase briefcase) {
        if (PLAYER == null) {
            PLAYER = new Player(firstName, lastName, budget, briefcase);
        }

        return PLAYER;
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }
}
