package controller;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import model.Task;
import model.User;
import org.bson.Document;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
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
    private Button cancelButton;
    @FXML
    private Button submitButton;
    @FXML
    private BorderPane taskFormBorderPane;
    @FXML
    private Label selectedAssignees;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private GridPane selectedEmployeeGridPane;
    private ArrayList<User> users;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        makeButtonsCancelAndDefault(cancelButton, submitButton);

        frequencyDropdownMenu.getItems().addAll(
                "Once", "Everyday", "Every Other Day", "Every Week", "Every Month"
        );
        urgencyDropdownMenu.getItems().addAll(
                "Low", "Medium", "High"
        );
        typeDropdownMenu.getItems().addAll(
                "Cleaner", "Bartender", "Barista");

        populateTaskFormWithAssigneeBoxes();
    }

    public void cancelAndReturnToOverviewPage(ActionEvent event) {
        switchScene(taskFormBorderPane, "overview-employee-page.fxml");
    }

    public void submitAndReturnToOverviewPage(ActionEvent event) {
        Task createdTask = createTaskFromValuesFromUI();

        ArrayList<User> selectedUsers = DatabaseMethods.getEmployeesFromDB(false, "tempUsers");

        if(selectedUsers.isEmpty()){
            ArrayList<String> assignees = new ArrayList<String>();
            assignees.add("General");
            createdTask.setAssignees(assignees);
        } else {
            for (User user : selectedUsers){
                String userName = user.getFullName();
                ArrayList<String> assignees = createdTask.getAssignees();
                assignees.add(userName);

                createdTask.setAssignees(assignees);
            }
        }
        emptyCollection("tempUsers");

        exportTaskToDatabase(createdTask);
        switchScene(taskFormBorderPane, "overview-employee-page.fxml");
    }

    public String getStringFromTextField(TextField textField) {
        if (textField.getText() != null) {
            return textField.getText();
        } else {
            return "";
        }
    }

    public String getStringFromComboBox(ComboBox comboBox) {
        if (comboBox.getValue() != null) {
            return comboBox.getValue().toString();
        } else {
            return "";
        }
    }

    public Task createTaskFromValuesFromUI() {
        String title = titleTextField.getText();
        String description = descriptionTextField.getText();
        String frequency = frequencyDropdownMenu.getValue().toString();
        String urgency = urgencyDropdownMenu.getValue().toString();
        String type = typeDropdownMenu.getValue().toString();
        LocalDate date = datePicker.getValue();
        ArrayList<String> assignees = new ArrayList<String>();

        return new Task(title, description, frequency, urgency, type, 0, true, assignees, date);
    }

    public void populateTaskFormWithAssigneeBoxes() {
        users = new ArrayList<>(DatabaseMethods.getEmployeesFromDB(false, "users"));

        int columns = 1;
        int rows = 1;

        try {
            if (!users.isEmpty()) {
                for(int i = 0; i < users.size(); i++) {
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("selected-employee-box-page.fxml"));
                    HBox hBox = loader.load();
                    SelectedEmployeeBoxController selectedEmployeeBoxController = loader.getController();
                    selectedEmployeeBoxController.setLabelInBoxUI(users.get(i));

                    selectedEmployeeGridPane.add(hBox, columns, rows);

                    rows++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
