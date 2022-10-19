package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

// make into interface, make default method with parameters
public class StageMethods {
    private Parent root;
    private Stage stage;
    private Scene scene;

    public void switchStage(BorderPane pane, String path) {
        try {
            BorderPane borderPane = FXMLLoader.load(getClass().getResource(path));
            pane.getChildren().setAll(borderPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
