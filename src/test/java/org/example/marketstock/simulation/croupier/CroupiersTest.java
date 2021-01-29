package org.example.marketstock.simulation.croupier;

import org.example.marketstock.exceptions.simulation.InvalidDrawParamsException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Random;

import static org.assertj.core.api.Assertions.*;

public class CroupiersTest {

    private static Random random;

    @BeforeAll
    public static void initialize() {
        random = new Random(0);
    }

    @ParameterizedTest
    @CsvSource({
            "1945, 2020",
            "1989, 2016",
            "1991, 2001",
            "1945, 1955",
            "2015, 2020"

    })
    public void should_draw_valid_date(int minYear, int maxYear) {

        // given
        LocalDate minDate = LocalDate.of(minYear, 1, 1);
        LocalDate maxDate = LocalDate.of(maxYear, 12, 31);

        // when
        String actual = Croupiers.drawDateWithBoundaries(minYear, maxYear);

        // then
        assertThatCode(() -> LocalDate.parse(actual)).doesNotThrowAnyException();

        LocalDate localDate = LocalDate.parse(actual);

        assertThat(localDate)
                .isAfterOrEqualTo(minDate)
                .isBeforeOrEqualTo(maxDate);
    }
    @ParameterizedTest
    @CsvSource({
            "2020, 1945",
            "2016, 1989",
            "2001, 1991",
            "1955, 1945",
            "2020, 2015"
    })
    public void should_not_draw_valid_date(int minYear, int maxYear) {
        assertThatExceptionOfType(InvalidDrawParamsException.class).isThrownBy(
                () -> Croupiers.drawDateWithBoundaries(minYear, maxYear)
        ).withMessage("Min year cannot be greater or equal max year " +
                Arrays.toString(new Object[]{minYear, maxYear})
        );
    }

    @ParameterizedTest
    @CsvSource({
            "0.0D, 0.85D",
            "1D, 45D",
            "-45D, -1D",
            "-0.85D, -0.1D"
    })
    public void should_draw_valid_double(double lowerBoundary, double upperBoundary) {

        // when
        Double actual = Croupiers.drawDoubleWithBoundaries(lowerBoundary, upperBoundary);

        // then
        assertThat(actual)
                .isNotNull()
                .isBetween(lowerBoundary, upperBoundary);
    }

    @ParameterizedTest
    @CsvSource({
            "0.85D, 0.0D",
            "45D, 1D",
            "-1D, -45D",
            "-0.1D, -0.85D"
    })
    public void should_not_draw_valid_double(double lowerBoundary, double upperBoundary) {
        assertThatExceptionOfType(InvalidDrawParamsException.class).isThrownBy(
                () -> Croupiers.drawDoubleWithBoundaries(lowerBoundary, upperBoundary)
        ).withMessage("Lower boundary cannot be greater or equal upper boundary " +
                Arrays.toString(new Object[]{lowerBoundary, upperBoundary})
        );
    }

    @ParameterizedTest
    @ArgumentsSource(StringArrayProvider.class)
    public void should_draw_valid_string_from_array(String[] strings) {

        // when
        String actual = Croupiers.drawString(strings);

        // then
        assertThat(actual).isNotEmpty();
        assertThat(strings).contains(actual);
    }

    @Test
    public void should_not_draw_string_empty_array() {

        // given
        final String[] emptyArray = new String[]{};

        // then
        assertThatExceptionOfType(InvalidDrawParamsException.class).isThrownBy(
                () -> Croupiers.drawString(emptyArray)
        ).withMessage("Array cannot be empty " + Arrays.toString(new Object[]{emptyArray}));
    }

    @ParameterizedTest
    @ArgumentsSource(StringArrayProvider.class)
    public void should_draw_valid_subset_with_random_limit_from_array(String[] strings) {

        // when
        String[] actual = Croupiers.drawSubset(strings);

        // then
        assertThat(actual).isNotEmpty();
        assertThat(strings).containsAll(Arrays.asList(actual));
    }

    @ParameterizedTest
    @ArgumentsSource(StringArrayProvider.class)
    public void should_draw_valid_subset_with_known_limit_from_array(String[] strings) {

        // when
        String[] actual = Croupiers.drawSubset(random, 1, strings);

        // then
        assertThat(actual).hasSize(1);
        assertThat(strings).containsAll(Arrays.asList(actual));
    }

    @ParameterizedTest
    @ArgumentsSource(StringArrayProvider.class)
    public void should_not_draw_subset_from_array_with_invalid_limit(String[] strings) {
        assertThatExceptionOfType(InvalidDrawParamsException.class).isThrownBy(
                () -> Croupiers.drawSubset(random, strings.length + 1, strings)
        ).withMessage("Subset cannot be greater than initial array " + Arrays.toString(new Object[]{strings.length + 1, strings}));
    }
}
