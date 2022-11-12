package controller;

import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import model.Task;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import java.io.IOException;
import java.net.URL;
import java.util.*;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.ne;

public class OverviewHistoryController implements Initializable, UIMethods, DatabaseMethods{
    @FXML
    private GridPane taskGrid;

    @FXML
    private BorderPane overviewHistoryBorderPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        populateOverviewPageWithTaskBoxes();
    }

    public void populateOverviewPageWithTaskBoxes() {
        Bson filter = and(ne("_id", " "));
        ArrayList<Task> tasks = new ArrayList<>(DatabaseMethods.getTasksFromDB(filter, false, "tasks"));

        tasks.sort(Comparator.comparing(Task::getDbDate).reversed());

        int columns = 1;
        int rows = 1;

        try {
            for (Task task : tasks) {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("history-task-box-page.fxml"));

                VBox vBox = loader.load();
                vBox.setId(task.getId().toString()); // Store task id as hBox id

                HistoryTaskController historyTaskController = loader.getController();
                historyTaskController.setTaskBoxToUI(task);

                taskGrid.add(vBox, columns, rows);

                rows++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void refreshPage(ActionEvent event) {
        switchScene(overviewHistoryBorderPane, "overview-history-page.fxml");
    }
}
