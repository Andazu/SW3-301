package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import model.Task;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

public class TaskController implements DatabaseMethods, UIMethods {
    @FXML
    private ProgressBar progressBar;
    @FXML
    private Label taskLabel;
    @FXML
    private ComboBox<String> dropdownMenuPercent;
    @FXML
    private GridPane gridPane;

    public void setTaskBoxToUI(Task task) {
        progressBar.setProgress(task.getProgress());
        taskLabel.setText(task.getTitle());
        dropdownMenuPercent.getItems().addAll("0%", "25%","50%","75%");
        setDropdownMenuPercentValue(task.getProgress() * 100);
        setAssigneesToGridPane(task.getAssignees());
    }

    public void setDropdownMenuPercentValue(double dropdownMenuPercentValue) {
        if (dropdownMenuPercentValue != 0.0) {
            int progress = (int) dropdownMenuPercentValue;
            String progressToString = Integer.toString(progress) + '%';
            dropdownMenuPercent.setValue(progressToString);
        }
    }

    public void setAssigneesToGridPane(ArrayList<String> assignees) {
        int columns = 1;
        int rows = 1;

        try {
            for (String assignee : assignees) {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("assignee-box-page.fxml"));

                HBox hBox = loader.load();

                AssigneeController assigneeController = loader.getController();
                assigneeController.setAssigneeName(assignee);

                gridPane.add(hBox, columns, rows);

                rows++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showDescription(ActionEvent event) {
        Node parent = returnParentNode(event);

        DescriptionController controller = new DescriptionController(parent.getId());
        makeModalDialog(controller, "description-page.fxml", 700, 450);
    }

    public void setTaskToInactive(ActionEvent event) {
        Node parent = returnParentNode(event);
        updateTask(parent.getId(), "tasks", false);

        // remove p from parent's child list
        ((GridPane) parent.getParent()).getChildren().remove(parent);
    }

    public void updateProgressBar(ActionEvent event) throws ParseException {
        Node parent = returnParentNode(event);
        double progress = updateProgressBarInDBAndReturnValue(parent.getId(), dropdownMenuPercent.getValue(), "tasks");
        progressBar.setProgress(progress);
    }

    public void addCommentToTask(ActionEvent event) {
        Node parent = returnParentNode(event);

        CommentController commentController = new CommentController(parent.getId());
        makeModalDialog(commentController,"add-comment-page.fxml", 731, 500);
    }
}
