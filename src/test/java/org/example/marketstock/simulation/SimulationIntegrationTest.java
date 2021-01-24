package org.example.marketstock.simulation;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import org.example.marketstock.models.asset.builder.CurrencyBuilder;
import org.example.marketstock.models.briefcase.builder.BriefcaseBuilder;
import org.example.marketstock.models.company.Company;
import org.example.marketstock.models.company.builder.CompanyBuilder;
import org.example.marketstock.models.entity.Investor;
import org.example.marketstock.models.entity.Player;
import org.example.marketstock.models.entity.builder.InvestorBuilder;
import org.example.marketstock.models.exchange.StockExchange;
import org.example.marketstock.models.exchange.builder.StockExchangeBuilder;
import org.example.marketstock.simulation.builder.SimulationBuilder;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;

import static org.assertj.core.api.Assertions.assertThat;

public class SimulationIntegrationTest {

    @Test
    public void should_serialize_simulation_to_json() throws Exception {

        // given
        final StockExchange stockExchange = StockExchangeBuilder.builder()
                .withName("New York Stock Exchange")
                .withCountry("USA")
                .withCity("New York")
                .withAddress("11 Wall Street")
                .withCurrency("USD")
                .withMargin(0.12)
                .withIndices(new ArrayList<>())
                .withCompanies(new ArrayList<>())
                .build();

        final Company company = CompanyBuilder.builder()
                .withName("Stratton Oakmont")
                .withCurrentRate(4.41)
                .withMinRate(4D)
                .withMaxRate(4.41)
                .withRateChanges(new ArrayList<>(Arrays.asList(4D, 4.2, 4.41)))
                .withMargin(0.12)
                .withNumberOfAssets(90)
                .withDateOfFirstValuation("1989-01-01")
                .withOpeningQuotation(4D)
                .withProfit(100_000D)
                .withRevenue(100_000D)
                .withOpeningCapital(1000D)
                .withEquityCapital(1000D)
                .withTurnover(41D)
                .withVolume(10)
                .build();

        stockExchange.getCompanies().add(company);

        final Investor investor = InvestorBuilder.builder()
                .withFirstName("Scrooge")
                .withLastName("McQuack")
                .withBudget(79D)
                .withPESEL("1234567890")
                .withBriefcase(BriefcaseBuilder.builder()
                        .withMap(new LinkedHashMap<>(Collections.singletonMap(company, 5)))
                        .build())
                .build();

        final Simulation simulation = SimulationBuilder.builder()
                .withPlayer(Player.updateInstance("Jordan", "Belfort", 80D,
                        BriefcaseBuilder.builder()
                                .withMap(new LinkedHashMap<>(Collections.singletonMap(company, 5)))
                                .build()))
                .withStockExchanges(FXCollections.observableArrayList(stockExchange))
                .withCommodityExchange(FXCollections.observableArrayList())
                .withCurrencyExchanges(FXCollections.observableArrayList())
                .withInvestors(FXCollections.observableArrayList(investor))
                .withInvestmentFunds(FXCollections.observableArrayList())
                .withMainCurrency(CurrencyBuilder.builder()
                        .withName("USD")
                        .withRateChanges(new ArrayList<>(Collections.singletonList(0.0)))
                        .withCountries(new ArrayList<>(Collections.singletonList("USA")))
                        .build())
                .build();

        // when
        final String actual = new ObjectMapper().writeValueAsString(simulation);
        final URL expectedUrl = getClass().getClassLoader().getResource("simulation/simulation.json");
        assertThat(expectedUrl).isNotNull();
        final String expected = new String(Files.readAllBytes(Paths.get(expectedUrl.toURI())));

        // then
        assertThat(actual).isEqualToIgnoringWhitespace(expected);
    }

    @Test
    public void should_deserialize_simulation_from_json() throws IOException {

        // given
        final URL url = getClass().getClassLoader().getResource("simulation/simulation.json");
        assertThat(url).isNotNull();

        // when
        final Simulation actual = new ObjectMapper().readValue(url, Simulation.class);

        // then
        assertThat(actual.getStockExchanges()).isNotEmpty();
        assertThat(actual.getInvestors()).isNotEmpty();

        final Company company = actual.getStockExchanges().get(0).getCompanies().get(0);
        final Company playerCompany = (Company) actual.getPlayer().getBriefcase().getAssets().get(0);
        final Company investorCompany = (Company) actual.getInvestors().get(0).getBriefcase().getAssets().get(0);

        assertThat(company).isEqualTo(playerCompany).isEqualTo(investorCompany);
        assertThat(actual.getPlayer().getBriefcase().contains(company, 5)).isTrue();
        assertThat(actual.getInvestors().get(0).getBriefcase().contains(company, 5)).isTrue();
    }
}
