package org.example.marketstock.simulation.croupier.builder;

import org.example.marketstock.simulation.croupier.Croupier;
import org.example.marketstock.simulation.json.ResourcesReader;
import org.example.marketstock.simulation.json.SimpleJsonReader;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

public class CroupierBuilderTest {

    private static Random testRandom;
    private static ResourcesReader testJsonReader;
    private CroupierBuilder subject;

    @BeforeAll
    public static void initialize() {
        testRandom = new Random(0);
        testJsonReader = new SimpleJsonReader();
    }

    @BeforeEach
    public void setUp() {
        subject = CroupierBuilder.builder();
    }

    @Test
    public void should_create_valid_croupier() {

        // when
        final Croupier actual = subject
                .withRandom(testRandom)
                .withResourcesReader(testJsonReader)
                .build();

        // then
        assertThat(actual)
                .hasFieldOrPropertyWithValue("random", testRandom)
                .hasFieldOrPropertyWithValue("resourcesReader", testJsonReader);
    }

    @Test
    public void should_create_valid_croupier_from_other() {

        // given
        final Croupier initial = subject
                .withRandom(testRandom)
                .withResourcesReader(testJsonReader)
                .build();

        // when
        final Croupier actual = subject
                .from(initial)
                .build();

        // then
        assertThat(actual)
                .hasFieldOrPropertyWithValue("random", testRandom)
                .hasFieldOrPropertyWithValue("resourcesReader", testJsonReader);
    }

    @Test
    public void should_create_valid_croupier_from_other_with_new_values() {

        // given
        final Croupier initial = subject
                .withRandom(testRandom)
                .withResourcesReader(testJsonReader)
                .build();

        final Random random = new Random(1);
        final ResourcesReader jsonReader = new SimpleJsonReader();

        // when
        final Croupier actual = subject
                .from(initial)
                .withRandom(random)
                .withResourcesReader(jsonReader)
                .build();

        // then
        assertThat(actual)
                .hasFieldOrPropertyWithValue("random", random)
                .hasFieldOrPropertyWithValue("resourcesReader", jsonReader);
    }
}
