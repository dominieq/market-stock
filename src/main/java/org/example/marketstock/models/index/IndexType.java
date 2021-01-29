package org.example.marketstock.models.index;

/**
 * The unique type of an {@link Index}.
 *
 * @since 1.0.0
 * @author Dominik Szmyt
 */
public enum IndexType {

    MAX("MAX"),
    MIN("MIN");

    private final String value;

    /**
     * Create an {@code IndexType} from a value.
     * @param value The value of an index type.
     */
    IndexType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
