package Launcher;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("start-page.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 800, 640);
        //scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

        stage.setTitle("The Living Room App");
        stage.setScene(scene);
        stage.show();
    }
}