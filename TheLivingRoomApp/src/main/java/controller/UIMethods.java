package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.bson.Document;
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

    default void createNewStage(String path, int width, int height) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(path));

            Scene scene = new Scene(fxmlLoader.load(), width, height);

            Stage stage = new Stage();

            stage.setTitle("The Living Room");
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/launcher/Images/notification1.png")));
            stage.setScene(scene);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    default void makeModalDialog(Object controller, String fxmlFile, int width, int height) {
        Stage stage = makeModalStage();
        FXMLLoader loader = makeModalLoader(fxmlFile);
        loader.setController(controller);
        showDialog(loader, stage, width, height);
    }

    default Stage makeModalStage() {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        return dialog;
    }

    default FXMLLoader makeModalLoader(String fxmlFile) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(fxmlFile));
        return loader;
    }

    default void showDialog(FXMLLoader loader, Stage dialog, int width, int height) {
        try {
            BorderPane dialogBox = loader.load();
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

    default void closeStage(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    default void informationDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(message);
        alert.show();
    }

    default void errorDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.show();
    }

    default void descriptionInformation(Document doc) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(doc.get("title").toString() + " Description");
        alert.setHeaderText(null);
        alert.setContentText(doc.get("description").toString());

        alert.showAndWait();
    }

    default Node returnParentNode(ActionEvent event) {
        Node triggerActionNode = (Node) event.getSource();
        Node parent = triggerActionNode.getParent();
        return parent;
    }
}
