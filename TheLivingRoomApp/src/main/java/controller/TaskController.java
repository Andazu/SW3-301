package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import model.Task;
import org.bson.Document;
import org.bson.types.ObjectId;

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
        setAssigneesToGridPane(task.getAssignees());
    }

    public void setAssigneesToGridPane(ArrayList<String> assignees) {
        int columns = 1;
        int rows = 1;

        try {
            for (int i = 0; i < assignees.size(); i++) {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("assignee-box-page.fxml"));

                HBox hBox = loader.load();

                AssigneeController assigneeController = loader.getController();
                assigneeController.setAssigneeName(assignees.get(i));

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
        makeModalDialog(controller, "description-page.fxml", 600, 450);
    }

    public void setTaskToInactive(ActionEvent event) {
        Node parent = returnParentNode(event);
        updateTask(parent.getId());

        // remove p from parent's child list
        ((GridPane) parent.getParent()).getChildren().remove(parent);
    }

    public void updateProgressBar(ActionEvent event) throws ParseException {
        Node parent = returnParentNode(event);
        updateProgressBarInDB(parent.getId(), dropdownMenuPercent);
    }

    public void addCommentToTask(ActionEvent event) {
        Node parent = returnParentNode(event);

        CommentController commentController = new CommentController(parent.getId());
        makeModalDialog(commentController,"add-comment-page.fxml", 731, 500);
    }
}
