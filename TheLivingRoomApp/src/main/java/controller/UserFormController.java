package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import model.User;
import org.controlsfx.control.action.Action;

import java.net.URL;
import java.util.ResourceBundle;

public class UserFormController implements Initializable, UIMethods, DatabaseMethods {
    @FXML
    private Button cancelButton;
    @FXML
    private Button submitButton;
    @FXML
    private TextField firstNameTextField;
    @FXML
    private TextField lastNameTextField;
    @FXML
    private ComboBox<String> roleComboBox;
    @FXML
    private TextField phoneNumberTextField;
    @FXML
    private TextField emailTextField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addCssToButtons(cancelButton, "cancel-button");
        addCssToButtons(submitButton, "submit-button");

        makeButtonsCancelAndDefault(cancelButton, submitButton);

        roleComboBox.getItems().addAll(
                "All", "Bartender", "Cleaner"
        );
    }

    public void cancelAction(ActionEvent event) {
        closeStage(event);
    }

    public void submitAction(ActionEvent event) {
        createUserFromUI();
        closeStage(event);
    }

    private void createUserFromUI() {
        User user = new User();

        boolean validFirstName = user.setFirstName(firstNameTextField.getText());
        boolean validLastName = user.setLastName(lastNameTextField.getText());
        boolean validEmail = user.setEmailAddress(emailTextField.getText());
        boolean validPhoneNumber = user.setPhoneNumber(phoneNumberTextField.getText());
        boolean validRole;
        if (roleComboBox.getValue() == null) {
            validRole = false;
        } else {
            user.setRole(roleComboBox.getValue());
            validRole = true;
        }
        user.setAdmin(false);

        if (validFirstName & validLastName & validEmail & validPhoneNumber & validRole) {
            exportUserToDatabase(user, "users");
        } else {
            errorDialog("The fields can't be empty", "");
        }
    }
}
