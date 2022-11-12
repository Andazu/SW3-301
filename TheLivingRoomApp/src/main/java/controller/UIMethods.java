package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
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

    default void createNewStage(String path, int width, int height) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(path));

            Scene scene = new Scene(fxmlLoader.load(), width, height);

            Stage stage = new Stage();

            stage.setTitle("The Living Room");
            scene.getStylesheets().add("stylesheet.css");
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

    default void errorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    default void descriptionInformation(String title, String description) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(description);

        alert.showAndWait();
    }

    default Node returnParentsParentNode(ActionEvent event) {
        Node triggerActionNode = (Node) event.getSource();
        return triggerActionNode.getParent().getParent();
    }

    default void addCssToButtons(Button button, String cssClass) {
        button.getStyleClass().add(cssClass);
    }

    default TextInputDialog managerPinCodeLogin(BorderPane borderpane) {
        TextInputDialog td = new TextInputDialog();
        td.setTitle("Manager Login");
        td.setHeaderText("");
        td.setGraphic(null);

        // Gør så kun "Enter" knappen eller tryk på ok tjekker PIN-koden
        td.getDialogPane().lookupButton(ButtonType.OK).addEventFilter(
            ActionEvent.ACTION, event -> {
                if (td.getEditor().getText().equals("1234")) {
                    switchScene(borderpane, "overview-manager-page.fxml");
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Wrong PIN code");
                    alert.setGraphic(null);
                    alert.setHeaderText(null);
                    alert.show();
                }
            }
        );

        td.showAndWait();
        return td;
    }
}