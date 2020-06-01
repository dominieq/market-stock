package org.example.marketstock.app;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.marketstock.fxml.*;
import java.io.IOException;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Modality;
import org.example.marketstock.models.asset.Commodity;
import org.example.marketstock.models.asset.Currency;
import org.example.marketstock.models.asset.Share;
import org.example.marketstock.models.asset.InvestmentUnit;
import org.example.marketstock.models.company.Company;
import org.example.marketstock.models.entity.InvestmentFund;
import org.example.marketstock.models.entity.Investor;
import org.example.marketstock.models.entity.Player;
import org.example.marketstock.models.exchange.CommodityExchange;
import org.example.marketstock.models.exchange.CurrencyExchange;
import org.example.marketstock.models.exchange.StockExchange;
import org.example.marketstock.models.index.Index;

/**
 *
 * @author Dominik Szmyt
 * @since 1.0.0
 */
public class MarketApp extends Application {

    private static final Logger LOGGER = LogManager.getLogger(MarketApp.class);
    private Stage primaryStage;
    private BorderPane rootLayout;

    private Player player;

    private ObservableList<StockExchange> stockExchanges;
    private ObservableList<CommodityExchange> commodityExchanges;
    private  ObservableList<CurrencyExchange> currencyExchanges;
    private volatile ObservableList<Share> shares;
    private volatile ObservableList<InvestmentUnit> investmentUnits;
    private volatile ObservableList<Currency> currencies;
    private volatile ObservableList<Commodity> commodities;
    private volatile ObservableList<InvestmentFund> investmentFunds;
    private volatile ObservableList<Investor> investors;
    private volatile ObservableList<Company> companies;
    private ObservableList<Index> indices;

    public static void main(String[] args) {
        LOGGER.info("Starting market stock simulator");
        launch(args);
    }

    public void addEntities() {
        int count = this.shares.size() + this.investmentUnits.size()
                + this.currencies.size() + this.commodities.size();

        if(count % 5 == 0) {
            /* Create new investor and add them to simulation. */
            Investor investor = new Investor();
            investor.initialize(this);
            investors.add(investor);

            /* Start the thread of the new investor. */
            Thread thread1 = new Thread(investor);
            thread1.start();
            investor.setThreadId(thread1.getId());

            /* Create new investment fund and add it to simulation. */
            InvestmentFund investmentFund = new InvestmentFund();
            investmentFund.initialize(this);
            investmentFunds.add(investmentFund);
            investmentUnits.add(investmentFund.getInvestmentUnit());

            /* Start the thread of the new investment fund. */
            Thread thread2 = new Thread(investmentFund);
            thread2.start();
            investmentFund.setThreadId(thread2.getId());
        }
    }

    @Override
    public void start (Stage primaryStage) {
        LOGGER.info("Starting JavaFX application.");

        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Market Stock Simulator");

        player = new Player();
        stockExchanges = FXCollections.observableArrayList();
        commodityExchanges = FXCollections.observableArrayList();
        currencyExchanges = FXCollections.observableArrayList();
        currencies = FXCollections.observableArrayList();
        commodities = FXCollections.observableArrayList();
        shares = FXCollections.observableArrayList();
        investmentUnits = FXCollections.observableArrayList();
        investmentFunds = FXCollections.observableArrayList();
        investors = FXCollections.observableArrayList();
        companies = FXCollections.observableArrayList();
        indices = FXCollections.observableArrayList();
        
        initRootLayout();
        showStartMenuLayout();
    }

    private void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MarketApp.class.getResource("/fxml/RootLayout.fxml"));
            rootLayout = loader.load();

            Scene scene = new Scene (rootLayout);
            primaryStage.setScene(scene);

            RootLayoutController controller = loader.getController();
            controller.setMarketApp(this);

            primaryStage.show();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private void showStartMenuLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MarketApp.class.getResource("/fxml/StartMenuLayout.fxml"));
            VBox layout = loader.load();

            rootLayout.setCenter(layout);

            StartMenuLayoutController controller = loader.getController();
            controller.setMarketApp(this);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void showSimulationLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MarketApp.class.getResource("/fxml/SimulationLayout.fxml"));
            TabPane layout = loader.load();

            rootLayout.setCenter(layout);

            SimulationLayoutController simulation = loader.getController();
            simulation.setMarketApp(this);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void showGuideLayout () {
        try {
            FXMLLoader loader = new FXMLLoader ();
            loader.setLocation(MarketApp.class.getResource("/fxml/GuideLayout.fxml"));
            AnchorPane guideLayout = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Manual and description of the application");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(primaryStage);

            Scene scene = new Scene(guideLayout);
            stage.setScene(scene);

            stage.showAndWait();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public Stage getPrimaryStage () {
        return primaryStage;
    }

    public Player getPlayer() {
        return this.player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public ObservableList<StockExchange> getStockExchanges() {
        return this.stockExchanges;
    }

    public void setStockExchanges(ArrayList<StockExchange> stockExchanges) {
        this.stockExchanges = FXCollections.observableArrayList(stockExchanges);
    }

    public ObservableList<CommodityExchange> getCommodityExchanges() {
        return this.commodityExchanges;
    }

    public void setCommodityExchanges(ArrayList<CommodityExchange> commodityExchanges) {
        this.commodityExchanges = FXCollections.observableArrayList(commodityExchanges);
    }

    public ObservableList<CurrencyExchange> getCurrencyExchanges() {
        return this.currencyExchanges;
    }

    public void setCurrencyExchanges(ArrayList<CurrencyExchange> currencyExchanges) {
        this.currencyExchanges = FXCollections.observableArrayList(currencyExchanges);
    }

    public ObservableList<Share> getShares() {
        return this.shares;
    }

    public void setShares(ArrayList<Share> shares) {
        this.shares = FXCollections.observableArrayList(shares);
    }

    public ObservableList<InvestmentUnit> getInvestmentUnits() {
        return this.investmentUnits;
    }

    public void setInvestmentUnits(ArrayList<InvestmentUnit> investmentUnits) {
        this.investmentUnits = FXCollections.observableArrayList(investmentUnits);
    }

    public ObservableList<Currency> getCurrencies() {
        return this.currencies;
    }

    public void setCurrencies(ArrayList<Currency> currencies) {
        this.currencies = FXCollections.observableArrayList(currencies);
    }

    public ObservableList<Commodity> getCommodities() {
        return this.commodities;
    }

    public void setCommodities(ArrayList<Commodity> commodities) {
        this.commodities = FXCollections.observableArrayList(commodities);
    }

    public ObservableList<InvestmentFund> getInvestmentFunds() {
        return investmentFunds;
    }

    public void setInvestmentFunds(ArrayList<InvestmentFund> investmentFunds) {
        this.investmentFunds = FXCollections.observableArrayList(investmentFunds);

        for (InvestmentFund investmentFund : this.investmentFunds) {
            Thread thread = new Thread(investmentFund);
            thread.start();
            investmentFund.setThreadId(thread.getId());
        }
    }

    public ObservableList<Investor> getInvestors() {
        return investors;
    }

    public void setInvestors(ArrayList<Investor> investors) {
        this.investors = FXCollections.observableArrayList(investors);

        for (Investor investor : this.investors) {
            investor.setMarketApp(this);

            Thread thread = new Thread(investor);

            thread.start();
            investor.setThreadId(thread.getId());
        }
    }

    public ObservableList<Company> getCompanies() {
        return companies;
    }

    public void setCompanies(ArrayList<Company> companies) {
        this.companies = FXCollections.observableArrayList(companies);

        for (Company company : this.companies) {
            Thread thread = new Thread(company);

            thread.start();
            company.setThreadId(thread.getId());
        }
    }

    public ObservableList<Index> getIndices() {
        return indices;
    }

    public void setIndices(ArrayList<Index> indices) {
        this.indices = FXCollections.observableArrayList(indices);
    }
}
