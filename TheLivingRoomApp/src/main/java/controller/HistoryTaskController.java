package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import model.Task;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

public class HistoryTaskController implements DatabaseMethods, UIMethods, Initializable {

    @FXML
    private Button recoverButton;
    @FXML
    private Button deleteButton;

    @FXML
    private Button informationButton;

    @FXML
    private Label deadlineLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addCssToButtons(deleteButton, "cancel-button");
        addCssToButtons(recoverButton, "submit-button");
    }

    public void setTaskBoxToUI(Task task) {
        informationButton.setText(task.getTitle());
        deadlineLabel.setText(task.getDbDate().toString());
    }

    public void showDescription(ActionEvent event) {
        Node parent = returnParentsParentNode(event);

        DescriptionController controller = new DescriptionController(parent.getId());
        makeModalDialog(controller, "description-page.fxml", 700, 450);
    }

    public void setTaskToActive(ActionEvent event) {
        Node parent = returnParentsParentNode(event);
        updateTask(parent.getId(), "tasks", true);

        // remove p from parent's child list
        ((GridPane) parent.getParent()).getChildren().remove(parent);
    }

    public void deleteTaskFromDB(ActionEvent event){
        Node parent = returnParentsParentNode(event);
        deleteFromDB(parent.getId(), "tasks");
        ((GridPane) parent.getParent()).getChildren().remove(parent);
    }
}
