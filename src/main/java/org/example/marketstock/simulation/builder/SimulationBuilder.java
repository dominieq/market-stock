package org.example.marketstock.simulation.builder;

import javafx.collections.ObservableList;
import org.example.marketstock.models.asset.Currency;
import org.example.marketstock.models.entity.InvestmentFund;
import org.example.marketstock.models.entity.Investor;
import org.example.marketstock.models.entity.Player;
import org.example.marketstock.models.exchange.CommodityExchange;
import org.example.marketstock.models.exchange.CurrencyExchange;
import org.example.marketstock.models.exchange.StockExchange;
import org.example.marketstock.simulation.Simulation;
import org.example.marketstock.simulation.croupier.Croupier;

import java.util.List;

import static java.util.Objects.isNull;

/**
 * A builder for the {@link Simulation} class.
 *
 * @author Dominik Szmyt
 * @see Simulation
 * @since 1.0.0
 */
public final class SimulationBuilder {

    private Player player;
    private ObservableList<StockExchange> stockExchanges;
    private ObservableList<CurrencyExchange> currencyExchanges;
    private ObservableList<CommodityExchange> commodityExchanges;
    private ObservableList<Investor> investors;
    private ObservableList<InvestmentFund> investmentFunds;
    private List<String> commodityNames;
    private List<String> currencyNames;
    private Croupier croupier;
    private Currency mainCurrency;

    public static SimulationBuilder builder() {
        return new SimulationBuilder();
    }

    public SimulationBuilder from (final Simulation simulation) {
        if (isNull(simulation)) {
            return this;
        }

        this.player = simulation.getPlayer();
        this.stockExchanges = simulation.getStockExchanges();
        this.currencyExchanges = simulation.getCurrencyExchanges();
        this.commodityExchanges = simulation.getCommodityExchanges();
        this.investors = simulation.getInvestors();
        this.investmentFunds = simulation.getInvestmentFunds();
        this.commodityNames = simulation.getCommodityNames();
        this.currencyExchanges = simulation.getCurrencyExchanges();
        this.croupier = simulation.getCroupier();
        this.mainCurrency = simulation.getMainCurrency();
        return this;
    }

    public SimulationBuilder withPlayer(final Player player) {
        this.player = player;
        return this;
    }

    public SimulationBuilder withStockExchanges(final ObservableList<StockExchange> stockExchanges) {
        this.stockExchanges = stockExchanges;
        return this;
    }

    public SimulationBuilder withCurrencyExchanges(final ObservableList<CurrencyExchange> currencyExchanges) {
        this.currencyExchanges = currencyExchanges;
        return this;
    }

    public SimulationBuilder withCommodityExchange(final ObservableList<CommodityExchange> commodityExchange) {
        this.commodityExchanges = commodityExchange;
        return this;
    }

    public SimulationBuilder withInvestors(final ObservableList<Investor> investors) {
        this.investors = investors;
        return this;
    }

    public SimulationBuilder withInvestmentFunds(final ObservableList<InvestmentFund> investmentFunds) {
        this.investmentFunds = investmentFunds;
        return this;
    }

    public SimulationBuilder withCommodityNames(final List<String> commodityNames) {
        this.commodityNames = commodityNames;
        return this;
    }

    public SimulationBuilder withCurrencyNames(final List<String> currencyNames) {
        this.currencyNames = currencyNames;
        return this;
    }

    public SimulationBuilder withCroupier(final Croupier croupier) {
        this.croupier = croupier;
        return this;
    }

    public SimulationBuilder withMainCurrency(final Currency mainCurrency) {
        this.mainCurrency = mainCurrency;
        return this;
    }

    public Simulation build() {
        return new Simulation(
                player,
                stockExchanges, currencyExchanges, commodityExchanges,
                investors, investmentFunds,
                commodityNames, currencyNames,
                croupier, mainCurrency
        );
    }
}
