package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import model.Task;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import static com.mongodb.client.model.Filters.eq;

public class OverviewController implements Initializable, UIMethods, DatabaseMethods {
    @FXML
    private GridPane taskGrid;
    @FXML
    private Button addTaskButton;
    private ArrayList<Task> tasks;
    @FXML
    private BorderPane overviewEmployeeBorderPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) { populateOverviewPageWithTaskBoxes(); }

    public void openTaskFormPage(ActionEvent event) {
        switchScene(overviewEmployeeBorderPane,"task-form-page.fxml");
    }

    public void populateOverviewPageWithTaskBoxes() {
        tasks = new ArrayList<>(DatabaseMethods.getTasksFromDB(true));

        int columns = 1;
        int rows = 1;

        try {
            for(int i = 0; i < tasks.size(); i++) {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("task-box-page.fxml"));

                HBox hBox = loader.load();
                hBox.setId(tasks.get(i).getId().toString()); // Store task id as hBox id

                TaskController taskController = loader.getController();
                taskController.setTaskBoxToUI(tasks.get(i));

                rows++;

                taskGrid.add(hBox, columns, rows);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    static Task createTaskToDisplay(ArrayList<Object> values) {
        return new Task((ObjectId) values.get(0), (String) values.get(1), (String) values.get(2), (ArrayList<String>) values.get(9));
    }
}

