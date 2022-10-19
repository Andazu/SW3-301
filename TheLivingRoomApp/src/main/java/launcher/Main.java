package launcher;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login-page.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 1024, 700);
        //scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

        stage.setTitle("The Living Room");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/launcher/Images/notification1.png")));
        stage.setScene(scene);

        stage.show();
    }
}