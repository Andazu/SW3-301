package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import model.Task;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.text.ParseException;

public class TaskController implements DatabaseMethods, UIMethods {
    @FXML
    private ProgressBar progressBar;
    @FXML
    private Label taskLabel;
    @FXML
    private Button checkmark;
    @FXML
    private Button information;
    @FXML
    private ComboBox<String> dropdownMenuPercent;
    @FXML
    private ComboBox<String> dropdownMenu;

    public void setTaskBoxToUI(Task task) {
        progressBar.setProgress(task.getProgress());
        taskLabel.setText(task.getTitle());
        dropdownMenuPercent.getItems().addAll("25%","50%","75%");
        //check.setId();
        //minus.setId();
        //edit.setId();
        information.setId(task.getId().toString());
    }
    public void showDescription() {
        Document doc = DatabaseMethods.getDocumentById(information.getId(), "tasks");

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(doc.get("title").toString() + " Description");
        alert.setHeaderText(null);
        alert.setContentText(doc.get("description").toString());

        alert.showAndWait();
    }

    public void setTaskToInactive(ActionEvent event) {
        // get button that triggered the action
        Node node = (Node) event.getSource();

        // get node to remove
        Node parent = node.getParent();

        updateTask(parent);

        // remove p from parent's child list
        ((GridPane) parent.getParent()).getChildren().remove(parent);
    }

    public void updateProgressBar(ActionEvent event) throws ParseException {
        Node node = (Node) event.getSource();

        // get node to remove
        Node parent = node.getParent();

        updateProgressBarInDB(parent, dropdownMenuPercent);
    }

    public void addCommentToTask(ActionEvent event) {
        Node node = (Node) event.getSource();

        Node parent = node.getParent();

        makeModalDialog("add-comment-page.fxml", 731, 500, parent.getId());
    }
}
