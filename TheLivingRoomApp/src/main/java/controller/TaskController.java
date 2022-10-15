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

    @FXML
    private ImageView checkmark;

    @FXML
    private ImageView minus;

    @FXML
    private ImageView edit;

    @FXML
    private ImageView information;

    @FXML
    private ComboBox dropdownMenu;

    public void setTask(Task task) {
        progressBar.setProgress(0);
        taskLabel.setText(task.getTitle());
        //checkmark.setId();
        //minus.setId();
        //edit.setId();
        //information.setId();
        dropdownMenu.getItems().add(task.getAssignees());
        dropdownMenu.setValue(task.getAssignees());
    }
}
