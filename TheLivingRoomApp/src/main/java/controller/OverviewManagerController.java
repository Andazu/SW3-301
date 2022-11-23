package controller;

import com.mongodb.client.model.Filters;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import model.Task;
import model.User;

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
    private Label dateForShownDay;
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
    @FXML
    private ComboBox<String> viewDropdownMenu;
    private String frequency;
    private String urgency;
    private String type;
    private double progress;
    private String progressValue;
    private String employee;
    private ArrayList<Task> tasks;
    private ArrayList<User> users;
    private DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
    private Date date;
    private LocalDate localDate = LocalDate.now();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tasks = new ArrayList<>(DatabaseMethods.getTasksFromDB(Filters.eq("active", true), true,"tasks"));
        users = DatabaseMethods.getEmployeesFromDB(false, "users");
        date = new Date();
        
        dateForShownDay.setText("Today");

        datePickerFilter.setValue(localDate);

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

        assigneeDropdownMenu.getItems().addAll("", "General");
        for (User user : users) {
            assigneeDropdownMenu.getItems().add(user.getFullName());
        }

        viewDropdownMenu.getItems().addAll(
                "History", "Employee"
        );

        refreshFilter.setVisible(false);

        populateOverviewPageWithTaskBoxes();
    }

    public void openTaskFormPage(ActionEvent event) {
        switchScene(overviewManagerBorderPane, "task-form-page.fxml");
    }

    public void openTeamManagerPage(ActionEvent event) {
        switchScene(overviewManagerBorderPane, "manage-team-page.fxml");
    }

    public void openAllTasksPage(ActionEvent event) {
        switchScene(overviewManagerBorderPane, "overview-all-tasks-page.fxml");
    }


    public void populateOverviewPageWithTaskBoxes() {
        Date dateToFormat = Date.from(datePickerFilter.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        String dateToParse = df.format(dateToFormat);

        populateOverviewWithTaskBoxes(taskGrid, frequency, urgency, type, progress, progressValue, employee, dateToParse, true);
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
        frequencyDropdownMenu.setValue("");
        urgencyDropdownMenu.setValue("");
        typeDropdownMenu.setValue("");
        progressDropdownMenu.setValue("");
        assigneeDropdownMenu.setValue("");
        datePickerFilter.setValue(localDate);

        String dateToParse = df.format(new Date());
        dateForShownDay.setText(dateToParse);

        populateOverviewWithTaskBoxes(taskGrid, null, null, null,  0.0, null, null, dateToParse, true);
    }

    public void dateFilter(ActionEvent event) {
        Date dateToFormat = Date.from(datePickerFilter.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        String dateToShow = df.format(dateToFormat);
        dateForShownDay.setText(dateToShow);

        if (dateToShow.equals(df.format(new Date()))) {
            dateForShownDay.setText("Today");
        } else {
            dateForShownDay.setText(dateToShow);
        }

        populateOverviewPageWithTaskBoxes();
    }

    public void changeView(ActionEvent event) {
        if (viewDropdownMenu.getValue() == "History") {
            switchScene(overviewManagerBorderPane, "overview-history-page.fxml");
        } else if (viewDropdownMenu.getValue() == "Employee"){
            switchScene(overviewManagerBorderPane, "overview-employee-page.fxml");
        }
    }
}

