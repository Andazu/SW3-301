package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import model.Task;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class TaskEmployeeController implements DatabaseMethods, UIMethods {
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
    private Circle overdueTask;
    @FXML
    private ImageView imageView;
    private ArrayList<String> assignees = new ArrayList<>();
    private final int oneDayMS = 86400000;

    public void setTaskBoxToUI(Task task) {
        expandAssigneeButton.setId("down");
        assignees = task.getAssignees();
        Date today = new Date();
        today.setTime(new Date().getTime() - oneDayMS);

        if (task.getDbDate().before(today)) {
            overdueTask.setFill(Color.RED);
        }

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

    public void showDescription(ActionEvent event) {
        Node parent = returnParentsParentNode(event);

        DescriptionController controller = new DescriptionController(parent.getId());
        makeModalDialog(controller, "description-page.fxml", 700, 450);
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

        CommentController commentController = new CommentController(parent.getId(), "Employee");
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
