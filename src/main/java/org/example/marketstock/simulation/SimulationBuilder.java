package org.example.marketstock.simulation;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.marketstock.models.entity.Player;
import org.example.marketstock.models.exchange.CommodityExchange;
import org.example.marketstock.models.exchange.CurrencyExchange;
import org.example.marketstock.models.exchange.StockExchange;

import java.util.ArrayList;

public class SimulationBuilder {

    public ObjectProperty<Player> player;
    public ObservableList<StockExchange> stockExchanges;
    public ObservableList<CurrencyExchange> currencyExchanges;
    public ObservableList<CommodityExchange> commodityExchanges;

    public SimulationBuilder(Player player) {
        this.player = new SimpleObjectProperty<>(player);
        this.stockExchanges = FXCollections.observableArrayList();
        this.currencyExchanges = FXCollections.observableArrayList();
        this.commodityExchanges = FXCollections.observableArrayList();
    }

    public SimulationBuilder addStockExchanges(ArrayList<StockExchange> arrayList) {
        this.stockExchanges.addAll(arrayList);
        return this;
    }

    public SimulationBuilder addCurrencyExchanges(ArrayList<CurrencyExchange> arrayList) {
        this.currencyExchanges.addAll(arrayList);
        return this;
    }

    public SimulationBuilder addCommodityExchange(ArrayList<CommodityExchange> arrayList) {
        this.commodityExchanges.addAll(arrayList);
        return this;
    }
}
