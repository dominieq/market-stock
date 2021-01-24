package org.example.marketstock.simulation.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import javafx.collections.FXCollections;
import org.example.marketstock.models.asset.Currency;
import org.example.marketstock.models.entity.InvestmentFund;
import org.example.marketstock.models.entity.Investor;
import org.example.marketstock.models.entity.Player;
import org.example.marketstock.models.exchange.CommodityExchange;
import org.example.marketstock.models.exchange.CurrencyExchange;
import org.example.marketstock.models.exchange.StockExchange;
import org.example.marketstock.simulation.Simulation;
import org.example.marketstock.simulation.builder.SimulationBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SimulationDeserializer extends StdDeserializer<Simulation> {

    public SimulationDeserializer() {
        this(null);
    }

    public SimulationDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Simulation deserialize(final JsonParser jsonParser,
                                  final DeserializationContext deserializationContext)
            throws IOException, JsonProcessingException {

        final JsonNode jsonNode = jsonParser.getCodec().readTree(jsonParser);

        // Deserialize player
        final Player player = new ObjectMapper().readValue(jsonNode.get("player").toString(), Player.class);

        // Deserialize stock exchanges
        final JsonNode jsonStockExchanges = jsonNode.get("stockExchanges");
        final List<StockExchange> stockExchanges = new ArrayList<>();

        for (JsonNode node : jsonStockExchanges) {
            stockExchanges.add(new ObjectMapper().readValue(node.toString(), StockExchange.class));
        }

        // Deserialize commodity exchanges
        final JsonNode jsonCommodityExchanges = jsonNode.get("commodityExchanges");
        final List<CommodityExchange> commodityExchanges = new ArrayList<>();

        for (JsonNode node : jsonCommodityExchanges) {
            commodityExchanges.add(new ObjectMapper().readValue(node.toString(), CommodityExchange.class));
        }

        // Deserialize currency exchanges
        final JsonNode jsonCurrencyExchanges = jsonNode.get("currencyExchanges");
        final List<CurrencyExchange> currencyExchanges = new ArrayList<>();

        for (JsonNode node : jsonCurrencyExchanges) {
            currencyExchanges.add(new ObjectMapper().readValue(node.toString(), CurrencyExchange.class));
        }

        // Deserialize investors
        final JsonNode jsonInvestors = jsonNode.get("investors");
        final List<Investor> investors = new ArrayList<>();

        for (JsonNode node : jsonInvestors) {
            investors.add(new ObjectMapper().readValue(node.toString(), Investor.class));
        }

        // Deserialize investment funds
        final JsonNode jsonInvestmentFunds = jsonNode.get("investmentFunds");
        final List<InvestmentFund> investmentFunds = new ArrayList<>();

        for (JsonNode node : jsonInvestmentFunds) {
            investmentFunds.add(new ObjectMapper().readValue(node.toString(), InvestmentFund.class));
        }

        // Deserialize main currency
        final Currency mainCurrency = new ObjectMapper().readValue(jsonNode.get("mainCurrency").toString(), Currency.class);

        return SimulationBuilder.builder()
                .withPlayer(player)
                .withStockExchanges(FXCollections.observableArrayList(stockExchanges))
                .withCommodityExchange(FXCollections.observableArrayList(commodityExchanges))
                .withCurrencyExchanges(FXCollections.observableArrayList(currencyExchanges))
                .withInvestors(FXCollections.observableArrayList(investors))
                .withInvestmentFunds(FXCollections.observableArrayList(investmentFunds))
                .withMainCurrency(mainCurrency)
                .build();
    }
}
