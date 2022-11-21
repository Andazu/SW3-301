package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Task;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Objects;

public class TaskManagerController implements DatabaseMethods, UIMethods {
    @FXML
    private ProgressBar progressBar;
    @FXML
    private Label taskLabel;
    @FXML
    private ComboBox<String> dropdownMenuPercent;
    @FXML
    private Button expandAssigneeButton;
    @FXML
    private HBox hBox;
    @FXML
    private VBox vBoxTheWholeBox;
    @FXML
    private ImageView imageView;
    private ArrayList<String> assignees = new ArrayList<>();

    public void setTaskBoxToUI(Task task) {
        expandAssigneeButton.setId("down");
        assignees = task.getAssignees();

        progressBar.setProgress(task.getProgress());
        taskLabel.setText(task.getTitle());
        dropdownMenuPercent.getItems().addAll("0%", "25%","50%","75%");
        setDropdownMenuPercentValue(task.getProgress() * 100);
    }

    public void setDropdownMenuPercentValue(double dropdownMenuPercentValue) {
        if (dropdownMenuPercentValue != 0.0) {
            int progress = (int) dropdownMenuPercentValue;
            String progressToString = Integer.toString(progress) + '%';
            dropdownMenuPercent.setValue(progressToString);
        }
    }

    public void editTask(ActionEvent event) {
        Node parent = returnParentsParentNode(event);

        EditTaskController controller = new EditTaskController(parent.getId());
        makeModalDialog(controller, "edit-task-page.fxml", 1024, 768);
    }

    public void setTaskToInactive(ActionEvent event) {
        Node parent = returnParentsParentNode(event);
        completeTask(parent.getId(), "tasks", false);

        // remove p from parent's child list
        ((GridPane) parent.getParent()).getChildren().remove(parent);
    }

    public void updateProgressBar(ActionEvent event) throws ParseException {
        Node parent = returnParentsParentNode(event);
        double progress = updateProgressBarInDBAndReturnValue(parent.getId(), dropdownMenuPercent.getValue(), "tasks");
        progressBar.setProgress(progress);
    }

    public void addCommentToTask(ActionEvent event) {
        Node parent = returnParentsParentNode(event);

        CommentController commentController = new CommentController(parent.getId(), "Manager");
        makeModalDialog(commentController,"add-comment-page.fxml", 731, 500);
    }

    public void expandToViewAssignees(ActionEvent event) {
        if (Objects.equals(expandAssigneeButton.getId(), "down")) {
            setNewImageInImageView("images/up-chevron.png", "up");

            hBox.getChildren().add(placeAssigneesWhenExpanded());
        } else {
            setNewImageInImageView("images/down-chevron.png", "down");

            resetTaskBox();
        }
    }

    public void resetTaskBox() {
        hBox.getChildren().remove(0);
        hBox.setPrefHeight(0);
        vBoxTheWholeBox.setPrefHeight(75);
    }

    public void setNewImageInImageView(String path, String id) {
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(path)));
        imageView.setImage(image);

        expandAssigneeButton.setId(id);
    }

    public VBox placeAssigneesWhenExpanded() {
        VBox expandVBox = createVboxForLabels();

        double prefHeight = expandVBox.getPrefHeight();
        for (String assignee : assignees) {
            Label name = new Label(assignee);

            expandVBox.getChildren().add(name);
            prefHeight += 20;
        }
        vBoxTheWholeBox.setPrefHeight(75 + prefHeight);

        expandVBox.setPrefHeight(prefHeight);
        return expandVBox;
    }

    public VBox createVboxForLabels() {
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER_LEFT);
        vBox.setSpacing(10);
        vBox.setPrefWidth(167);
        return vBox;
    }
}
