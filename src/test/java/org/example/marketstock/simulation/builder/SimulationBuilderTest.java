package org.example.marketstock.simulation.builder;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import org.example.marketstock.models.asset.Asset;
import org.example.marketstock.models.asset.Currency;
import org.example.marketstock.models.asset.builder.CurrencyBuilder;
import org.example.marketstock.models.briefcase.Briefcase;
import org.example.marketstock.models.briefcase.builder.BriefcaseBuilder;
import org.example.marketstock.models.entity.InvestmentFund;
import org.example.marketstock.models.entity.Investor;
import org.example.marketstock.models.entity.Player;
import org.example.marketstock.models.exchange.CommodityExchange;
import org.example.marketstock.models.exchange.CurrencyExchange;
import org.example.marketstock.models.exchange.StockExchange;
import org.example.marketstock.simulation.Simulation;
import org.example.marketstock.simulation.croupier.Croupier;
import org.example.marketstock.simulation.croupier.builder.CroupierBuilder;
import org.example.marketstock.simulation.json.SimpleJsonReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

public class SimulationBuilderTest {

    private SimulationBuilder subject;

    @BeforeEach
    public void setUp() {
        subject = SimulationBuilder.builder();
    }

    @Test
    public void should_build_valid_simulation() {

        // given
        final Briefcase briefcase = BriefcaseBuilder.builder()
                .withMap(new HashMap<>())
                .build();

        final Player player = Player.getInstance(
                "FirstName",
                "LastName",
                10_000D,
                briefcase);

        final ObservableMap<Asset, Integer> playersBriefcase = FXCollections.observableHashMap();
        playersBriefcase.putAll(briefcase.getMap());

        final ObservableList<StockExchange> stockExchanges = FXCollections.observableArrayList();
        final ObservableList<CommodityExchange> commodityExchanges = FXCollections.observableArrayList();
        final ObservableList<CurrencyExchange> currencyExchanges = FXCollections.observableArrayList();
        final ObservableList<Investor> investors = FXCollections.observableArrayList();
        final ObservableList<InvestmentFund> investmentFunds = FXCollections.observableArrayList();
        final List<String> currencyNames = FXCollections.observableArrayList();
        final List<String> commodityNames = FXCollections.observableArrayList();

        final Croupier croupier = CroupierBuilder.builder()
                .withResourcesReader(new SimpleJsonReader())
                .withRandom(new Random(0))
                .build();
        final Currency mainCurrency = CurrencyBuilder.builder()
                .withName("MainCurrency")
                .withRateChanges(new ArrayList<>())
                .withCountries(new ArrayList<>())
                .build();

        // when
        final Simulation actual = subject
                .withPlayer(player)
                .withStockExchanges(stockExchanges)
                .withCommodityExchange(commodityExchanges)
                .withCurrencyExchanges(currencyExchanges)
                .withInvestors(investors)
                .withInvestmentFunds(investmentFunds)
                .withCurrencyNames(currencyNames)
                .withCommodityNames(commodityNames)
                .withCroupier(croupier)
                .withMainCurrency(mainCurrency)
                .build();

        // then
        assertThat(actual)
                .hasFieldOrPropertyWithValue("player", player)
                .hasFieldOrPropertyWithValue("stockExchanges", stockExchanges)
                .hasFieldOrPropertyWithValue("commodityExchanges", commodityExchanges)
                .hasFieldOrPropertyWithValue("currencyExchanges", currencyExchanges)
                .hasFieldOrPropertyWithValue("investors", investors)
                .hasFieldOrPropertyWithValue("investmentFunds", investmentFunds)
                .hasFieldOrPropertyWithValue("commodityNames", commodityNames)
                .hasFieldOrPropertyWithValue("currencyNames", currencyNames)
                .hasFieldOrPropertyWithValue("croupier", croupier)
                .hasFieldOrPropertyWithValue("mainCurrency", mainCurrency);
    }
}
