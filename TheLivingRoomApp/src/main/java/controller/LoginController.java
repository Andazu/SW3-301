package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;

public class LoginController implements UIMethods {
    @FXML
    private BorderPane loginBorderPane;

    public void openEmployeeOverviewPage(ActionEvent event) {
        switchScene(loginBorderPane, "overview-employee-page.fxml");
    }

    public void openManagerOverviewPage(ActionEvent event) {
        managerPinCodeLogin(loginBorderPane);
    }

    public void openHistoryOverviewPage(ActionEvent event) {
        switchScene(loginBorderPane, "overview-history-page.fxml");
    }
}
