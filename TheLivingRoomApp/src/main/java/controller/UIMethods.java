package controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.bson.types.ObjectId;

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

    default void makeModalDialog(String fxmlFile, int width, int height) {
        try {
            Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(fxmlFile));
            BorderPane dialogBox = loader.load();

            Scene dialogScene = new Scene(dialogBox, width, height);
            dialog.setScene(dialogScene);
            dialog.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    default void makeModalDialogWithId(String fxmlFile, int width, int height, ObjectId id) {
        try {
            Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(fxmlFile));
            BorderPane dialogBox = loader.load();

            dialogBox.setId(id.toString());

            Scene dialogScene = new Scene(dialogBox, width, height);
            dialog.setScene(dialogScene);
            dialog.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    default void makeButtonsCancelAndDefault(Button cancel,Button ok) {
        cancel.setCancelButton(true);
        ok.setDefaultButton(true);
    }
}
