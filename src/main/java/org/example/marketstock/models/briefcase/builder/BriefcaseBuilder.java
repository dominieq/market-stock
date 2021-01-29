package org.example.marketstock.models.briefcase.builder;

import org.example.marketstock.models.briefcase.Briefcase;
import org.example.marketstock.models.asset.Asset;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A builder for the {@link Briefcase} class.
 *
 * @author Dominik Szmyt
 * @see Briefcase
 * @since 1.0.0
 */
public final class BriefcaseBuilder {

    private Map<Asset, Integer> map;

    private BriefcaseBuilder() { }

    public static BriefcaseBuilder builder() {
        return new BriefcaseBuilder();
    }

    public static BriefcaseBuilder emptyBriefcase() {
        return new BriefcaseBuilder().withMap(new LinkedHashMap<>());
    }

    public BriefcaseBuilder withMap(final Map<Asset, Integer> map) {
        this.map = map;
        return this;
    }

    public Briefcase build() {
        return new Briefcase(map);
    }
}
