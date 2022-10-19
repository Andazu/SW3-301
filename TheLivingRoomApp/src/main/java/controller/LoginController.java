package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;

public class LoginController {
    private StageMethods stageMethods = new StageMethods();

    @FXML
    private BorderPane loginBorderPane;

    public void openEmployeeOverviewPage(ActionEvent event) {
        stageMethods.switchStage(loginBorderPane, "overview-employee-page.fxml");
    }

    public void openManagerOverviewPage(ActionEvent event) {
        stageMethods.switchStage(loginBorderPane, "overview-manager-page.fxml");
    }
}
