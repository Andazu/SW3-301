package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class CommentBoxController {
    @FXML
    private Label commentDescription;

    public void setCommentToUI(String s) {
        commentDescription.setText(s);
    }
}
