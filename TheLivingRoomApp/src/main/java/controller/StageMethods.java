package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.stage.Stage;

import java.io.IOException;

public class StageMethods {
    private Parent root;
    private Stage stage;
    private Scene scene;

    public void switchStage(ActionEvent event, String path) {
        try {
            root = FXMLLoader.load(getClass().getResource(path));
            // https://stackoverflow.com/questions/32129179/a-code-i-dont-understand-about-switching-scenes-in-javafx-and-scenebuilder
            stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root, 1024, 700);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
