package org.example.marketstock.models.entity;

import com.google.common.base.Objects;
import org.example.marketstock.models.briefcase.Briefcase;

/**
 * Represents the concept of a real life investor whose sole purpose is to buy and sell assets.
 *
 * @author Dominik Szmyt
 * @since 1.0.0
 */
public abstract class AbstractInvestor extends AbstractEntity {

    protected String firstName;
    protected String lastName;

    /**
     * Create an {@code AbstractInvestor} with all necessary fields.
     * @param firstName1 The first name of an {@code AbstractInvestor}.
     * @param lastName1 The last name of an {@code AbstractInvestor}.
     * @param budget1 The budget of an {@code AbstractInvestor}.
     * @param briefcase1 A {@code Briefcase} that belongs to an {@code AbstractInvestor}.
     */
    public AbstractInvestor(final String firstName1,
                            final String lastName1,
                            final double budget1,
                            final Briefcase briefcase1) {

        super(budget1, briefcase1);

        firstName = firstName1;
        lastName = lastName1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractInvestor)) return false;
        AbstractInvestor that = (AbstractInvestor) o;
        return Objects.equal(firstName, that.firstName) &&
                Objects.equal(lastName, that.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(firstName, lastName);
    }

    abstract public String getFirstName();
    abstract public String getLastName();
}
