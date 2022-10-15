package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.Task;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class OverviewController implements Initializable {

    @FXML
    private GridPane taskGrid;
    @FXML
    private Button addTaskButton;
    private ArrayList<Task> tasks;
    private Stage stage;
    private Scene scene;
    private Parent root;

    private ArrayList<Task> tasks() {
        ArrayList<Task> taskList = new ArrayList<>();

        ArrayList<String> assignee = new ArrayList<>();
        assignee.add("Nikoline");

        for (int i = 0; i < 10; i++) {

            Task task = new Task("The given task", "A new task", assignee);

            taskList.add(task);
        }

        return taskList;
    }

    public void getTaskForm(ActionEvent event) {
        try {
            root = FXMLLoader.load(getClass().getResource("taskForm.fxml"));
            stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root, 800, 500);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tasks = new ArrayList<>(tasks());

        int columns = 0;
        int rows = 1;

        try {
            for(int i = 0; i < tasks.size(); i++) {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("taskBox.fxml"));
                HBox hBox = loader.load();
                TaskController taskController = loader.getController();
                taskController.setTask(tasks.get(i));

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