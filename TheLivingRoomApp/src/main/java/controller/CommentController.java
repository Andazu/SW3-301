package controller;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.net.URL;
import java.util.ResourceBundle;

import static controller.DatabaseMethods.getDBColl;

public class CommentController implements DatabaseMethods, UIMethods, Initializable {
    @FXML
    private Button cancelButton;
    @FXML
    private Button addCommentButton;
    @FXML
    private TextArea addComment;
    @FXML
    private BorderPane addCommentBorderPane;
    private String id;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        makeButtonsCancelAndDefault(cancelButton, addCommentButton);
    }

    public void cancelAndReturnToOverviewPage(ActionEvent event) {
        closeStage(event);
    }

    public void addCommentAndReturnToOverviewPage(ActionEvent event) {
        if (addComment.getText() != "") {
            addCommentToDB(addCommentBorderPane, addComment.getText());
            closeStage(event);
        } else {
            informationDialog("The Comment Cannot Be Empty");
        }
    }
}
