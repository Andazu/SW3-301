package controller;

import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import model.Task;
import org.bson.types.ObjectId;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;

public class OverviewEmployeeController implements Initializable, UIMethods, DatabaseMethods {
    @FXML
    private GridPane taskGrid;

    @FXML
    private BorderPane overviewEmployeeBorderPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) { populateOverviewPageWithTaskBoxes(); }

    public void populateOverviewPageWithTaskBoxes() {
        ArrayList<Task> tasks = new ArrayList<>(DatabaseMethods.getTasksFromDB(true, "tasks"));
        String styleClass = "VBOX";

        int columns = 1;
        int rows = 1;

        try {
            for (Task task : tasks) {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("task-box-page.fxml"));

                VBox vBox = loader.load();
                vBox.setId(task.getId().toString()); // Store task id as hBox id
                vBox.getStyleClass().add(styleClass);

                TaskController taskController = loader.getController();
                taskController.setTaskBoxToUI(task);

                taskGrid.add(vBox, columns, rows);

                rows++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    static Task createTaskToDisplay(ArrayList<Object> values) {
        ObjectId id = new ObjectId(values.get(0).toString());
        return new Task(id, (String) values.get(1), (String) values.get(2), (String) values.get(3), (String) values.get(4), (String) values.get(5), (Double) values.get(6), (Boolean) values.get(7), (ArrayList<String>) values.get(8), (ArrayList<String>) values.get(9), (Date) values.get(10));
    }

    public void refreshPage(ActionEvent event) {
        switchScene(overviewEmployeeBorderPane, "overview-employee-page.fxml");
    }
}

