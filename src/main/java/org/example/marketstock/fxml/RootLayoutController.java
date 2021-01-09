package org.example.marketstock.fxml;

import org.example.marketstock.app.MarketApp;

import java.util.logging.Logger;

import javafx.fxml.FXML;

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
        this.serializeSimulation();
    }

    @FXML
    private void handleSaveAndCloseFile() {
        marketApp.shutdownSimulation();
        serializeSimulation();
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
        marketApp.shutdownSimulation();
        marketApp.getPrimaryStage().close();
    }

    public void setMarketApp(MarketApp marketApp) {
        this.marketApp = marketApp;
    }
}
