package controller;

import com.mongodb.client.model.Filters;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.*;
import model.Task;
import model.User;
import org.bson.BsonDocument;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.*;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

public class OverviewManagerController implements Initializable, UIMethods, DatabaseMethods {
    @FXML
    private GridPane taskGrid;
    @FXML
    private BorderPane overviewManagerBorderPane;
    @FXML
    private Button filterButton;
    @FXML
    private HBox filterOptionsHBox;
    @FXML
    private ComboBox<String> frequencyDropdownMenu;
    @FXML
    private ComboBox<String> urgencyDropdownMenu;
    @FXML
    private ComboBox<String> typeDropdownMenu;
    @FXML
    private ComboBox<String> progressDropdownMenu;
    @FXML
    private ComboBox<String> assigneeDropdownMenu;
    private String frequency;
    private String urgency;
    private String type;
    private double progress;
    private String progressValue;
    private String employee;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        frequencyDropdownMenu.getItems().addAll(
                "", "Once", "Every Day", "Every Other Day", "Every Week", "Every Month"
        );

        urgencyDropdownMenu.getItems().addAll(
                "", "Low", "Medium", "High"
        );

        typeDropdownMenu.getItems().addAll(
                "", "All", "Cleaner", "Bartender"
        );

        progressDropdownMenu.getItems().addAll(
                "", "0%", "25%", "50%", "75%"
        );

        ArrayList<User> users = DatabaseMethods.getEmployeesFromDB(false, "users");

        assigneeDropdownMenu.getItems().addAll("", "General");
        for (User user : users) {
            assigneeDropdownMenu.getItems().add(user.getFullName());
        }

        populateOverviewPageWithTaskBoxes();
    }

    public void openTaskFormPage(ActionEvent event) {
        switchScene(overviewManagerBorderPane, "task-form-page.fxml");
    }

    private Bson getFilters(String field, Object value) {
        Bson filter;
        if (!(Objects.equals(value, "")) & value != null) {
            filter = Filters.eq(field, value);
        } else {
            filter = Filters.ne(field, null);
        }
        return filter;
    }

    public void populateOverviewPageWithTaskBoxes() {
        taskGrid.getChildren().clear();

        Bson frequencyFilter = getFilters("frequency", frequency);
        Bson urgencyFilter = getFilters("urgency", urgency);
        Bson typeFilter = getFilters("type", type);

        Bson progressFilter;
        if (progressValue == null || progressValue.equals("")) {
            progressFilter = getFilters("progress", null);
        } else {
            progressFilter = getFilters("progress", progress);
        }

        boolean filterEmployee = !(Objects.equals(employee, "")) & employee != null;

        Bson filter = Filters.and(frequencyFilter, urgencyFilter, typeFilter, progressFilter);
        ArrayList<Task> tasks = new ArrayList<>(DatabaseMethods.getTasksFromDB(filter, true, "tasks"));

        int columns = 1;
        int rows = 1;

        try {
            for (Task task : tasks) {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("task-box-manager-page.fxml"));

                VBox vBox = loader.load();
                vBox.setId(task.getId().toString()); // Store task id as hBox id

                TaskManagerController taskController = loader.getController();
                taskController.setTaskBoxToUI(task);

                if (filterEmployee) {
                    for (String assignee : task.getAssignees()) {
                        if (employee.equals(assignee)) {
                            taskGrid.add(vBox, columns, rows);
                        }
                    }
                } else {
                    taskGrid.add(vBox, columns, rows);
                }

                rows++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void refreshPage(ActionEvent event) {
        switchScene(overviewManagerBorderPane, "overview-manager-page.fxml");
    }

    public void filterTasks(ActionEvent event) {
        if (filterOptionsHBox.isVisible()) {
            filterOptionsHBox.setVisible(false);
            filterOptionsHBox.setPrefHeight(0);
        } else {
            filterOptionsHBox.setVisible(true);
            filterOptionsHBox.setPrefHeight(75);
        }
    }

    public void frequencyFilter(ActionEvent event) {
        if (!Objects.equals(frequency, frequencyDropdownMenu.getValue())) {
            frequency = frequencyDropdownMenu.getValue();
            populateOverviewPageWithTaskBoxes();
        }
    }

    public void urgencyFilter(ActionEvent event) {
        if (!Objects.equals(urgency, urgencyDropdownMenu.getValue())) {
            urgency = urgencyDropdownMenu.getValue();
            populateOverviewPageWithTaskBoxes();
        }
    }

    public void typeFilter(ActionEvent event) {
        if (!Objects.equals(type, typeDropdownMenu.getValue())) {
            type = typeDropdownMenu.getValue();
            populateOverviewPageWithTaskBoxes();
        }
    }

    public void progressFilter(ActionEvent event) throws ParseException {
        if (!Objects.equals(progressValue, progressDropdownMenu.getValue()) & !progressDropdownMenu.getValue().equals("")) {
            if (progressDropdownMenu.getValue().equals("0%")) {
                progress = 0.0;
            } else {
                progress = (double)(new DecimalFormat("0.0#%").parse(progressDropdownMenu.getValue()));
            }
            progressValue = progressDropdownMenu.getValue();
            populateOverviewPageWithTaskBoxes();
        } else {
            progressValue = progressDropdownMenu.getValue();
            populateOverviewPageWithTaskBoxes();
        }
    }

    public void assigneesFilter(ActionEvent event) {
        if (!Objects.equals(employee, assigneeDropdownMenu.getValue())) {
            employee = assigneeDropdownMenu.getValue();
            populateOverviewPageWithTaskBoxes();
        }
    }
}

