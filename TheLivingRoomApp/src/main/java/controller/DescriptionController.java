package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import org.bson.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import controller.CommentController;

public class DescriptionController implements UIMethods {
    @FXML
    private Button okButton;
    @FXML
    private BorderPane borderPaneInfo;
    @FXML
    private Label titleLabel;
    @FXML
    private Label descriptionLabel;
    @FXML
    private Label dateLabel;
    @FXML
    private Label frequencyLabel;
    @FXML
    private Label urgencyLabel;
    @FXML
    private Label typeLabel;
    @FXML
    private Label assigneeLabel;
    @FXML
    private GridPane commentGridPane;
    private String id;

    @FXML
    public void initialize() {
        okButton.setDefaultButton(true);
        descriptionLabel.setWrapText(true);
        setValuesInFields();
        displayComments();
    }

    public void makeOkButtonLogic(ActionEvent event) {
        closeStage(event);
    }

    public void setAssigneeLabel(Document doc) {
        ArrayList<String> assignee = new ArrayList<>((Collection<String>) doc.get("assignees"));
        if (assignee.get(0) == "General") {
            assigneeLabel.setText("Everyone");
        } else {
            String allAssignees = "";
            for (String name: assignee) {
                allAssignees += name + ". ";
            }
            assigneeLabel.setText(allAssignees);
        }
    }

    public void setValuesInFields() {
        Document doc = DatabaseMethods.getDocumentById(id, "tasks");

        titleLabel.setText(doc.get("title").toString());
        descriptionLabel.setText(doc.get("description").toString());

        String date = doc.get("date").toString();
        dateLabel.setText(date.substring(0,10));

        frequencyLabel.setText(doc.get("frequency").toString());
        urgencyLabel.setText(doc.get("urgency").toString());
        typeLabel.setText(doc.get("type").toString());

        setAssigneeLabel(doc);
    }

    public void displayComments() {
        ArrayList<String> comments = new ArrayList<>(DatabaseMethods.getCommentsFromDB(id));

        int columns = 1;
        int rows = 1;

        try {
            for(int i = 0; i < comments.size(); i++) {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("comment-box-page.fxml"));

                HBox hBox = loader.load();
                hBox.setPrefWidth(170);

                CommentBoxController commentBoxController = loader.getController();
                commentBoxController.setCommentToUI(comments.get(i), 165);

                commentGridPane.add(hBox, columns, rows);

                rows++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public DescriptionController(String id) {
        this.id = id;
    }
}
