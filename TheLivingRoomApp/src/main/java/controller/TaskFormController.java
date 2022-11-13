package controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import model.*;
import java.net.URL;
import java.util.*;

public class TaskFormController implements Initializable, UIMethods, DatabaseMethods {
    @FXML
    private TextField titleTextField;
    @FXML
    private TextField descriptionTextField;
    @FXML
    private DatePicker datePicker;
    @FXML
    private ComboBox<String> frequencyDropdownMenu;
    @FXML
    private ComboBox<String> urgencyDropdownMenu;
    @FXML
    private ComboBox<String> typeDropdownMenu;
    @FXML
    private Button cancelButton;
    @FXML
    private Button submitButton;
    @FXML
    private BorderPane taskFormBorderPane;
    @FXML
    private GridPane selectedEmployeeGridPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addCssToButtons(cancelButton, "cancel-button");
        addCssToButtons(submitButton, "submit-button");

        makeButtonsCancelAndDefault(cancelButton, submitButton);

        frequencyDropdownMenu.getItems().addAll(
                "Once", "Every day", "Every Other Day", "Every Week", "Every Month"
        );
        urgencyDropdownMenu.getItems().addAll(
                "Low", "Medium", "High"
        );
        typeDropdownMenu.getItems().addAll(
                "Cleaner", "Bartender");

        populateTaskFormWithAssigneeBoxes();
    }

    public void cancelAndReturnToOverviewPage(ActionEvent event) {
        switchScene(taskFormBorderPane, "overview-manager-page.fxml");
    }

    public void submitAndReturnToOverviewPage(ActionEvent event) {
        Task createdTask = new Task(0.0, true);

        createdTask.setDescription(descriptionTextField.getText());
        createdTask.setDate(datePicker.getValue());
        createdTask.setType(typeDropdownMenu.getValue());

        boolean validTitle = createdTask.setTitle(titleTextField.getText());
        boolean validFrequency = createdTask.setFrequency(frequencyDropdownMenu.getValue());
        boolean validUrgency = createdTask.setUrgency(urgencyDropdownMenu.getValue());

        if (validTitle & validFrequency & validUrgency) {
            ArrayList<String> selectedUsers = getSelectedAssignees();

            ArrayList<String> assignees = new ArrayList<>();
            if(selectedUsers.isEmpty()){
                assignees.add("General");
                createdTask.setAssignees(assignees);
            } else {
                for (String user : selectedUsers){
                    //assignees = createdTask.getAssignees();
                    assignees.add(user);

                    createdTask.setAssignees(assignees);
                }
            }
            exportTaskToDatabase(createdTask, "tasks");
            switchScene(taskFormBorderPane, "overview-manager-page.fxml");
        } else {
            errorDialog("Empty Fields", "The following fields cannot be empty: Title, Frequency, Urgency, or Date");
        }
    }

    public void populateTaskFormWithAssigneeBoxes() {
        ArrayList<User> users = new ArrayList<>(DatabaseMethods.getEmployeesFromDB(false, "users"));

        int columns = 1;
        int rows = 1;

        if (!users.isEmpty()) {
            for (User user : users) {
                HBox hBox = assigneeBox(user);

                selectedEmployeeGridPane.add(hBox, columns, rows);

                rows++;
            }
        } else {
            selectedEmployeeGridPane.add(setNoAssigneesLabel(), 1, 1);
        }
    }

    public HBox setNoAssigneesLabel() {
        Label noAssigneesCreatedLabel = new Label("No Assignees Created");
        noAssigneesCreatedLabel.setMinWidth(225);
        noAssigneesCreatedLabel.setMinHeight(50);
        noAssigneesCreatedLabel.setWrapText(true);
        noAssigneesCreatedLabel.setMaxWidth(200);
        noAssigneesCreatedLabel.setAlignment(Pos.CENTER);

        HBox hBox = new HBox(noAssigneesCreatedLabel);
        hBox.setMinWidth(200);

        return hBox;
    }

    public HBox assigneeBox(User user) {
        HBox hBoxName = hBoxName(user);

        HBox hBoxRole = hBoxRole(user);

        HBox hBoxCheckBox = hBoxCheckBox();

        HBox hBox = new HBox(hBoxName, hBoxRole, hBoxCheckBox);

        return hBoxAll(hBox);
    }

    public HBox hBoxName(User user) {
        Label fullName = new Label(user.getFullName());
        HBox hBoxName = new HBox(fullName);
        hBoxName.setAlignment(Pos.CENTER);
        hBoxName.setPrefWidth(120);
        hBoxName.setPrefHeight(50);
        return hBoxName;
    }

    public HBox hBoxRole(User user) {
        Label role = new Label(user.getRole());
        HBox hBoxRole = new HBox(role);
        hBoxRole.setAlignment(Pos.CENTER);
        hBoxRole.setPrefWidth(55);
        hBoxRole.setPrefHeight(50);
        return hBoxRole;
    }

    public HBox hBoxCheckBox() {
        CheckBox checkBox = new CheckBox();
        HBox hBoxCheckBox = new HBox(checkBox);
        hBoxCheckBox.setAlignment(Pos.CENTER);
        hBoxCheckBox.setPrefWidth(50);
        hBoxCheckBox.setPrefHeight(50);
        return hBoxCheckBox;
    }

    public HBox hBoxAll(HBox hBox) {
        hBox.setPrefWidth(225);
        hBox.setPrefHeight(50);
        hBox.setMinWidth(Region.USE_PREF_SIZE);
        hBox.setMinHeight(Region.USE_PREF_SIZE);
        hBox.setMaxWidth(Region.USE_PREF_SIZE);
        hBox.setMaxHeight(Region.USE_PREF_SIZE);
        hBox.setAlignment(Pos.CENTER);
        return hBox;
    }

    public ArrayList<String> getSelectedAssignees() {
        ArrayList<String> assignees = new ArrayList<>();

        ObservableList<Node> hBoxOuter = selectedEmployeeGridPane.getChildren();

        for (Node node : hBoxOuter) {
            HBox hBoxMiddle = (HBox) node;
            ObservableList<Node> hBoxChildren = hBoxMiddle.getChildren();

            HBox hBoxInner0 = (HBox) hBoxChildren.get(0);
            Label labelFullName = (Label) hBoxInner0.getChildren().get(0);

            HBox hBoxInner2 = (HBox) hBoxChildren.get(2);
            CheckBox checkBox = (CheckBox) hBoxInner2.getChildren().get(0);
            if (checkBox.isSelected()) {
                assignees.add(labelFullName.getText());
            }
        }

        return assignees;
    }
}
