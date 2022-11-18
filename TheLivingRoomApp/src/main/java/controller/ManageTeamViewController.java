package controller;

import com.mongodb.client.model.Filters;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import model.Task;
import model.User;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.*;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.ne;

public class ManageTeamViewController implements Initializable, UIMethods, DatabaseMethods {
    @FXML
    private GridPane userGrid;
    @FXML
    private BorderPane ManageTeamViewBorderPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //ArrayList<User> users = DatabaseMethods.getEmployeesFromDB(false, "users");

        populateManageTeamViewWithUserBoxes();
    }

    public void populateManageTeamViewWithUserBoxes() {
        ArrayList<User> users = new ArrayList<>(DatabaseMethods.getEmployeesFromDB(false, "users"));

        int columns = 1;
        int rows = 1;

        try {
            for (User user : users) {

                FXMLLoader loader = new FXMLLoader();

                loader.setLocation(getClass().getResource("employee-box-page.fxml"));

                VBox vBox = loader.load();
                vBox.setId(user.getId().toString()); // Store user id as hBox id

                UserBoxController userBoxController = loader.getController();
                userBoxController.setUserBoxToUI(user);

                userGrid.add(vBox, columns, rows);
                rows++;

                Separator separator = new Separator(Orientation.HORIZONTAL);
                userGrid.add(separator, columns, rows);
                rows++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void refreshPage(ActionEvent event) {
        switchScene(ManageTeamViewBorderPane, "manage-team-page.fxml");
    }
}

