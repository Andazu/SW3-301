package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public interface UIMethods {
    default void switchScene(BorderPane pane, String path) {
        try {
            BorderPane borderPane = FXMLLoader.load(getClass().getResource(path));
            pane.getChildren().setAll(borderPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
