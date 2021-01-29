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
 * Controls the {@code RootLayout} and consequently the {@code MenuBar}.
 *
 * @author Dominik Szmyt
 * @since 1.0.0
 */
public class RootLayoutController {

    private static final Logger LOGGER = LogManager.getLogger(RootLayoutController.class);
    private MarketApp marketApp;

    @FXML
    private void initialize () {}

    /**
     * Callback fired when a user wishes to save the running {@link Simulation} to a file.
     * @see #serializeSimulation()
     */
    @FXML
    private void handleSaveFile () {
        serializeSimulation();
    }

    /**
     * Callback fired when a user wishes to save the running {@link Simulation} before exiting.
     * If the serialization was successful, closes the application.
     * If some errors occurred during the serialization process, displays a warning
     * and requires a confirmation in order to proceed with closing the application.
     * @see #serializeSimulation()
     */
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

    /**
     * At first, if there is no running {@link Simulation}, warns a user and returns {@code false}.
     * Displays a {@link FileChooser} and after a user selected their file,
     * tries to serialize the existing {@code Simulation}.
     * If the process succeeds returns {@code true}, otherwise returns {@code false}.
     * @return {@code true} if a {@link Simulation} was successfully serialized, otherwise returns {@code false}.
     */
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

    /**
     * Callback fired when a user wants to load a {@link Simulation} from a file.
     * If a simulation is still running, warns a user that they are going to lose their progress and
     * requires a confirmation to proceed. Displays a {@link FileChooser} and after a user selected their file,
     * tries to parse it as a new {@code Simulation}. If another simulation is still running, tries to stop it.
     * If there wasn't any simulation, creates a new one immediately. Displays an error if something went wrong.
     */
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

    /**
     * Callback fired when a user wishes to close the application.
     * Tries to gracefully stop a {@link Simulation} and if succeeds, closes the application.
     */
    @FXML
    private void handleClose () {
        LOGGER.info("[APP]: Shutting down simulation...");
        safeShutdown(marketApp.shutdownSimulation());
        LOGGER.debug("[APP]: Shutting down application...");
        marketApp.getPrimaryStage().close();
    }

    /**
     * Tries to shutdown an {@link ExecutorService} and awaits its termination.
     * If the process didn't succeed, tries to kill remaining processes.
     *
     * @param service An {@code ExecutorService} that is to be shutdown safely.
     */
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

    /**
     * Transforms the provided {@link Exception} into a {@link TextArea} that can be displayed in warning.
     * @param exception An {@code Exception} that is to be displayed in a warning.
     * @return An {@link Exception} inside a {@code TextArea}.
     */
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
