package org.example.marketstock.simulation.croupier;

import org.example.marketstock.simulation.json.JsonReader;
import org.example.marketstock.simulation.json.SimpleJsonReader;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URL;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

public class CroupierTest {

    private static JsonReader jsonReader;
    private static Random random;
    private Croupier subject;

    @BeforeAll
    public static void initialize() {
        jsonReader = new SimpleJsonReader();
        random = new Random(0);
    }

    @BeforeEach
    public void setUp() {
        subject = new Croupier(jsonReader, random);
    }

    @Test
    public void should_draw_valid_country() {

        // given
        URL url = getClass().getClassLoader().getResource("built-in-names/countries.json");
        assertThat(url).isNotNull();
        String[] countries = jsonReader.getResource(url.getPath());

        // when
        String actual = subject.drawCountry();

        // then
        assertThat(actual).isNotEmpty();
        assertThat(countries).containsOnlyOnce(actual);
    }

    @Test
    public void should_draw_valid_city() {

        // given
        URL url = getClass().getClassLoader().getResource("built-in-names/cities.json");
        assertThat(url).isNotNull();
        String[] cities = jsonReader.getResource(url.getPath());

        // when
        String actual = subject.drawCity();

        // then
        assertThat(actual).isNotEmpty();
        assertThat(cities).containsOnlyOnce(actual);
    }

    @Test
    public void should_draw_valid_address() {

        // given
        URL url = getClass().getClassLoader().getResource("built-in-names/addresses.json");
        assertThat(url).isNotNull();
        String[] addresses = jsonReader.getResource(url.getPath());

        // when
        String actual = subject.drawAddress();

        // then
        assertThat(actual).isNotEmpty();
        assertThat(addresses).containsOnlyOnce(actual);
    }

    @Test
    public void should_draw_valid_currency() {

        // given
        URL url = getClass().getClassLoader().getResource("built-in-names/currencies.json");
        assertThat(url).isNotNull();
        String[] currencies = jsonReader.getResource(url.getPath());

        // when
        String actual = subject.drawCurrency();

        // then
        assertThat(actual).isNotEmpty();
        assertThat(currencies).containsOnlyOnce(actual);
    }

    @Test
    public void should_draw_valid_company_name() {

        // given
        URL url = getClass().getClassLoader().getResource("built-in-names/companies.json");
        assertThat(url).isNotNull();
        String[] names = jsonReader.getResource(url.getPath());

        // when
        String actual = subject.drawCompanyName();

        // then
        assertThat(actual).isNotEmpty();
        assertThat(names).containsOnlyOnce(actual);
    }

    @Test
    public void should_draw_valid_margin() {

        // when
        double actual = subject.drawMargin();

        // then
        assertThat(actual).isBetween(0.01D, 0.15D);
    }

    @Test
    public void should_draw_valid_date_of_first_valuation() {

        // when
        String actual = subject.drawDateOfFirstValuation();

        // then
        assertThatCode(() -> LocalDate.parse(actual)).doesNotThrowAnyException();
        assertThat(LocalDate.parse(actual).getYear())
                .isBetween(1945, Calendar.getInstance().get(Calendar.YEAR));
    }

    @Test
    public void should_draw_valid_opening_rate() {

        // when
        double actual = subject.drawOpeningRate();

        // then
        assertThat(actual).isBetween(1D, 7D);
    }


    @Test
    public void should_draw_valid_number_of_assets() {

        // when
        int actual = subject.drawNumberOfAssets();

        // then
        assertThat(actual).isBetween(1000, 100_000);
    }

    @Test
    public void should_draw_valid_revenue() {

        // when
        double actual = subject.drawRevenue();

        // then
        assertThat(actual).isBetween(100_000_000D, 999_999_999D);
    }

    @Test
    public void should_draw_valid_profit() {

        // when
        double actual = subject.drawProfit();

        // then
        assertThat(actual).isBetween(500_000D, 250_000_000D);
    }

    @Test
    public void should_draw_valid_equity_capital() {

        // when
        double actual = subject.drawEquityCapital();

        // then
        assertThat(actual).isBetween(100_000D, 999_999_999D);
    }

    @Test
    public void should_draw_opening_capital() {

        // when
        double actual = subject.drawOpeningCapital();

        // then
        assertThat(actual).isBetween(50_000D, 999_999_999D);
    }

    @Test
    public void should_draw_valid_current_rate() {

        // when
        double actual = subject.drawCurrentRate();

        // then
        assertThat(actual).isBetween(0.01D, 10D);
    }

    @Test
    public void should_draw_valid_countries_list() {

        // given
        URL url = getClass().getClassLoader().getResource("built-in-names/countries.json");
        assertThat(url).isNotNull();
        String[] names = jsonReader.getResource(url.getPath());

        // when
        String[] actual = subject.drawCountries();

        // then
        assertThat(actual).isNotEmpty();
        assertThat(names).containsAll(Arrays.asList(actual));
    }

    @Test
    public void should_draw_valid_first_name() {

        // given
        URL url = getClass().getClassLoader().getResource("built-in-names/first-names.json");
        assertThat(url).isNotNull();
        String[] names = jsonReader.getResource(url.getPath());

        // when
        String actual = subject.drawFirstName();

        // then
        assertThat(actual).isNotEmpty();
        assertThat(names).contains(actual);
    }

    @Test
    public void should_draw_valid_last_name() {

        // given
        URL url = getClass().getClassLoader().getResource("built-in-names/last-names.json");
        assertThat(url).isNotNull();
        String[] names = jsonReader.getResource(url.getPath());

        // when
        String actual = subject.drawLastName();

        // then
        assertThat(actual).isNotEmpty();
        assertThat(names).contains(actual);
    }

    @Test
    public void should_draw_valid_investment_fund_name() {

        // given
        URL url = getClass().getClassLoader().getResource("built-in-names/fund-names.json");
        assertThat(url).isNotNull();
        String[] names = jsonReader.getResource(url.getPath());

        // when
        String actual = subject.drawInvestmentFundName();

        // then
        assertThat(actual).isNotEmpty();
        assertThat(names).contains(actual);
    }

    @Test
    public void should_draw_valid_budget() {

        // when
        double actual = subject.drawBudget();

        // then
        assertThat(actual).isBetween(10_000D, 1_000_000D);
    }

    @Test
    public void should_draw_valid_PESEL() {

        // when
        String actual = subject.drawPESEL();

        // then
        assertThat(actual).isNotEmpty();
        assertThat(Long.parseLong(actual)).isBetween(11_111_111_111L, 99_999_999_999L);
    }
}
