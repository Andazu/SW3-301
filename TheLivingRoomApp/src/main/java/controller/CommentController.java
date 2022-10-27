package controller;

import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CommentController implements DatabaseMethods, UIMethods, Initializable {
    @FXML
    private Button cancelButton;
    @FXML
    private Button addCommentButton;
    @FXML
    private TextArea addComment;

    @FXML
    private GridPane addCommentGridPane;
    private String id;

    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM HH:mm");
    LocalDateTime now = LocalDateTime.now();

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
            addCommentToDB(this.id, dtf.format(now) + ":\n" + addComment.getText(), "tasks");
            closeStage(event);
        } else {
            informationDialog("The Comment Cannot Be Empty");
        }
    }

    public void displayComments() {
        ArrayList<String> comments = new ArrayList<>(DatabaseMethods.getCommentsFromDB(id, "tasks"));

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
