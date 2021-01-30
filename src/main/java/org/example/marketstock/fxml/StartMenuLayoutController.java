package org.example.marketstock.fxml;

import javafx.fxml.FXML;
import org.example.marketstock.app.MarketApp;

/**
 * Controls the {@code StartMenuLayout}.
 *
 * @author Dominik Szmyt
 * @since 1.0.0
 */
public class StartMenuLayoutController {
    
    private MarketApp marketApp;
    
    @FXML
    private void initialize() {}

    /**
     * Callback fired when a user wishes to play new game.
     * Stars simulation with default settings.
     */
    @FXML
    private void handleNewGame () {
        if (marketApp.showCreatePlayerDialog()) {
            marketApp.prepareNewGame();
            marketApp.showSimulationLayout();
        }
    }

    /**
     * Callback fired when a user wishes to close the application.
     * Closes the application.
     */
    @FXML
    private void handleExit () {
        this.marketApp.getPrimaryStage().close();
    }

    /**
     * Callback fired when a user wishes to read a guide.
     * Displays a short manual in a new dialog.
     */
    @FXML
    private void handleGuide () {
        this.marketApp.showGuideLayout();
    }

    public void setMarketApp(MarketApp marketApp) {
        this.marketApp = marketApp;
    }
}
