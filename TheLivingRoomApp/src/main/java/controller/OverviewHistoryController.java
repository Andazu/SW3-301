package controller;

import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.*;
import model.Task;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.ne;

public class OverviewHistoryController implements Initializable, UIMethods, DatabaseMethods{
    @FXML
    private GridPane taskGrid;
    @FXML
    private BorderPane overviewHistoryBorderPane;
    @FXML
    private ComboBox<String> viewDropdownMenu;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        populateOverviewPageWithTaskBoxes();

        viewDropdownMenu.getItems().addAll(
                "Employee", "Manager"
        );
    }

    public void populateOverviewPageWithTaskBoxes() {
        Bson filter = and(ne("_id", " "));
        ArrayList<Task> tasks = new ArrayList<>(DatabaseMethods.getTasksFromDB(filter, false, "tasks"));

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

    public void changeView(ActionEvent event) {
        if (viewDropdownMenu.getValue().equals("Employee")) {
            switchScene(overviewHistoryBorderPane, "overview-employee-page.fxml");
        } else if (viewDropdownMenu.getValue().equals("Manager")){
            PinCodeController controller = new PinCodeController();
            makeModalDialog(controller, "manager-pin-code-page.fxml", 300, 400);

            if (controller.isValidPinCode()) {
                switchScene(overviewHistoryBorderPane, "overview-manager-page.fxml");
            }
        }
    }
}
