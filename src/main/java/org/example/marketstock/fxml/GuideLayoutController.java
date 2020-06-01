package org.example.marketstock.fxml;

import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 *
 * @author Dominik
 * @since 1.0.0
 */
public class GuideLayoutController {

    @FXML private TextFlow guideText;

    @FXML private void initialize () {
        String guideString = "Guide and description of Market Stock Simulator\nAuthor: Dominik Szmyt\n";
        guideText.getChildren().addAll(new Text(guideString));
    }
}
