package org.example.marketstock.fxml;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.stage.FileChooser;
import org.example.marketstock.app.MarketApp;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

import javafx.fxml.FXML;
import org.example.marketstock.simulation.Simulation;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

/**
 *
 * @author Dominik
 * @since 1.0.0
 */
public class RootLayoutController {

    private static final Logger LOGGER = Logger.getLogger(RootLayoutController.class.getName());
    private MarketApp marketApp;

    @FXML
    private void initialize () {}

    @FXML
    private void handleSaveFile () {
        serializeSimulation();
    }

    @FXML
    private void handleSaveAndCloseFile() {
        final boolean serialized = serializeSimulation();

        if (serialized) {
            marketApp.shutdownSimulation();
            marketApp.getPrimaryStage().close();
        }
    }

    private boolean serializeSimulation() {
        synchronized (marketApp.getSimulation()) {
            final FileChooser fileChooser = new FileChooser();
            final File file = fileChooser.showSaveDialog(marketApp.getPrimaryStage());

            if (nonNull(file)) {
                try (final PrintWriter printWriter = new PrintWriter(file)) {
                    final String serialized = new ObjectMapper().writeValueAsString(marketApp.getSimulation());
                    printWriter.println(serialized);
                } catch (IOException exception) {
                    // TODO change for logger after merge
                    exception.printStackTrace();
                    return false;
                }
                return true;
            }
        }
        return false;
    }

    @FXML
    private void handleLoadFile () {
        if (nonNull(marketApp.getSimulation())) marketApp.shutdownSimulation();

        final FileChooser fileChooser = new FileChooser();
        final File file = fileChooser.showOpenDialog(marketApp.getPrimaryStage());

        if (nonNull(file)) {
            try {
                final Simulation simulation = new ObjectMapper().readValue(file, Simulation.class);
                marketApp.getSimulationBuilder().from(simulation);

                if (isNull(marketApp.getSimulation())) {
                    marketApp.prepareResources();
                    marketApp.showSimulationLayout();
                } else {
                    final Simulation oldSimulation = marketApp.getSimulation();
                    marketApp.setSimulation(
                            marketApp.getSimulationBuilder()
                                    .withCommodityNames(oldSimulation.getCommodityNames())
                                    .withCurrencyNames(oldSimulation.getCurrencyNames())
                                    .withCroupier(oldSimulation.getCroupier())
                                    .build());
                }
            } catch (IOException exception) {
                // TODO change for logger after merge
                exception.printStackTrace();
            }
        }
    }

    @FXML
    private void handleClose () {
        marketApp.shutdownSimulation();
        marketApp.getPrimaryStage().close();
    }

    public void setMarketApp(MarketApp marketApp) {
        this.marketApp = marketApp;
    }
}
