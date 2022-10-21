package controller;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import model.Task;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.text.DecimalFormat;
import java.text.ParseException;

import static controller.DatabaseMethods.getDBColl;
import static controller.DatabaseMethods.getTaskById;

public class TaskController {
    @FXML
    private ProgressBar progressBar;
    @FXML
    private Label taskLabel;

    // update scene builder and ids
    @FXML
    private Button checkmark;

    @FXML
    private ImageView minus;

    @FXML
    private ImageView edit;

    @FXML
    private Button information;

    @FXML
    private ComboBox<String> dropdownMenu;

    @FXML
    private ComboBox<String> dropdownMenuPercent;

    public void setTaskBoxToUI(Task task) {
        progressBar.setProgress(task.getProgress());
        taskLabel.setText(task.getTitle());
        information.setId(task.getId().toString());
        dropdownMenu.getItems().add(task.getAssignees().get(0));
        dropdownMenuPercent.getItems().addAll("25%","50%","75%");
    }
    public void showDescription() {
        Document doc = DatabaseMethods.getTaskById(information.getId());
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(doc.get("title").toString() + " Description");
        alert.setHeaderText(null);
        alert.setContentText(doc.get("description").toString());

        alert.showAndWait();
    }

    public void setTaskToInactive(ActionEvent event) {
        // get button that triggered the action
        Node n = (Node) event.getSource();

        // get node to remove
        Node p = n.getParent();

        // Convert HBox id to ObjectId type
        ObjectId id = new ObjectId(p.getId());

        MongoCollection<Document> collection = getDBColl("tasks");
        collection.updateOne(Filters.eq("_id", id), Updates.set("active", false));

        // remove p from parent's child list
        ((GridPane) p.getParent()).getChildren().remove(p);
    }

    public void updateProgressBar(ActionEvent event) throws ParseException {
        Node n = (Node) event.getSource();

        // get node to remove
        Node p = n.getParent();

        // Convert HBox id to ObjectId type
        ObjectId id = new ObjectId(p.getId());

        MongoCollection<Document> collection = getDBColl("tasks");
        collection.updateOne(Filters.eq("_id", id), Updates.set("progress", (new DecimalFormat("0.0#%").parse(dropdownMenuPercent.getValue()))));
    }
}
