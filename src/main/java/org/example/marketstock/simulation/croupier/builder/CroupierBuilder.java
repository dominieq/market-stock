package org.example.marketstock.simulation.croupier.builder;

import org.example.marketstock.simulation.croupier.Croupier;
import org.example.marketstock.simulation.json.ResourcesReader;

import java.util.Random;

import static java.util.Objects.isNull;

/**
 * A builder for the {@link Croupier} class.
 *
 * @author Dominik Szmyt
 * @see Croupier
 * @since 1.0.0
 */
public final class CroupierBuilder {

    private ResourcesReader resourcesReader;
    private Random random;

    private CroupierBuilder() { }

    public static CroupierBuilder builder() {
        return new CroupierBuilder();
    }

    public CroupierBuilder from(final Croupier croupier) {
        if (isNull(croupier)) {
            return this;
        }

        this.resourcesReader = croupier.getResourcesReader();
        this.random = croupier.getRandom();
        return this;
    }

    public CroupierBuilder withResourcesReader(final ResourcesReader resourcesReader) {
        this.resourcesReader = resourcesReader;
        return this;
    }

    public CroupierBuilder withRandom(final Random random) {
        this.random = random;
        return this;
    }

    public Croupier build() {
        return new Croupier(resourcesReader, random);
    }
}
