package controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import model.Task;
import model.User;
import org.bson.Document;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class EditTaskController implements Initializable, UIMethods, DatabaseMethods {
    @FXML
    private Button cancelButton;
    @FXML
    private Button submitButton;
    @FXML
    private TextField titleTextField;
    @FXML
    private TextArea descriptionTextArea;
    @FXML
    private DatePicker datePicker;
    @FXML
    private ComboBox<String> frequencyDropdownMenu;
    @FXML
    private ComboBox<String> urgencyDropdownMenu;
    @FXML
    private ComboBox<String> typeDropdownMenu;
    @FXML
    private GridPane selectedEmployeeGridPane;
    @FXML
    private BorderPane taskEditBorderPane;
    @FXML
    private GridPane commentGridPane;
    private final String id;

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

        descriptionTextArea.setWrapText(true);
        setValuesInFields();
        displayComments();
    }

    public void displayComments() {
        ArrayList<String> comments = new ArrayList<>(DatabaseMethods.getCommentsFromDB(id, "tasks"));

        int columns = 1;
        int rows = 1;

        try {
            for (String comment : comments) {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("comment-box-page.fxml"));

                HBox hBox = loader.load();
                hBox.setPrefWidth(210);

                CommentBoxController commentBoxController = loader.getController();
                commentBoxController.setCommentToUI(comment, 205);

                commentGridPane.add(hBox, columns, rows);

                rows++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void cancelButtonLogic(ActionEvent event) {
        closeStage(event);
    }

    public void submitAndUpdateTask(ActionEvent event) {
        Task createdTask = new Task(0.0, true);

        createdTask.setDescription(descriptionTextArea.getText());
        createdTask.setDate(datePicker.getValue());
        createdTask.setType(typeDropdownMenu.getValue());

        boolean validTitle = createdTask.setTitle(titleTextField.getText());
        boolean validFrequency = createdTask.setFrequency(frequencyDropdownMenu.getValue());
        boolean validUrgency = createdTask.setUrgency(urgencyDropdownMenu.getValue());

        if (validTitle & validFrequency & validUrgency) {
            ArrayList<String> selectedUsers = getSelectedAssignees();

            ArrayList<String> assignees = new ArrayList<>();
            if (selectedUsers.isEmpty() || selectedUsers.contains("General")) {
                assignees.add("General");
                createdTask.setAssignees(assignees);
            } else {
                assignees.addAll(selectedUsers);
                createdTask.setAssignees(assignees);
            }
            updateTask(id, "tasks", createdTask);
            switchScene(taskEditBorderPane, "overview-manager-page.fxml");
        } else {
            errorDialog("Empty Fields", "The following fields cannot be empty: Title, Frequency, Urgency, or Date");
        }
    }

    private void setValuesInFields() {
        Document doc = DatabaseMethods.getDocumentById(id, "tasks");

        titleTextField.setText(doc.get("title").toString());
        descriptionTextArea.setText(doc.get("description").toString());
        datePicker.setValue(convertToLocalDateViaInstant((Date) doc.get("date")));
        frequencyDropdownMenu.setValue(doc.get("frequency").toString());
        urgencyDropdownMenu.setValue(doc.get("urgency").toString());
        typeDropdownMenu.setValue(doc.get("type").toString());

        displaySelectedAssignees(doc);
    }

    private LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    private void displaySelectedAssignees(Document doc) {
        ArrayList<String> assignees = new ArrayList<>((Collection<String>) doc.get("assignees"));
        ArrayList<User> users = createUserList();

        int columns = 1;
        int rows = 1;

        for (User user : users) {
            HBox hBox;
            if (assignees.contains(user.getFullName())) {
                hBox = assigneeBox(user, true);
            } else {
                hBox = assigneeBox(user, false);
            }

            selectedEmployeeGridPane.add(hBox, columns, rows);

            rows++;
        }
    }

    private ArrayList<User> createUserList() {
        User generalUser = new User();
        generalUser.setFullName("General");
        generalUser.setRole("All");

        ArrayList<User> users = new ArrayList<>();
        users.add(generalUser);
        users.addAll(DatabaseMethods.getEmployeesFromDB(false, "users"));
        return users;
    }

    private HBox assigneeBox(User user, boolean isChecked) {
        HBox hBoxName = hBoxName(user);

        HBox hBoxRole = hBoxRole(user);

        HBox hBoxCheckBox = hBoxCheckBox(isChecked);

        HBox hBox = new HBox(hBoxName, hBoxRole, hBoxCheckBox);

        return hBoxAll(hBox);
    }

    private HBox hBoxName(User user) {
        Label fullName = new Label(user.getFullName());
        HBox hBoxName = new HBox(fullName);
        hBoxName.setAlignment(Pos.CENTER);
        hBoxName.setPrefWidth(120);
        hBoxName.setPrefHeight(50);
        return hBoxName;
    }

    private HBox hBoxRole(User user) {
        Label role = new Label(user.getRole());
        HBox hBoxRole = new HBox(role);
        hBoxRole.setAlignment(Pos.CENTER);
        hBoxRole.setPrefWidth(55);
        hBoxRole.setPrefHeight(50);
        return hBoxRole;
    }

    private HBox hBoxCheckBox(boolean isChecked) {
        CheckBox checkBox = new CheckBox();
        HBox hBoxCheckBox = new HBox(checkBox);
        hBoxCheckBox.setAlignment(Pos.CENTER);
        hBoxCheckBox.setPrefWidth(50);
        hBoxCheckBox.setPrefHeight(50);
        if (isChecked) {
            checkBox.setSelected(true);
        }
        return hBoxCheckBox;
    }

    private HBox hBoxAll(HBox hBox) {
        hBox.setPrefWidth(225);
        hBox.setPrefHeight(50);
        hBox.setMinWidth(Region.USE_PREF_SIZE);
        hBox.setMinHeight(Region.USE_PREF_SIZE);
        hBox.setMaxWidth(Region.USE_PREF_SIZE);
        hBox.setMaxHeight(Region.USE_PREF_SIZE);
        hBox.setAlignment(Pos.CENTER);
        return hBox;
    }

    private ArrayList<String> getSelectedAssignees() {
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

    public EditTaskController(String id) {
        this.id = id;
    }
}
