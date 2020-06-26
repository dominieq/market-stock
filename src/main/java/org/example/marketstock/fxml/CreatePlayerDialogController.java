package org.example.marketstock.fxml;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CreatePlayerDialogController {

    private String firstName;
    private String lastName;
    private double budget;
    private Stage dialogStage;
    private boolean confirmClicked;

    public CreatePlayerDialogController() {
        this.firstName = "";
        this.lastName = "";
        this.budget = 0.0;
    }

    @FXML
    private TextField firstNameTextField;

    @FXML
    private TextField lastNameTextField;

    @FXML
    private TextField budgetTextField;

    @FXML
    public void initialize() {
        this.budgetTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[\\d.]")) {
                this.budgetTextField.setText(newValue.replaceAll("[^\\d.]", ""));
            }
        });
    }

    @FXML
    private void handleCancel() {
        String firstName = this.firstNameTextField.getText();
        String lastName = this.lastNameTextField.getText();
        String budget = this.budgetTextField.getText();

        if (firstName != null && !firstName.equals("")
                || lastName != null && !lastName.equals("")
                || budget != null && !budget.equals("")) {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Unsaved changes");
            alert.setContentText("There are unsaved changes.\nDo you wish to proceed?");
            alert.showAndWait()
                    .filter(response -> response == ButtonType.OK)
                    .ifPresent(response -> {
                        this.confirmClicked = false;
                        this.dialogStage.close();
                    });
        } else {
            this.confirmClicked = false;
            this.dialogStage.close();
        }
    }

    @FXML
    private void handleConfirm() {
        String alertContent = "";

        if (this.firstNameTextField.getText() == null || this.firstNameTextField.getText().length() == 0) {
            alertContent += "no valid first name\n";
        }

        if (this.lastNameTextField.getText() == null || this.lastNameTextField.getText().length() == 0) {
            alertContent += "no valid last name\n";
        }

        if (this.budgetTextField.getText() == null || this.budgetTextField.getText().length() == 0) {
            alertContent += "no valid budget";
        } else {
            try {
                Double.parseDouble(this.budgetTextField.getText());
            } catch (NumberFormatException exception) {
                alertContent += "budget should be a valid number";
            }
        }

        if (!alertContent.equals("")) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid input");
            alert.setHeaderText("There are several problems");
            alert.setContentText(alertContent);
            alert.showAndWait();
        } else {
            this.firstName = this.firstNameTextField.getText();
            this.lastName = this.lastNameTextField.getText();
            this.budget = Double.parseDouble(this.budgetTextField.getText());
            this.confirmClicked = true;
            this.dialogStage.close();
        }
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public Stage getDialogStage() {
        return dialogStage;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public boolean isConfirmClicked() {
        return confirmClicked;
    }

    public void setConfirmClicked(boolean confirmClicked) {
        this.confirmClicked = confirmClicked;
    }
}
