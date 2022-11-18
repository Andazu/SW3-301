package controller;

import com.mongodb.client.model.Filters;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.layout.*;
import model.Task;
import model.User;
import org.bson.BsonDocument;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

public class OverviewManagerController implements Initializable, UIMethods, DatabaseMethods {
    @FXML
    private GridPane taskGrid;
    @FXML
    private BorderPane overviewManagerBorderPane;
    @FXML
    private Button refreshFilter;
    @FXML
    private DatePicker datePickerFilter;
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
    private ArrayList<Task> tasks;
    private DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    private Date date = new Date();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ArrayList<Task> taskArrayList = new ArrayList<>(DatabaseMethods.getTasksFromDB(Filters.eq("active", true), true,"tasks"));
        tasks = taskArrayList;
        for (Task task: tasks) {
            Date dato = new Date();
            if (df.format(dato).equals(df.format(task.getDbDate()))) {
                System.out.println("Luksus");
            }
        }


        frequencyDropdownMenu.getItems().addAll(
                "", "Once", "Every Day", "Every Other Day", "Every Week", "Every Month"
        );

        urgencyDropdownMenu.getItems().addAll(
                "", "Low", "Medium", "High"
        );

        typeDropdownMenu.getItems().addAll(
                "", "Cleaner", "Bartender"
        );

        progressDropdownMenu.getItems().addAll(
                "", "0%", "25%", "50%", "75%"
        );

        ArrayList<User> users = DatabaseMethods.getEmployeesFromDB(false, "users");

        assigneeDropdownMenu.getItems().addAll("", "General");
        for (User user : users) {
            assigneeDropdownMenu.getItems().add(user.getFullName());
        }

        refreshFilter.setVisible(false);

        populateOverviewPageWithTaskBoxes();
    }

    public void openTaskFormPage(ActionEvent event) {
        switchScene(overviewManagerBorderPane, "task-form-page.fxml");
    }

    public void populateOverviewPageWithTaskBoxes() {
        if (datePickerFilter.getValue() != null) {
            populateOverviewWithTaskBoxes(taskGrid, frequency, urgency, type, progress, progressValue, employee, Date.from(datePickerFilter.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()), df);
        } else {
            populateOverviewWithTaskBoxes(taskGrid, frequency, urgency, type, progress, progressValue, employee, date, df);

        }
    }

    public void refreshPage(ActionEvent event) {
        switchScene(overviewManagerBorderPane, "overview-manager-page.fxml");
    }

    public void filterTasks(ActionEvent event) {
        if (filterOptionsHBox.isVisible()) {
            filterOptionsHBox.setVisible(false);
            filterOptionsHBox.setPrefHeight(0);
            refreshFilter.setVisible(false);
        } else {
            filterOptionsHBox.setVisible(true);
            filterOptionsHBox.setPrefHeight(75);
            refreshFilter.setVisible(true);
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
            } else if (!progressDropdownMenu.getValue().equals("Progress")) {
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

    public void refreshFilters(ActionEvent event) {
        ListCell<String> frequencyText = new ListCell<>();
        frequencyText.setText("Frequency");
        ListCell<String> urgencyText = new ListCell<>();
        urgencyText.setText("Urgency");
        ListCell<String> typeText = new ListCell<>();
        typeText.setText("Type");
        ListCell<String> progressText = new ListCell<>();
        progressText.setText("Progress");
        ListCell<String> assigneeText = new ListCell<>();
        assigneeText.setText("Assignee");

        frequencyDropdownMenu.setButtonCell(frequencyText);
        urgencyDropdownMenu.setButtonCell(urgencyText);
        typeDropdownMenu.setButtonCell(typeText);
        progressDropdownMenu.setButtonCell(progressText);
        assigneeDropdownMenu.setButtonCell(assigneeText);

        populateOverviewWithTaskBoxes(taskGrid, null, null, null,  0.0, null, null, new Date(), df);
    }

    public void dateFilter(ActionEvent event) {
        populateOverviewPageWithTaskBoxes();
    }
}

