package org.example.marketstock.models.entity;

import org.example.marketstock.models.briefcase.Briefcase;

/**
 * @author Dominik Szmyt
 * @since 1.0.0
 */
public class Player extends Entity {

    private static Player PLAYER;

    private Player(String firstName, String lastName, double budget) {
        super(firstName, lastName, budget);
    }

    private Player(String firstName, String lastName, double budget, Briefcase briefcase) {
        super(firstName, lastName, budget, briefcase);
    }

    public static Player getInstance(String firstName, String lastName, double budget) {
        if (PLAYER != null) {
            throw new IllegalStateException("Player already constructed.");
        }

        PLAYER = new Player(firstName, lastName, budget);
        return PLAYER;
    }

    public static Player getInstance(String firstName, String lastName, double budget, Briefcase briefcase) {
        if (PLAYER != null) {
            throw new IllegalStateException("Player already constructed.");
        }

        PLAYER = new Player(firstName, lastName, budget, briefcase);
        return PLAYER;
    }
}
