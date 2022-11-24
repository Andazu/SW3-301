package controller;

import com.mongodb.client.model.Filters;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.*;
import model.Task;
import model.User;
import org.bson.types.ObjectId;

import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.mongodb.client.model.Filters.and;

public class OverviewEmployeeController implements Initializable, UIMethods, DatabaseMethods {
    @FXML
    private GridPane taskGrid;
    @FXML
    private BorderPane overviewEmployeeBorderPane;
    @FXML
    private Button refreshFilter;
    @FXML
    private Label dateForShownDay;
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
    private final DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
    private Date date;
    private final int oneDayMS = 86_400_000;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tasks = new ArrayList<>(DatabaseMethods.getTasksFromDB(Filters.eq("active", true), true,"tasks"));
        users = DatabaseMethods.getEmployeesFromDB(false, "users");
        date = new Date();

        dateForShownDay.setText("Today");

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
                "History", "Manager"
        );

        refreshFilter.setVisible(false);

        populateOverviewPageWithTaskBoxes();
    }

    public void populateOverviewPageWithTaskBoxes() {
        String dateToParse = df.format(this.date);
        populateOverviewWithTaskBoxes(taskGrid, frequency, urgency, type, progress, progressValue, employee, dateToParse, false);
    }

    static Task createTaskToDisplay(ArrayList<Object> values) {
        ObjectId id = new ObjectId(values.get(0).toString());
        return new Task(id, (String) values.get(1), (String) values.get(2), (String) values.get(3), (String) values.get(4), (String) values.get(5), (Double) values.get(6), (Boolean) values.get(7), (ArrayList<String>) values.get(8), (ArrayList<String>) values.get(9), (Date) values.get(10));
    }

    public void refreshPage(ActionEvent event) {
        switchScene(overviewEmployeeBorderPane, "overview-employee-page.fxml");
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

        String dateToParse = df.format(this.date);

        populateOverviewWithTaskBoxes(taskGrid, null, null, null,  0.0, null, null, dateToParse, false);
    }

    public void previousDay(ActionEvent event) {
        this.date.setTime(this.date.getTime() - oneDayMS);
        String previousDayDate = df.format(this.date);
        Date todayDate = new Date();
        String todayDateString = df.format(todayDate);

        if (previousDayDate.equals(todayDateString)) {
            dateForShownDay.setText("Today");
        } else {
            dateForShownDay.setText(df.format(this.date));
        }

        populateOverviewWithTaskBoxes(taskGrid, frequency, urgency, type, progress, progressValue, employee, previousDayDate, false);
    }

    public void nextDay(ActionEvent event) {
        this.date.setTime(this.date.getTime() + oneDayMS);
        String nextDayDate = df.format(this.date);
        Date todayDate = new Date();

        if (nextDayDate.equals(df.format(todayDate))) {
            dateForShownDay.setText("Today");
        } else {
            dateForShownDay.setText(df.format(this.date));
        }

        populateOverviewWithTaskBoxes(taskGrid, frequency, urgency, type, progress, progressValue, employee, nextDayDate, false);
    }

    public void changeView(ActionEvent event) {
        if (viewDropdownMenu.getValue().equals("History")) {
            switchScene(overviewEmployeeBorderPane, "overview-history-page.fxml");
        } else if (viewDropdownMenu.getValue().equals("Manager")){
            PinCodeController controller = new PinCodeController();
            makeModalDialog(controller, "manager-pin-code-page.fxml", 300, 400);

            if (controller.isValidPinCode()) {
                switchScene(overviewEmployeeBorderPane, "overview-manager-page.fxml");
            }
        }
    }
}

