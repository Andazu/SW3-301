package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import model.Task;
import model.User;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class TaskFormController implements Initializable, UIMethods, DatabaseMethods {
    @FXML
    private TextField titleTextField;
    @FXML
    private TextField descriptionTextField;
    @FXML
    private DatePicker datePicker;
    @FXML
    private ComboBox frequencyDropdownMenu;
    @FXML
    private ComboBox urgencyDropdownMenu;
    @FXML
    private ComboBox typeDropdownMenu;
    @FXML
    private RadioButton generalRadioButton;
    @FXML
    private RadioButton specificRadioButton;
    @FXML
    private ComboBox assigneeDropdownMenu;
    @FXML
    private Button cancelButton;
    @FXML
    private Button submitButton;
    @FXML
    private BorderPane taskFormBorderPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cancelButton.setCancelButton(true);
        submitButton.setDefaultButton(true);

        generalRadioButton.setSelected(true);
        frequencyDropdownMenu.getItems().addAll(
                "Once", "Everyday", "Every Other Day", "Every Week", "Every Month"
        );
        urgencyDropdownMenu.getItems().addAll(
                "Low", "Medium", "High"
        );
        typeDropdownMenu.getItems().add("IDK What Type Supposed To Be");

        addEmployeesToDropdown();
    }

    public void setSpecificRadioButtonActive(ActionEvent event) {
        logicForRadioButtonsOnAction(generalRadioButton, true);

    }

    public void setGeneralRadioButtonActive(ActionEvent event) {
        logicForRadioButtonsOnAction(specificRadioButton, false);
    }

    public void logicForRadioButtonsOnAction(RadioButton button, boolean setVisible) {
        try {
            if (button.isSelected()) {
                button.setSelected(false);
                assigneeDropdownMenu.setVisible(setVisible);
            }
        } catch (IllegalAccessError e) {
            e.printStackTrace();
        }
    }

    public void cancelAndReturnToOverviewPage(ActionEvent event) {
        switchScene(taskFormBorderPane, "overview-employee-page.fxml");
    }

    public void submitAndReturnToOverviewPage(ActionEvent event) {
        Task createdTask = createTaskFromForm();
        exportTaskToDatabase(createdTask);

        switchScene(taskFormBorderPane, "overview-employee-page.fxml");
    }

    public Task createTaskFromForm() {
        ArrayList<String> list = new ArrayList<>();
        list.add(assigneeDropdownMenu.getValue() != null ? assigneeDropdownMenu.getValue().toString() : "General");

        return assignValuesFromUI(list);
    }

    public Task assignValuesFromUI(ArrayList<String> commentsList) {
        String title = titleTextField.getText();
        String description = descriptionTextField.getText();
        String frequency = frequencyDropdownMenu.getValue() != null ? frequencyDropdownMenu.getValue().toString() : null;
        String urgency = urgencyDropdownMenu.getValue() != null ? urgencyDropdownMenu.getValue().toString() : null;
        String type = typeDropdownMenu.getValue() != null ? typeDropdownMenu.getValue().toString() : null;
        LocalDate date = datePicker.getValue();

        return new Task(title, description, frequency, urgency, type, 0, true, commentsList, date);
    }

    public void addEmployeesToDropdown() {
        ArrayList<String> employees = DatabaseMethods.getEmployeesFromDB(false);

        for (int i = 0; i < employees.size(); i++) {
            assigneeDropdownMenu.getItems().add(employees.get(i));
        }
    }
}
