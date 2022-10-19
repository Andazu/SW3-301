package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import model.Task;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import static com.mongodb.client.model.Filters.eq;

public class OverviewController implements Initializable {
    private StageMethods stageMethods = new StageMethods();
    @FXML
    private GridPane taskGrid;
    @FXML
    private Button addTaskButton;
    private ArrayList<Task> tasks;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        populateOverviewPageWithTaskBoxes();
    }

    public void openTaskFormPage(ActionEvent event) {
        stageMethods.switchStage(event, "task-form-page.fxml");
    }

    public void populateOverviewPageWithTaskBoxes() {
        tasks = new ArrayList<>(Task.getActiveTasksFromDB());

        int columns = 0;
        int rows = 1;

        try {
            for(int i = 0; i < tasks.size(); i++) {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("task-box-page.fxml"));
                HBox hBox = loader.load();
                TaskController taskController = loader.getController();
                taskController.setTaskBoxToUI(tasks.get(i));

                if (columns == 1) {
                    columns = 0;
                    rows++;
                }

                taskGrid.add(hBox,columns++,rows);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}