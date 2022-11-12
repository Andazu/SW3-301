package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.junit.After;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationTest;
import javafx.scene.Parent;
import javafx.scene.Scene;
import java.time.LocalDate;

public class TaskFormControllerTest extends ApplicationTest {
    @FXML
    private ComboBox<String> frequencyDropdownMenu;

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("task-form-page.fxml"));
        stage.setTitle("TEST");
        stage.setScene(new Scene(root));
        stage.show();
    }

    @After
    public void tearDown() throws Exception {
        FxToolkit.hideStage();
        release(new KeyCode[]{});
        release(new MouseButton[]{});
    }

    @Test
    public void testClickData() {
        clickOn("#titleTextField").write("TEST");
        DatePicker datePicker = lookup("#datePicker").query();
        interact(() -> datePicker.setValue(LocalDate.now()));
        ComboBox<String> dropdown = lookup("#frequencyDropdownMenu").queryComboBox();
        //interact(() -> dropdown.getSelectionModel().select(0));
        clickOn("#submitButton");

    }
}
