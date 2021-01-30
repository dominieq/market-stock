package org.example.marketstock.models.briefcase.builder;

import org.example.marketstock.models.asset.Asset;
import org.example.marketstock.models.briefcase.Briefcase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class BriefcaseBuilderTest {

    private BriefcaseBuilder subject;

    @BeforeEach
    public void setUp() {
        subject = BriefcaseBuilder.builder();
    }

    @Test
    public void should_build_valid_briefcase() {

        // given
        final Map<Asset, Integer> map = new HashMap<>();

        // when
        final Briefcase actual = subject
                .withMap(map)
                .build();

        // then
        assertThat(actual)
                .hasFieldOrPropertyWithValue("map", map);
    }

    @Test
    public void should_build_empty_briefcase() {

        // when
        subject = BriefcaseBuilder.emptyBriefcase();
        final Briefcase actual = subject.build();

        // then
        assertThat(actual.getMap())
                .isInstanceOf(LinkedHashMap.class)
                .isEmpty();
    }
}
