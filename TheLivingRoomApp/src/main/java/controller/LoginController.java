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
        TextInputDialog td = new TextInputDialog("Enter PIN code");
        td.getEditor().clear();
        td.setTitle("Mananger Login");
        td.setHeaderText("");
        td.showAndWait();
        if (td.getEditor().getText().equals("123")) {
            switchScene(loginBorderPane, "overview-manager-page.fxml");
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Wrong PIN code");
            alert.show();
        }
    }
}
