package org.example.marketstock.simulation.croupier;

import org.example.marketstock.exceptions.simulation.InvalidDrawParamsException;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static java.util.Objects.isNull;

/**
 * Provides methods that generate random values.
 *
 * @since 1.0.0
 * @author Dominik Szmyt
 */
public final class Croupiers {

    private static final Random random = new Random(0);

    /**
     * Draws a new date with the year part between the selected boundaries using custom {@link Random} object.
     *
     * @param random - A custom {@link Random} object.
     * @param minYear - The lower boundary of the year part in generated {@link LocalDate}.
     * @param maxYear - The upper boundary of the year part in generated {@link LocalDate}.
     * @return String - A new date with the year part between the selected boundaries.
     */
    public static String drawDateWithBoundaries(Random random, int minYear, int maxYear) {
        if (isNull(random)) {
            throw new InvalidDrawParamsException("Random cannot be null", new Object[]{minYear, maxYear});
        }

        if (minYear >= maxYear) {
            throw new InvalidDrawParamsException("Min year cannot be greater or equal max year",
                    new Object[]{minYear, maxYear});
        }

        final int minDay = (int) LocalDate.of(minYear, 1, 1).toEpochDay();
        final int maxDay = (int) LocalDate.of(maxYear, 12, 31).toEpochDay();
        final long randomDay = minDay + random.nextInt(maxDay - minDay);

        return LocalDate.ofEpochDay(randomDay).toString();
    }

    /**
     * Draws a new date with the year part between the selected boundaries
     * using default {@link Random} object with seed that equals 0.
     *
     * @param minYear - The lower boundary of the year part in generated {@link LocalDate}.
     * @param maxYear - The upper boundary of the year part in generated {@link LocalDate}.
     * @return String - A new date with the year part between the selected boundaries.
     */
    public static String drawDateWithBoundaries(int minYear, int maxYear) {
        return drawDateWithBoundaries(random, minYear, maxYear);
    }

    /**
     * Draws a new double value between the selected boundaries using custom {@link Random} object.
     *
     * @param random - A custom {@link Random} object.
     * @param lowerBoundary - The lower boundary of a new double value.
     * @param upperBoundary - The upper boundary of a new double value.
     * @return double - A new double value between the selected boundaries.
     */
    public static double drawDoubleWithBoundaries(Random random, double lowerBoundary, double upperBoundary) {
        if (isNull(random)) {
            throw new InvalidDrawParamsException("Random cannot be null", new Object[]{lowerBoundary, upperBoundary});
        }

        if (lowerBoundary >= upperBoundary) {
            throw new InvalidDrawParamsException("Lower boundary cannot be greater or equal upper boundary",
                    new Object[]{lowerBoundary, upperBoundary});
        }

        return lowerBoundary + (upperBoundary - lowerBoundary) * random.nextDouble();
    }

    /**
     * Draws a new double value between the selected boundaries
     * using default {@link Random} object with seed that equals 0.
     *
     * @param lowerBoundary - The lower boundary of a new double value.
     * @param upperBoundary - The upper boundary of a new double value.
     * @return double - A new double value between the selected boundaries.
     */
    public static double drawDoubleWithBoundaries(double lowerBoundary, double upperBoundary) {
        return drawDoubleWithBoundaries(random, lowerBoundary, upperBoundary);
    }

    /**
     * Draws a string value from the provided string array using custom {@link Random} object.
     *
     * @param random - A custom {@link Random} object.
     * @param strings - The array of string values.
     * @return String - A random string value selected from the provided string array.
     */
    public static String drawString(Random random, String [] strings) {
        if (isNull(random)) {
            throw new InvalidDrawParamsException("Random cannot be null", new Object[]{strings});
        }

        if (strings.length == 0) {
            throw new InvalidDrawParamsException("Array cannot be empty", new Object[]{strings});
        }

        return strings[random.nextInt(strings.length)];
    }

    /**
     * Draws a string value from the provided string array
     * using custom {@link Random} object with seed that equals 0.
     *
     * @param strings - The array of string values.
     * @return String - A random string value selected from the provided string array.
     */
    public static String drawString(String[] strings) {
        return drawString(random, strings);
    }

    /**
     * Draws a subset from the provided string array using custom {@link Random} object.
     * The subset's size is equal to the provided limit value.
     *
     * @param random - A custom {@link Random} object.
     * @param limit - The size of a subset.
     * @param strings - The array of string values.
     * @return String[] - The random subset of the provided string array.
     */
    public static String[] drawSubset(Random random, int limit, String[] strings) {
        if (isNull(random)) {
            throw new InvalidDrawParamsException("Random cannot be null", new Object[]{limit, strings});
        }

        if (strings.length == 0) {
            throw new InvalidDrawParamsException("Array cannot be empty", new Object[]{limit, strings});
        }

        if (limit > strings.length) {
            throw new InvalidDrawParamsException("Subset cannot be greater than initial array", new Object[]{limit, strings});
        }

        Set<String> subset = new HashSet<>();
        while (subset.size() < limit) {
            subset.add(strings[random.nextInt(strings.length)]);
        }

        return Arrays.copyOf(subset.toArray(), subset.toArray().length, String[].class);
    }

    /**
     * Draws a subset from the provided string array using custom {@link Random} object.
     * The subset's size is a random value not greater then the array of strings.
     *
     * @param random - A custom {@link Random} object.
     * @param strings - The array of string values.
     * @return String[] - The random subset of the string array.
     */
    public static String[] drawSubset(Random random, String[] strings) {
        return drawSubset(random, 1 + random.nextInt(strings.length), strings);
    }

    /**
     * Draws a subset from the provided string array
     * using default {@link Random} object with seed that equals 0.
     * The subset's size is a random value not greater then the size of provided array.
     *
     * @param strings - The array of string values.
     * @return String[] - The random subset of the string array.
     */
    public static String[] drawSubset(String[] strings) {
        return drawSubset(random, strings);
    }
}
