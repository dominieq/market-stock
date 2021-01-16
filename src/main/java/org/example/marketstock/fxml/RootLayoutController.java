package org.example.marketstock.fxml;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.stage.FileChooser;
import org.example.marketstock.app.MarketApp;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

import javafx.fxml.FXML;

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
                    // TODO change for logger
                    exception.printStackTrace();
                }
                return true;
            }
        }

        return false;
    }

    @FXML
    private void handleLoadFile () {
        // TODO check whether simulation exist and needs to be stopped

        final FileChooser fileChooser = new FileChooser();
        final File file = fileChooser.showOpenDialog(marketApp.getPrimaryStage());

        if (nonNull(file)) {
            // TODO deserialize
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
