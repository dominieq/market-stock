package org.example.marketstock.models.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.example.marketstock.models.briefcase.Briefcase;
import org.example.marketstock.models.entity.serialization.PlayerDeserializer;

import static java.util.Objects.isNull;

/**
 * @author Dominik Szmyt
 * @since 1.0.0
 */
@JsonDeserialize(using = PlayerDeserializer.class)
public class Player extends AbstractInvestor {

    @JsonIgnore
    private static Player PLAYER;

    private Player(final String firstName,
                   final String lastName,
                   final double budget,
                   final Briefcase briefcase) {

        super(firstName, lastName, budget, briefcase);
    }

    public static Player getInstance(final String firstName,
                                     final String lastName,
                                     final double budget,
                                     final Briefcase briefcase) {

        if (isNull(PLAYER)) {
            PLAYER = new Player(firstName, lastName, budget, briefcase);
        }

        return PLAYER;
    }

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
