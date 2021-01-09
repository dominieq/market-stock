package org.example.marketstock.models.index;

public enum IndexType {

    MAX("MAX"),
    MIN("MIN");

    private final String value;

    IndexType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
