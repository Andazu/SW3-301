package controller;

import javafx.event.ActionEvent;

public class LoginController {
    private StageMethods stageMethods = new StageMethods();

    public void openEmployeeOverviewPage(ActionEvent event) {
        stageMethods.switchStage(event, "overview-employee-page.fxml");
    }

    public void openManagerOverviewPage(ActionEvent event) {
        stageMethods.switchStage(event, "overview-manager-page.fxml");
    }
}
