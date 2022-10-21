package controller;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cancelButton.setCancelButton(true);
        addCommentButton.setDefaultButton(true);
    }

    public void cancelAndReturnToOverviewPage(ActionEvent event) {
        switchScene(addCommentBorderPane, "overview-employee-page.fxml");
    }

    public void addCommentAndReturnToOverviewPage(ActionEvent event) {
        String comment = addComment.getText();

        ObjectId id = new ObjectId(addCommentBorderPane.getId());

        addCommentToDB(id, comment);

        //stage.close(event);
    }
}
