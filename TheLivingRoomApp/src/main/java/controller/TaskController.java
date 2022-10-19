package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
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
}
