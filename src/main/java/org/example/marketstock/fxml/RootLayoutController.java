package org.example.marketstock.fxml;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.marketstock.app.MarketApp;

import javafx.fxml.FXML;

/**
 *
 * @author Dominik
 * @since 1.0.0
 */
public class RootLayoutController {

    private static final Logger LOGGER = LogManager.getLogger(RootLayoutController.class);
    private MarketApp marketApp;

    @FXML
    private void initialize () {}

    @FXML
    private void handleSaveFile () {
        this.serializeSimulation();
    }

    @FXML
    private void handleSaveAndCloseFile() {
        LOGGER.info("[APP]: Shutting down simulation...");
        marketApp.shutdownSimulation();
        serializeSimulation();
        LOGGER.debug("[APP]: Shutting down application...");
        marketApp.getPrimaryStage().close();
    }

    private void serializeSimulation() {
        throw new RuntimeException();
    }

    @FXML
    private void handleLoadFile () {
        throw new RuntimeException();
    }

    @FXML
    private void handleClose () {
        LOGGER.info("[APP]: Shutting down simulation...");
        marketApp.shutdownSimulation();
        LOGGER.debug("[APP]: Shutting down application...");
        marketApp.getPrimaryStage().close();
    }

    public void setMarketApp(MarketApp marketApp) {
        this.marketApp = marketApp;
    }
}
