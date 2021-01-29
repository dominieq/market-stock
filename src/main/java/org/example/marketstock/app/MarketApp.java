package org.example.marketstock.app;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.marketstock.fxml.*;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Modality;
import org.example.marketstock.models.asset.builder.CurrencyBuilder;
import org.example.marketstock.models.briefcase.builder.BriefcaseBuilder;
import org.example.marketstock.models.entity.Player;
import org.example.marketstock.models.exchange.StockExchange;
import org.example.marketstock.simulation.Simulation;
import org.example.marketstock.simulation.builder.SimulationBuilder;
import org.example.marketstock.simulation.croupier.builder.CroupierBuilder;
import org.example.marketstock.simulation.json.SimpleJsonReader;

import static java.util.Objects.nonNull;

/**
 * Main class of the project. Starts the application.
 * Is responsible for creating both the {@link Player} and {@link Simulation}.
 *
 * @author Dominik Szmyt
 * @since 1.0.0
 */
public class MarketApp extends Application {

    private static final Logger LOGGER = LogManager.getLogger(MarketApp.class);

    private Simulation simulation;
    private final SimulationBuilder simulationBuilder = SimulationBuilder.builder();

    private Stage primaryStage;
    private BorderPane rootLayout;

    public static void main(String[] args) {
        LOGGER.info("[APP]: Starting market stock simulator.");
        launch(args);
    }

    /**
     * Starts the application using provided stage.
     * Uses {@link #initRootLayout()} and {@link #showStartMenuLayout()} methods to display starting layout.
     * @param primaryStage The primary stage for this application.
     */
    @Override
    public void start (Stage primaryStage) {
        LOGGER.debug("[APP]: Starting JavaFX application.");

        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Market Stock Simulator");
        
        initRootLayout();
        showStartMenuLayout();
    }

    /**
     * Initializes {@link RootLayoutController} and displays a {@code RootLayout}.
     */
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

    /**
     * Initializes {@link StartMenuLayoutController} and displays a {@code StartMenuLayout}.
     */
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

    /**
     * Builds the {@link Simulation} from a builder and initializes {@link SimulationLayoutController}.
     * In the end displays a {@code SimulationLayout}.
     */
    public void showSimulationLayout() {
        try {
            final FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MarketApp.class.getResource("/fxml/SimulationLayout.fxml"));
            final TabPane layout = loader.load();

            rootLayout.setCenter(layout);

            simulation = simulationBuilder.build();

            final SimulationLayoutController controller = loader.getController();
            controller.setSimulation(simulation);
            controller.setSimulationItems();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Initializes the {@link GuideLayoutController} and displays a {@code GuideLayoutController}.
     */
    public void showGuideLayout() {
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

    /**
     * Initializes the {@link CreatePlayerDialogController} and displays a {@code CreatePlayerDialog}.
     * After a user selected their values, creates the {@link Player} and returns {@code true}.
     * @return {@code true} if the {@link Player} was built successfully, otherwise returns {@code false}.
     */
    public boolean showCreatePlayerDialog() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MarketApp.class.getResource("/fxml/CreatePlayerDialog.fxml"));

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Customize player");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(this.primaryStage);
            dialogStage.setScene(new Scene(loader.load()));

            CreatePlayerDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);

            dialogStage.showAndWait();

            if (controller.isConfirmClicked()) {
                Player.getInstance(
                        controller.getFirstName(),
                        controller.getLastName(),
                        controller.getBudget(),
                        BriefcaseBuilder.builder()
                            .withMap(new HashMap<>())
                            .build()
                );

                simulationBuilder
                        .withPlayer(Player.getInstance(
                                controller.getFirstName(),
                                controller.getLastName(),
                                controller.getBudget(),
                                BriefcaseBuilder.builder()
                                        .withMap(new HashMap<>())
                                        .build()));

                return true;
            }
            return false;
        } catch (IOException exception) {
            exception.printStackTrace();
            return false;
        }
    }

    /**
     * Prepares the {@link SimulationBuilder} for a new game.
     * Adds empty collections and creates the main currency
     * that will be used as a reference for all {@code Currencies}.
     */
    public void prepareNewGame() {
        simulationBuilder
                .withStockExchanges(FXCollections.observableArrayList())
                .withCurrencyExchanges(FXCollections.observableArrayList())
                .withCommodityExchange(FXCollections.observableArrayList())
                .withInvestors(FXCollections.observableArrayList())
                .withInvestmentFunds(FXCollections.observableArrayList());

        final URL countriesURL = getClass().getClassLoader().getResource("built-in-names/countries.json");
        if (nonNull(countriesURL)) {
            simulationBuilder.withMainCurrency(CurrencyBuilder.builder()
                    .withName("MarketAppCurrency")
                    .withRateChanges(new ArrayList<>(Collections.singletonList(0.0)))
                    .withCountries(Arrays.asList(new SimpleJsonReader().getResource(countriesURL.getPath())))
                    .build());
        }

        prepareResources();
    }

    /**
     * Prepares the {@link SimulationBuilder} to include necessary resources for creating new assets and entities.
     * This step includes loading data from default files.
     */
    public void prepareResources() {
        simulationBuilder
                .withCroupier(CroupierBuilder.builder()
                        .withJsonReader(new SimpleJsonReader())
                        .withRandom(new Random())
                        .build());

        final URL currenciesURL = getClass().getClassLoader().getResource("built-in-names/currencies.json");
        if (nonNull(currenciesURL)) {
            simulationBuilder
                    .withCurrencyNames(Arrays.asList(new SimpleJsonReader().getResource(currenciesURL.getPath())));
        }

        final URL commodities = getClass().getClassLoader().getResource("built-in-names/commodities.json");
        if (nonNull(commodities)) {
            simulationBuilder
                    .withCommodityNames(Arrays.asList(new SimpleJsonReader().getResource(commodities.getPath())));
        }
    }

    /**
     * If a {@link Simulation} exists starts all necessary threads
     * which includes: companies, investors and investment funds.
     */
    public void startSimulation() {
        if (nonNull(simulation)) {
            simulation.getStockExchanges().forEach(stockExchange ->
                    stockExchange.getCompanies().forEach(company -> stockExchange.getCompaniesService().submit(company)));

            simulation.getInvestors().forEach(investor -> simulation.getEntitiesService().submit(investor));
            simulation.getInvestmentFunds().forEach(investmentFund -> simulation.getEntitiesService().submit(investmentFund));
        }
    }

    /**
     * If a {@link Simulation} exists tries to kill all running threads in a separate thread.
     * @return An {@code ExecutorService} with a single thread responsible for killing threads from the {@link Simulation}.
     */
    public ExecutorService shutdownSimulation() {
        final ExecutorService shutdownService = Executors.newSingleThreadExecutor();

        if (nonNull(simulation)) {
            shutdownService.submit(() -> {
                LOGGER.debug("[APP]: Stopping all companies...");
                simulation.getStockExchanges().stream()
                        .map(StockExchange::getCompaniesService)
                        .forEach(ExecutorService::shutdownNow);
                LOGGER.debug("[APP]: Stopping investors and investment funds...");
                simulation.getEntitiesService().shutdownNow();
            });
        }

        return shutdownService;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public Simulation getSimulation() {
        return simulation;
    }

    public void setSimulation(final Simulation simulation) {
        this.simulation = simulation;
    }

    public SimulationBuilder getSimulationBuilder() {
        return simulationBuilder;
    }
}
