package org.example.marketstock.models.entity;

import com.google.common.base.Objects;
import org.example.marketstock.models.briefcase.Briefcase;

public abstract class AbstractInvestor extends AbstractEntity {

    protected String firstName;
    protected String lastName;

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
