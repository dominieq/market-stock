package org.example.marketstock.simulation.croupier;

import org.example.marketstock.exceptions.simulation.InvalidDrawParamsException;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static java.util.Objects.isNull;

public final class Croupiers {

    private static final Random random = new Random(0);

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

    public static String drawDateWithBoundaries(int minYear, int maxYear) {
        return drawDateWithBoundaries(random, minYear, maxYear);
    }

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

    public static double drawDoubleWithBoundaries(double lowerBoundary, double upperBoundary) {
        return drawDoubleWithBoundaries(random, lowerBoundary, upperBoundary);
    }

    public static String drawString(Random random, String [] strings) {
        if (isNull(random)) {
            throw new InvalidDrawParamsException("Random cannot be null", new Object[]{strings});
        }

        if (strings.length == 0) {
            throw new InvalidDrawParamsException("Array cannot be empty", new Object[]{strings});
        }

        return strings[random.nextInt(strings.length)];
    }

    public static String drawString(String[] strings) {
        return drawString(random, strings);
    }

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

    public static String[] drawSubset(Random random, String[] strings) {
        return drawSubset(random, 1 + random.nextInt(strings.length), strings);
    }

    public static String[] drawSubset(String[] strings) {
        return drawSubset(random, strings);
    }
}
