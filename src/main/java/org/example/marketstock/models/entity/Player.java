package org.example.marketstock.models.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.example.marketstock.models.briefcase.Briefcase;
import org.example.marketstock.models.entity.serialization.PlayerDeserializer;

import static java.util.Objects.isNull;

/**
 * Represents a {@code MarketStock} user that will perform transaction using provided GUI.
 *
 * @author Dominik Szmyt
 * @since 1.0.0
 */
@JsonDeserialize(using = PlayerDeserializer.class)
public class Player extends AbstractInvestor {

    @JsonIgnore
    private static Player PLAYER;

    /**
     * Create a {@code Player} with all necessary fields.
     * @param firstName The first name of a {@code Player}.
     * @param lastName The last name of a {@code PLayer}.
     * @param budget The budget of a {@code Player}.
     * @param briefcase A briefcase that belongs to a {@code Player}.
     */
    private Player(final String firstName,
                   final String lastName,
                   final double budget,
                   final Briefcase briefcase) {

        super(firstName, lastName, budget, briefcase);
    }

    /**
     * If an instance didn't exist, creates a new {@code Player}.
     * Otherwise, returns existing instance of a {@code Player}.
     *
     * @param firstName The first name of a {@code Player}.
     * @param lastName The last name of a {@code Player}.
     * @param budget The budget of a {@code Player}.
     * @param briefcase A briefcase that belongs to a {@code Player}.
     * @return If an instance didn't exist, returns a new {@code Player}.
     */
    public static Player getInstance(final String firstName,
                                     final String lastName,
                                     final double budget,
                                     final Briefcase briefcase) {

        if (isNull(PLAYER)) {
            PLAYER = new Player(firstName, lastName, budget, briefcase);
        }

        return PLAYER;
    }

    /**
     * Updates an existing instance with a new {@code Player}.
     *
     * @param firstName The first name of a {@code Player}.
     * @param lastName The last name of a {@code Player}.
     * @param budget The budget of a {@code Player}.
     * @param briefcase A briefcase that belongs to a {@code Player}.
     * @return Returns an updated instance of a {@code Player}.
     */
    public static Player updateInstance(final String firstName,
                                        final String lastName,
                                        final double budget,
                                        final Briefcase briefcase) {

        PLAYER = new Player(firstName, lastName, budget, briefcase);
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
