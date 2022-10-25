package controller;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
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
    private ListView commentHistory;
    @FXML
    private BorderPane addCommentBorderPane;
    @FXML
    private GridPane addCommentGridPane;
    private String id;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        makeButtonsCancelAndDefault(cancelButton, addCommentButton);

        displayComments();
    }

    public void cancelAndReturnToOverviewPage(ActionEvent event) {
        closeStage(event);
    }

    public void addCommentAndReturnToOverviewPage(ActionEvent event) {
        if (addComment.getText() != "") {
            addCommentToDB(this.id, addComment.getText());
            closeStage(event);
        } else {
            informationDialog("The Comment Cannot Be Empty");
        }
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
                hBox.setPrefWidth(300);

                CommentBoxController commentBoxController = loader.getController();
                commentBoxController.setCommentToUI(comments.get(i), 295);

                addCommentGridPane.add(hBox, columns, rows);

                rows++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public CommentController(String id) {
        this.id = id;
    }
}
