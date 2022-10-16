package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Task;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AddTaskController implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;
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

    public void setSpecificRadioButton(ActionEvent event) {
        try {
            if (generalRadioButton.isSelected()) {
                generalRadioButton.setSelected(false);
                assigneeDropdownMenu.setVisible(true);
            }
        } catch (IllegalAccessError e) {
            e.printStackTrace();
        }

    }

    public void setGeneralRadioButton(ActionEvent event) {
        try {
            if (specificRadioButton.isSelected()) {
                specificRadioButton.setSelected(false);
                assigneeDropdownMenu.setVisible(false);
            }
        } catch (IllegalAccessError e) {
            e.printStackTrace();
        }
    }

    public void handleCancel(ActionEvent event) {
        try {
            root = FXMLLoader.load(getClass().getResource("overview-employee-page.fxml"));
            stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root, 1024, 700);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Task createTask() {
        ArrayList<String> list = new ArrayList<>();
        list.add(assigneeDropdownMenu.getValue().toString());
        Task task = new Task(titleTextField.getText(),descriptionTextField.getText(),frequencyDropdownMenu.getValue().toString(),
                urgencyDropdownMenu.getValue().toString(),typeDropdownMenu.getValue().toString(),0,true,list,
                datePicker.getValue());
        return task;
    }

    public void handleSubmit(ActionEvent event) {
        try {
            Task createdTask = createTask();
            createdTask.exportDocument();

            root = FXMLLoader.load(getClass().getResource("overview-employee-page.fxml"));
            stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root, 1024, 700);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        generalRadioButton.setSelected(true);
        frequencyDropdownMenu.getItems().addAll(
                "Once", "Everyday", "Every Other Day", "Every Week", "Every Month"
        );
        urgencyDropdownMenu.getItems().addAll(
                "Low", "Medium", "High"
        );
        typeDropdownMenu.getItems().add("IDK What Type Supposed To Be");

        // gets users from DB
        assigneeDropdownMenu.getItems().add("Nicoline");
    }
}
