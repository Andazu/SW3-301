package controller;

import com.mongodb.client.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.MongoDBLocal;
import model.Task;
import org.bson.Document;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import static com.mongodb.client.model.Filters.eq;

public class OverviewController implements Initializable {

    @FXML
    private GridPane taskGrid;
    @FXML
    private Button addTaskButton;
    private ArrayList<Task> tasks;
    private Stage stage;
    private Scene scene;
    private Parent root;

    private ArrayList<Task> tasksFromDB() {
        ArrayList<Task> taskList = new ArrayList<>();

        MongoCollection<Document> coll = MongoDBLocal.getTaskColl();

        for (Document doc : coll.find(eq("active", true))) {//Task dbTask = new Task();
            ArrayList<Object> values = new ArrayList<>(doc.values());

            taskList.add(Task.createTaskToDisplay(values));
        }
        return taskList;
    }

    public void getTaskForm(ActionEvent event) {
        try {
            root = FXMLLoader.load(getClass().getResource("taskForm.fxml"));
            stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root, 1024, 700);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tasks = new ArrayList<>(tasksFromDB());

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