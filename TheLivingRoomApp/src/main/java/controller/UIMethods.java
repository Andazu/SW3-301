package controller;

import com.mongodb.client.model.Filters;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Task;
import org.bson.conversions.Bson;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

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

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getResource("dialogs.css").toExternalForm());
        dialogPane.setId("error-dialog");
        dialogPane.getStyleClass().add("error-dialog");

        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
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

    default TextInputDialog managerPinCodeLogin(BorderPane borderpane) {
        TextInputDialog td = new TextInputDialog();
        td.setTitle("Manager Login");
        td.setHeaderText("");
        td.setGraphic(null);

        // Gør så kun "Enter" knappen eller tryk på ok tjekker PIN-koden
        td.getDialogPane().lookupButton(ButtonType.OK).addEventFilter(
            ActionEvent.ACTION, event -> {
                if (td.getEditor().getText().equals("1234")) {
                    switchScene(borderpane, "overview-manager-page.fxml");
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Wrong PIN code");
                    alert.setGraphic(null);
                    alert.setHeaderText(null);
                    alert.show();
                }
            }
        );

        td.showAndWait();
        return td;
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

    default void populateOverviewWithTaskBoxes(GridPane taskGrid, String frequency, String urgency, String type, double progress, String progressValue, String employee, String date, boolean isManagerView) {
        taskGrid.getChildren().clear();

        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        Date parsedDate = new Date();

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

                if (filterEmployee &&
                        (date.equals(df.format(task.getDbDate())) ||
                         parsedDate.after(task.getDbDate())       )) {
                    for (String assignee : task.getAssignees()) {
                        if (employee.equals(assignee)) {
                            taskGrid.add(vBox, columns, rows);
                        }
                    }
                } else if (date.equals(df.format(task.getDbDate())) || parsedDate.after(task.getDbDate())){
                    taskGrid.add(vBox, columns, rows);
                }

                rows++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}