package org.example.marketstock.fxml;

import javafx.fxml.FXML;
import org.example.marketstock.app.MarketApp;

/**
 *
 * @author Dominik
 * @since 1.0.0
 */
public class StartMenuLayoutController {
    
    private MarketApp marketApp;
    
    @FXML
    private void initialize() {}

    /**
     * Starts simulation with default settings.
     */
    @FXML
    private void handleNewGame () {
        if (marketApp.showCreatePlayerDialog()) {
            marketApp.showSimulationLayout();
        }
    }

    /**
     * Closes application.
     */
    @FXML
    private void handleExit () {
        this.marketApp.getPrimaryStage().close();
    }

    /**
     * Displays short manual in a new window.
     */
    @FXML
    private void handleGuide () {
        this.marketApp.showGuideLayout();
    }

    public void setMarketApp(MarketApp marketApp) {
        this.marketApp = marketApp;
    }
}
