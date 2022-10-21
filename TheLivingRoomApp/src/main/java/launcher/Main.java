package launcher;

import controller.UIMethods;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application implements UIMethods {

    @Override
    public void start(Stage stage) {
        createNewStage("login-page.fxml", 1024, 700);
    }
}