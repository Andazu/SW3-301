package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import model.Task;

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
    private ImageView information;

    @FXML
    private ComboBox<String> dropdownMenu;

    public void setTaskBoxToUI(Task task) {
        progressBar.setProgress(0);
        taskLabel.setText(task.getTitle());
        //check.setId();
        //minus.setId();
        //edit.setId();
        //information.setId();
        dropdownMenu.getItems().add(task.getAssignees().get(0));
        dropdownMenu.setValue(task.getAssignees().get(0));
    }
    public void showDescription() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Description");
        alert.setHeaderText(null);
        alert.setContentText("TEEESTT");

        alert.showAndWait();
    }

    public void setTaskToInactive(ActionEvent event) {
        // get button that triggered the action
        Node n = (Node) event.getSource();

        // get node to remove
        Node p = n.getParent();

        // remove p from parent's child list
        ((GridPane) p.getParent()).getChildren().remove(p);
    }
}
