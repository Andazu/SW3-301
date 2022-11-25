package controller;

import com.mongodb.client.model.Filters;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Task;
import model.User;
import org.bson.conversions.Bson;

import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Objects;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.ne;

public interface UIMethods {
    default void switchScene(BorderPane pane, String path) {
        try {
            BorderPane borderPane = FXMLLoader.load(getClass().getResource(path));
            pane.getChildren().setAll(borderPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    default void createNewStage(String path, int width, int height) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(path));

            Scene scene = new Scene(fxmlLoader.load(), width, height);

            Stage stage = new Stage();

            stage.initStyle(StageStyle.TRANSPARENT);
            scene.setFill(Color.TRANSPARENT);

            stage.setTitle("The Living Room");
            scene.getStylesheets().add("stylesheet.css");
            stage.setScene(scene);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    default void makeModalDialog(Object controller, String fxmlFile, int width, int height) {
        Stage stage = makeModalStage();
        FXMLLoader loader = makeModalLoader(fxmlFile);
        loader.setController(controller);
        showDialog(loader, stage, width, height);
    }

    default Stage makeModalStage() {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initStyle(StageStyle.TRANSPARENT);
        return dialog;
    }

    default FXMLLoader makeModalLoader(String fxmlFile) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(fxmlFile));
        return loader;
    }

    default void showDialog(FXMLLoader loader, Stage dialog, int width, int height) {
        try {
            BorderPane dialogBox = loader.load();
            Scene dialogScene = new Scene(dialogBox, width, height);
            dialog.setScene(dialogScene);
            dialog.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    default void makeButtonsCancelAndDefault(Button cancel,Button ok) {
        cancel.setCancelButton(true);
        ok.setDefaultButton(true);
    }

    default void closeStage(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    default void informationDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(message);
        alert.show();
    }

    default void errorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        setCssToDialogs(alert, "error-dialog");
        alert.setGraphic(null);
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    default void setCssToDialogs(Alert alert, String cssId) {
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getResource("dialogs.css").toExternalForm());
        dialogPane.getStyleClass().add(cssId);
    }

    default void descriptionInformation(String title, String description) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(description);
        alert.showAndWait();
    }

    default Node returnParentsParentNode(ActionEvent event) {
        Node triggerActionNode = (Node) event.getSource();
        return triggerActionNode.getParent().getParent();
    }

    default void addCssToButtons(Button button, String cssClass) {
        button.getStyleClass().add(cssClass);
    }

    default Bson getFilters(String field, Object value) {
        Bson filter;
        if (!(Objects.equals(value, "")) & value != null) {
            filter = Filters.eq(field, value);
        } else {
            filter = Filters.ne(field, null);
        }
        return filter;
    }

    default void populateOverviewWithTaskBoxes(GridPane taskGrid, String frequency, String urgency, String type, double progress, String progressValue, String employee, String date, boolean isManagerView, boolean isAllTask) {
        if (isAllTask) {
            Bson filter = and(ne("_id", " "));
            ArrayList<Task> tasks = new ArrayList<>(DatabaseMethods.getTasksFromDB(filter, true, "tasks"));

            // Sort tasks based on date
            tasks.sort(Comparator.comparing(Task::getDbDate).reversed());

            int columns = 1;
            int rows = 1;
            String previousDate = (" ");

            try {
                for (Task task : tasks) {
                    String newDate = task.makeDateLabel();

                    // Print new month and year if needed
                    if (!previousDate.equals(newDate)){
                        Separator separator = new Separator(Orientation.HORIZONTAL);
                        Label label = new Label(newDate);
                        label.setPadding(new Insets(20));
                        label.setStyle("-fx-font-size: 18;");

                        VBox vBox = new VBox(separator, label);

                        taskGrid.add(vBox, columns, rows);
                        previousDate = newDate;
                        rows++;
                    }

                    // Print the tasks
                    FXMLLoader loader = new FXMLLoader();

                    if (isManagerView) {
                        loader.setLocation(getClass().getResource("task-box-manager-page.fxml"));
                    } else {
                        loader.setLocation(getClass().getResource("history-task-box-page.fxml"));
                    }

                    VBox vBox = loader.load();
                    vBox.setId(task.getId().toString()); // Store task id as hBox id

                    if (isManagerView) {
                        TaskManagerController taskManagerController = loader.getController();
                        taskManagerController.setTaskBoxToUI(task);
                    } else {
                        HistoryTaskController historyTaskController = loader.getController();
                        historyTaskController.setTaskBoxToUI(task);
                    }

                    taskGrid.add(vBox, columns, rows);
                    rows++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            taskGrid.getChildren().clear();

            DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
            Date parsedDate;

            try {
                parsedDate = df.parse(date);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

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
            //.thenComparing(Task::getUrgency): change the urgency to low: 1, medium: 2, high: 3 to sort on it
            tasks.sort(Comparator.comparing(Task::getDbDate).reversed());

            int columns = 1;
            int rows = 1;

            try {
                for (Task task : tasks) {
                    FXMLLoader loader = new FXMLLoader();
                    if (isManagerView) {
                        loader.setLocation(getClass().getResource("task-box-manager-page.fxml"));
                    } else {
                        loader.setLocation(getClass().getResource("task-box-page.fxml"));
                    }

                    VBox vBox = loader.load();
                    vBox.setId(task.getId().toString()); // Store task id as hBox id

                    if (isManagerView) {
                        TaskManagerController taskController = loader.getController();
                        taskController.setTaskBoxToUI(task);
                    } else {
                        TaskEmployeeController taskController = loader.getController();
                        taskController.setTaskBoxToUI(task);
                    }

                    if (isManagerView) {
                        if (filterEmployee && (date.equals(df.format(task.getDbDate())))) {
                            for (String assignee : task.getAssignees()) {
                                if (employee.equals(assignee)) {
                                    taskGrid.add(vBox, columns, rows);
                                }
                            }
                        } else if (date.equals(df.format(task.getDbDate()))){
                            taskGrid.add(vBox, columns, rows);
                        }
                    } else {
                        if (filterEmployee && (date.equals(df.format(task.getDbDate())) || parsedDate.after(task.getDbDate()))) {
                            for (String assignee : task.getAssignees()) {
                                if (employee.equals(assignee)) {
                                    taskGrid.add(vBox, columns, rows);
                                }
                            }
                        } else if (date.equals(df.format(task.getDbDate())) || parsedDate.after(task.getDbDate())){
                            taskGrid.add(vBox, columns, rows);
                        }
                    }

                    rows++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    default void stdUIForPages(ComboBox<String> frequencyDropdownMenu, ComboBox<String> urgencyDropdownMenu,
        ComboBox<String> typeDropdownMenu, ComboBox<String> progressDropdownMenu, ComboBox<String> assigneeDropdownMenu,
                                          Button refreshFilter, Label dateForShownDay, boolean isOverViewPage) {
        frequencyDropdownMenu.getItems().addAll(
                "", "Once", "Every Day", "Every Other Day", "Every Week", "Every Month"
        );

        urgencyDropdownMenu.getItems().addAll(
                "", "Low", "Medium", "High"
        );

        typeDropdownMenu.getItems().addAll(
                "", "Cleaner", "Bartender", "All"
        );

        if (isOverViewPage) {
            ArrayList<User> users = DatabaseMethods.getEmployeesFromDB(false, "users");

            progressDropdownMenu.getItems().addAll(
                    "", "0%", "25%", "50%", "75%"
            );

            refreshFilter.setVisible(false);

            assigneeDropdownMenu.getItems().addAll("", "General");
            for (User user : users) {
                assigneeDropdownMenu.getItems().add(user.getFullName());
            }

            dateForShownDay.setText("Today");
        }
    }

    default void changeView(ComboBox<String> viewDropdownMenu, BorderPane borderPaneToSwitch) {
        if (viewDropdownMenu.getValue().equals("History")) {
            switchScene(borderPaneToSwitch, "overview-history-page.fxml");
        } else if (viewDropdownMenu.getValue().equals("Manager")){
            PinCodeController controller = new PinCodeController();
            makeModalDialog(controller, "manager-pin-code-page.fxml", 300, 400);

            if (controller.isValidPinCode()) {
                switchScene(borderPaneToSwitch, "overview-manager-page.fxml");
            }
        } else {
            switchScene(borderPaneToSwitch, "overview-employee-page.fxml");
        }
    }

    default void resetFilters(ComboBox<String> frequencyDropdownMenu, ComboBox<String> urgencyDropdownMenu, ComboBox<String> typeDropdownMenu,
        ComboBox<String> progressDropdownMenu, ComboBox<String> assigneeDropdownMenu, DatePicker datePickerFilter, LocalDate localDate) {
        frequencyDropdownMenu.setValue("");
        urgencyDropdownMenu.setValue("");
        typeDropdownMenu.setValue("");
        progressDropdownMenu.setValue("");
        assigneeDropdownMenu.setValue("");
        datePickerFilter.setValue(localDate);
    }

    default void filterSectionLogic(HBox filterOptionsHBox, Button refreshFilter) {
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

    default double progress(String progressValue, ComboBox<String> progressDropdownMenu) throws ParseException {
        double progress = 0;
        if (!Objects.equals(progressValue, progressDropdownMenu.getValue()) & !progressDropdownMenu.getValue().equals("")) {
            if (progressDropdownMenu.getValue().equals("0%")) {
                progress = 0.0;
            } else if (!progressDropdownMenu.getValue().equals("Progress")) {
                progress = (double)(new DecimalFormat("0.0#%").parse(progressDropdownMenu.getValue()));
            }
        }
        return progress;
    }
}