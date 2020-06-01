package org.example.marketstock.fxml;

import org.example.marketstock.app.MarketApp;
import javafx.fxml.FXML;

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
        this.marketApp.getPlayer().createPlayer("Dominik", "Szmyt", 10000.0);
        this.marketApp.showSimulationLayout();
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

    public MarketApp getMarketApp() {
        return marketApp;
    }

    public void setMarketApp(MarketApp marketApp) {
        this.marketApp = marketApp;
    }
}
