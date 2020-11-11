package org.example.marketstock.simulation.croupier;

import com.google.common.base.MoreObjects;
import org.example.marketstock.simulation.json.JsonReader;

import java.net.URL;
import java.util.Calendar;
import java.util.Random;

import static java.util.Objects.isNull;

public class Croupier {

    private static final String COMPANIES_LOCATION = "built-in-names/companies.json";
    private static final String COUNTRIES_LOCATION = "built-in-names/countries.json";
    private static final String CITIES_LOCATION = "built-in-names/cities.json";
    private static final String ADDRESSES_LOCATION = "built-in-names/addresses.json";
    private static final String CURRENCIES_LOCATION = "built-in-names/currencies.json";
    private static final String FIRST_NAMES_LOCATION = "built-in-names/first-names.json";
    private static final String LAST_NAMES_LOCATION = "built-in-names/last-names.json";
    private static final String FUND_NAMES_LOCATION = "built-in-names/fund-names.json";

    private final JsonReader jsonReader;
    private final Random random;

    public Croupier(final JsonReader jsonReader,
                    final Random random) {

        this.jsonReader = jsonReader;
        this.random = random;
    }

    public String drawCompanyName() {
        final String path = getPath(COMPANIES_LOCATION);
        return Croupiers.drawString(random, jsonReader.getResource(path));
    }

    public String drawCountry() {
        final String path = getPath(COUNTRIES_LOCATION);
        return Croupiers.drawString(random, jsonReader.getResource(path));
    }

    public String drawCity() {
        final String path = getPath(CITIES_LOCATION);
        return Croupiers.drawString(random, jsonReader.getResource(path));
    }

    public String drawAddress() {
        final String path = getPath(ADDRESSES_LOCATION);
        return Croupiers.drawString(random, jsonReader.getResource(path));
    }

    public String drawCurrency() {
        final String path = getPath(CURRENCIES_LOCATION);
        return Croupiers.drawString(random, jsonReader.getResource(path));
    }

    public double drawMargin() {
        return Croupiers.drawDoubleWithBoundaries(random, 0.01D, 0.15D);
    }

    public String drawDateOfFirstValuation() {
        return Croupiers.drawDateWithBoundaries(random, 1945, Calendar.getInstance().get(Calendar.YEAR));
    }

    public double drawOpeningRate() {
        return Croupiers.drawDoubleWithBoundaries(random, 1D, 7D);
    }

    public int drawNumberOfAssets() {
        return random.nextInt(99_001) + 1000;
    }

    public double drawRevenue() {
        return Croupiers.drawDoubleWithBoundaries(random, 100_000_000D, 999_999_999D);
    }

    public double drawProfit() {
        double primaryValue = Croupiers.drawDoubleWithBoundaries(random, 100_000_000D, 999_999_999D);
        double scaleDown = Croupiers.drawDoubleWithBoundaries(random, 0.005D, 0.25D);

        return primaryValue * scaleDown;
    }

    public double drawEquityCapital() {
        return Croupiers.drawDoubleWithBoundaries(random, 100_000D, 999_999_999D);
    }

    public double drawOpeningCapital() {
        return Croupiers.drawDoubleWithBoundaries(random, 50_000D, 999_999_999D);
    }

    public double drawCurrentRate() {
        return Croupiers.drawDoubleWithBoundaries(random, 0.01D, 10D);
    }

    public String[] drawCountries() {
        final String path = getPath(COUNTRIES_LOCATION);
        return Croupiers.drawSubset(random, jsonReader.getResource(path));
    }

    public String drawFirstName() {
        final String path = getPath(FIRST_NAMES_LOCATION);
        return Croupiers.drawString(random, jsonReader.getResource(path));
    }

    public String drawLastName() {
        final String path = getPath(LAST_NAMES_LOCATION);
        return Croupiers.drawString(random, jsonReader.getResource(path));
    }

    public String drawInvestmentFundName() {
        final String path = getPath(FUND_NAMES_LOCATION);
        return Croupiers.drawString(random, jsonReader.getResource(path));
    }

    public double drawBudget() {
        return Croupiers.drawDoubleWithBoundaries(random, 10_000D, 1_000_000D);
    }

    public String drawPESEL() {
        final long lowerBoundary = 11_111_111_111L;
        final long upperBoundary = 99_999_999_999L;

        return String.valueOf(lowerBoundary + (long) (Math.random() * (upperBoundary - lowerBoundary)));
    }

    private String getPath(String location) {
        URL url = getClass().getClassLoader().getResource(location);

        if (isNull(url)) {
            return "";
        }

        return url.getPath();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("jsonReader", jsonReader)
                .add("random", random)
                .toString();
    }

    public Random getRandom() {
        return random;
    }

    public JsonReader getJsonReader() {
        return jsonReader;
    }
}
