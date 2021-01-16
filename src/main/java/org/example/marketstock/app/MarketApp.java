package org.example.marketstock.app;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.marketstock.fxml.*;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutorService;

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
import org.example.marketstock.simulation.json.JsonReader;
import org.example.marketstock.simulation.json.SimpleJsonReader;

import static java.util.Objects.nonNull;

/**
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
        LOGGER.info("Starting market stock simulator");
        launch(args);
    }

    @Override
    public void start (Stage primaryStage) {
        LOGGER.info("Starting JavaFX application.");

        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Market Stock Simulator");
        
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
            final FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MarketApp.class.getResource("/fxml/SimulationLayout.fxml"));
            final TabPane layout = loader.load();

            rootLayout.setCenter(layout);

            final JsonReader jsonReader = new SimpleJsonReader();
            simulationBuilder
                    .withStockExchanges(FXCollections.observableArrayList())
                    .withCurrencyExchanges(FXCollections.observableArrayList())
                    .withCommodityExchange(FXCollections.observableArrayList())
                    .withInvestors(FXCollections.observableArrayList())
                    .withInvestmentFunds(FXCollections.observableArrayList())
                    .withCroupier(CroupierBuilder.builder()
                            .withJsonReader(jsonReader)
                            .withRandom(new Random())
                            .build());

            final URL currenciesURL = getClass().getClassLoader().getResource("built-in-names/currencies.json");
            if (nonNull(currenciesURL)) {
                simulationBuilder.withCurrencyNames(Arrays.asList(jsonReader.getResource(currenciesURL.getPath())));
            }

            final URL commodities = getClass().getClassLoader().getResource("built-in-names/commodities.json");
            if (nonNull(commodities)) {
                simulationBuilder.withCommodityNames(Arrays.asList(jsonReader.getResource(commodities.getPath())));
            }

            final URL countriesURL = getClass().getClassLoader().getResource("built-in-names/countries.json");
            if (nonNull(countriesURL)) {
                simulationBuilder.withMainCurrency(CurrencyBuilder.builder()
                        .withName("MarketAppCurrency")
                        .withRateChanges(new ArrayList<>(Collections.singletonList(0.0)))
                        .withCountries(Arrays.asList(jsonReader.getResource(countriesURL.getPath())))
                        .build());
            }

            simulation = simulationBuilder.build();

            final SimulationLayoutController controller = loader.getController();
            controller.setSimulation(simulation);
            controller.setSimulationItems();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

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

    public void shutdownSimulation() {
        if (nonNull(simulation)) {
            simulation.getStockExchanges().stream()
                    .map(StockExchange::getCompaniesService)
                    .forEach(ExecutorService::shutdownNow);
            simulation.getEntitiesService().shutdownNow();
        }
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public Simulation getSimulation() {
        return simulation;
    }

    public SimulationBuilder getSimulationBuilder() {
        return simulationBuilder;
    }
}
