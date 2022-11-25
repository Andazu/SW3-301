package controller;

import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.*;
import java.net.URL;
import java.util.*;

public class AllTasksController implements Initializable, UIMethods, DatabaseMethods{
    @FXML
    private GridPane taskGrid;
    @FXML
    private BorderPane overviewAllTasksBorderPane;
    @FXML
    private ComboBox<String> viewDropdownMenu;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        populateOverviewWithTaskBoxes(taskGrid, null, null, null, 0.0, null, null, null, true, true);

        viewDropdownMenu.getItems().addAll(
                "Employee", "Manager"
        );
    }

    public void refreshPage(ActionEvent event) {
        switchScene(overviewAllTasksBorderPane, "overview-history-page.fxml");
    }

    public void returnToManagerOverview(ActionEvent event){
        switchScene(overviewAllTasksBorderPane, "overview-manager-page.fxml");
    }

    public void changeView(ActionEvent event) {
        if (viewDropdownMenu.getValue().equals("History")) {
            switchScene(overviewAllTasksBorderPane, "overview-history-page.fxml");
        } else if (viewDropdownMenu.getValue().equals("Manager")){
            PinCodeController controller = new PinCodeController();
            makeModalDialog(controller, "manager-pin-code-page.fxml", 300, 400);

            if (controller.isValidPinCode()) {
                switchScene(overviewAllTasksBorderPane, "overview-manager-page.fxml");
            }
        }
    }
}
