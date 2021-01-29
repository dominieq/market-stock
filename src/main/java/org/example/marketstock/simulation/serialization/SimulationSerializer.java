package org.example.marketstock.simulation.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.example.marketstock.models.entity.InvestmentFund;
import org.example.marketstock.models.entity.Investor;
import org.example.marketstock.models.exchange.CommodityExchange;
import org.example.marketstock.models.exchange.CurrencyExchange;
import org.example.marketstock.models.exchange.StockExchange;
import org.example.marketstock.simulation.Simulation;

import java.io.IOException;

/**
 * A custom serializer for the {@link Simulation} class.
 *
 * @author Dominik Szmyt
 * @see Simulation
 * @since 1.0.0
 */
public class SimulationSerializer extends StdSerializer<Simulation> {

    public SimulationSerializer() {
        this(null);
    }

    public SimulationSerializer(Class<Simulation> clazz) {
        super(clazz);
    }

    @Override
    public void serialize(final Simulation simulation,
                          final JsonGenerator jsonGenerator,
                          final SerializerProvider serializerProvider)
            throws IOException {

        jsonGenerator.writeStartObject();

        // Serialize player
        jsonGenerator.writeObjectField("player", simulation.getPlayer());

        // Serialize main currency
        jsonGenerator.writeObjectField("mainCurrency", simulation.getMainCurrency());

        // Serialize Stock Exchanges
        jsonGenerator.writeArrayFieldStart("stockExchanges");

        for (StockExchange stockExchange : simulation.getStockExchanges()) {
            jsonGenerator.writeObject(stockExchange);
        }

        jsonGenerator.writeEndArray();

        // Serialize Commodity Exchanged
        jsonGenerator.writeArrayFieldStart("commodityExchanges");

        for (CommodityExchange commodityExchange : simulation.getCommodityExchanges()) {
            jsonGenerator.writeObject(commodityExchange);
        }

        jsonGenerator.writeEndArray();

        // Serialize Currency Exchanges
        jsonGenerator.writeArrayFieldStart("currencyExchanges");

        for (CurrencyExchange currencyExchange : simulation.getCurrencyExchanges()) {
            jsonGenerator.writeObject(currencyExchange);
        }

        jsonGenerator.writeEndArray();

        // Serialize investors
        jsonGenerator.writeArrayFieldStart("investors");

        for (Investor investor : simulation.getInvestors()) {
            jsonGenerator.writeObject(investor);
        }

        jsonGenerator.writeEndArray();

        // Serialize investment funds
        jsonGenerator.writeArrayFieldStart("investmentFunds");

        for (InvestmentFund investmentFund : simulation.getInvestmentFunds()) {
            jsonGenerator.writeObject(investmentFund);
        }

        jsonGenerator.writeEndArray();

        jsonGenerator.writeEndObject();
    }
}
