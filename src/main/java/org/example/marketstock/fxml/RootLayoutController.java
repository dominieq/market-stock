package org.example.marketstock.fxml;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.fxml.FXML;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.marketstock.app.MarketApp;
import org.example.marketstock.simulation.Simulation;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

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
        serializeSimulation();
    }

    @FXML
    private void handleSaveAndCloseFile() {
        final boolean serialized = serializeSimulation();

        if (serialized || isNull(marketApp.getSimulation())) {
            LOGGER.info("[APP]: Shutting down simulation...");
            safeShutdown(marketApp.shutdownSimulation());
            marketApp.getPrimaryStage().close();
        } else {
            final Alert warning = new Alert(Alert.AlertType.WARNING);
            warning.setTitle("Saving problems");
            warning.setHeaderText("Do you wish to continue?");
            warning.setContentText("Some problems occurred while saving simulation.\n" +
                    "Do you wish to close the application anyway?");

            final ButtonType confirmButton = new ButtonType("OK", ButtonBar.ButtonData.YES);
            final ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            warning.getButtonTypes().setAll(confirmButton, cancelButton);

            final Optional<ButtonType> selection = warning.showAndWait();
            if (selection.isPresent() && Objects.equals(selection.get(), confirmButton)) {
                LOGGER.info("[APP]: Shutting down simulation...");
                safeShutdown(marketApp.shutdownSimulation());
                LOGGER.debug("[APP]: Shutting down application...");
                marketApp.getPrimaryStage().close();
            }
        }
    }

    private boolean serializeSimulation() {
        if (isNull(marketApp.getSimulation())) {
            final Alert warning = new Alert(Alert.AlertType.WARNING);
            warning.setTitle("No simulation");
            warning.setHeaderText("Cannot save without simulation.");
            warning.showAndWait();
            return false;
        }

        synchronized (marketApp.getSimulation()) {
            final FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save simulation");
            fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JSON", "*.json"));
            fileChooser.setInitialFileName("simulation");

            final File file = fileChooser.showSaveDialog(marketApp.getPrimaryStage());

            if (nonNull(file)) {
                try (final PrintWriter printWriter = new PrintWriter(file)) {
                    final String serialized = new ObjectMapper().writeValueAsString(marketApp.getSimulation());
                    printWriter.println(serialized);
                } catch (IOException exception) {
                    final Alert error = new Alert(Alert.AlertType.ERROR);
                    error.setTitle("Saving error");
                    error.setHeaderText("Error occurred while saving file: " + file.getName());
                    error.getDialogPane().setExpandableContent(getExceptionTextArea(exception));
                    error.showAndWait();

                    LOGGER.error(exception.getMessage(), exception);
                    return false;
                }
                return true;
            }
            return false;
        }
    }

    @FXML
    private void handleLoadFile () {
        if (nonNull(marketApp.getSimulation())) {
            final Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Unsaved progress");
            confirmation.setHeaderText("You have unsaved progress");
            confirmation.setTitle("You will lose all your progress. Do you wish to proceed?");

            final Optional<ButtonType> selection = confirmation.showAndWait();
            if (!(selection.isPresent() &&  Objects.equals(selection.get(), ButtonType.OK))) return;
        }

        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load simulation");

        final FileChooser.ExtensionFilter jsonFilter = new FileChooser.ExtensionFilter("JSON", "*.json");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All files", "*.*"), jsonFilter);
        fileChooser.setSelectedExtensionFilter(jsonFilter);
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));

        final File file = fileChooser.showOpenDialog(marketApp.getPrimaryStage());

        if (nonNull(file)) {
            try {
                final Simulation simulation = new ObjectMapper().readValue(file, Simulation.class);
                marketApp.getSimulationBuilder().from(simulation);

                if (isNull(marketApp.getSimulation())) {
                    marketApp.prepareResources();
                } else {
                    safeShutdown(marketApp.shutdownSimulation());

                    final Simulation oldSimulation = marketApp.getSimulation();
                    marketApp.getSimulationBuilder()
                            .withCommodityNames(oldSimulation.getCommodityNames())
                            .withCurrencyNames(oldSimulation.getCurrencyNames())
                            .withCroupier(oldSimulation.getCroupier())
                            .build();
                }

                marketApp.showSimulationLayout();
                marketApp.startSimulation();
            } catch (IOException exception) {
                final Alert error = new Alert(Alert.AlertType.ERROR);
                error.setTitle("Loading error");
                error.setHeaderText("Error occurred while loading file: " + file.getName());
                error.getDialogPane().setExpandableContent(getExceptionTextArea(exception));
                error.showAndWait();

                LOGGER.error(exception.getMessage(), exception);
            }
        }
    }

    @FXML
    private void handleClose () {
        LOGGER.info("[APP]: Shutting down simulation...");
        safeShutdown(marketApp.shutdownSimulation());
        LOGGER.debug("[APP]: Shutting down application...");
        marketApp.getPrimaryStage().close();
    }

    private void safeShutdown(final ExecutorService service) {
        service.shutdown();
        boolean isTerminated = false;

        try {
            isTerminated = service.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException exception) {
            LOGGER.warn("[APP]: Shutting down simulation interrupted");
        } finally {
            if (!isTerminated) service.shutdownNow();
        }
    }

    private TextArea getExceptionTextArea(final Exception exception) {
        final StringWriter stringWriter = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(stringWriter);
        exception.printStackTrace(printWriter);

        final TextArea textArea = new TextArea(printWriter.toString());
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        return textArea;
    }

    public void setMarketApp(MarketApp marketApp) {
        this.marketApp = marketApp;
    }
}
