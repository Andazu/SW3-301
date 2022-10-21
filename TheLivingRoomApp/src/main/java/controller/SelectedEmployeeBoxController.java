package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import model.User;

public class SelectedEmployeeBoxController {
    @FXML
    private Label name;
    @FXML
    private Label role;
    @FXML
    private CheckBox checkBox;

    public void setLabelInBoxUI(User user) {
        checkBox.setId(user.getId());
        name.setText(user.getFullName());
        role.setText(user.getRole());
    }

    public void setAssigneeToDeletion(ActionEvent event) {
        if (checkBox.isSelected()) {
            DatabaseMethods.updateSelectedAssigneeDoc(checkBox.getId(), "selectedEmployees", true);
        } else {
            DatabaseMethods.updateSelectedAssigneeDoc(checkBox.getId(), "selectedEmployees", false);
        }
    }
}
