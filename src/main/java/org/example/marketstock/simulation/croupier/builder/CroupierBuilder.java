package org.example.marketstock.simulation.croupier.builder;

import org.example.marketstock.simulation.croupier.Croupier;
import org.example.marketstock.simulation.json.JsonReader;

import java.util.Random;

import static java.util.Objects.isNull;

public final class CroupierBuilder {

    private JsonReader jsonReader;
    private Random random;

    private CroupierBuilder() { }

    public static CroupierBuilder builder() {
        return new CroupierBuilder();
    }

    public CroupierBuilder from(final Croupier croupier) {
        if (isNull(croupier)) {
            return this;
        }

        this.jsonReader = croupier.getJsonReader();
        this.random = croupier.getRandom();
        return this;
    }

    public CroupierBuilder withJsonReader(final JsonReader jsonReader) {
        this.jsonReader = jsonReader;
        return this;
    }

    public CroupierBuilder withRandom(final Random random) {
        this.random = random;
        return this;
    }

    public Croupier build() {
        return new Croupier(jsonReader, random);
    }
}
