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

public class Simulation {

    private final ObjectProperty<Player> player;
    private final ObservableList<StockExchange> stockExchanges;
    private final ObservableList<CurrencyExchange> currencyExchanges;
    private final ObservableList<CommodityExchange> commodityExchanges;

    public Simulation(Player player) {
        this.player = new SimpleObjectProperty<>(player);
        this.stockExchanges = FXCollections.observableArrayList();
        this.currencyExchanges = FXCollections.observableArrayList();
        this.commodityExchanges = FXCollections.observableArrayList();
    }

    public Simulation(SimulationBuilder builder) {
        this.player = builder.player;
        this.stockExchanges =  builder.stockExchanges;
        this.currencyExchanges = builder.currencyExchanges;
        this.commodityExchanges = builder.commodityExchanges;
    }

    public Player getPlayer() {
        return this.player.getValue();
    }

    public void setPlayer(Player player) {
        this.player.setValue(player);
    }

    public ObservableList<StockExchange> getStockExchanges() {
        return FXCollections.observableArrayList(this.stockExchanges);
    }

    public void setStockExchanges(ArrayList<StockExchange> stockExchanges) {
        this.stockExchanges.addAll(stockExchanges);
    }

    public ObservableList<CurrencyExchange> getCurrencyExchanges() {
        return FXCollections.observableArrayList(this.currencyExchanges);
    }

    public void setCurrencyExchanges(ArrayList<CurrencyExchange> currencyExchanges) {
        this.currencyExchanges.addAll(currencyExchanges);
    }

    public ObservableList<CommodityExchange> getCommodityExchanges() {
        return FXCollections.observableArrayList(this.commodityExchanges);
    }

    public void setCommodityExchanges(ArrayList<CommodityExchange> commodityExchanges) {
        this.commodityExchanges.addAll(commodityExchanges);
    }
}
