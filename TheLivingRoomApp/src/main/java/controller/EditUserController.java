package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import model.User;
import org.bson.Document;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

public class EditUserController implements Initializable, UIMethods, DatabaseMethods {
    @FXML
    private BorderPane editEmployeeBorderPane;
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
    private final String id;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addCssToButtons(cancelButton, "cancel-button");
        addCssToButtons(submitButton, "submit-button");

        makeButtonsCancelAndDefault(cancelButton, submitButton);

        roleComboBox.getItems().addAll(
                "All", "Bartender", "Cleaner"
        );

        setValuesInFields();
    }

    private void setValuesInFields() {
        Document doc = DatabaseMethods.getDocumentById(id, "users");

        firstNameTextField.setText(doc.get("firstName").toString());
        lastNameTextField.setText(doc.get("lastName").toString());
        roleComboBox.setValue(doc.get("role").toString());
        phoneNumberTextField.setText(doc.get("phoneNumber").toString());
        emailTextField.setText(doc.get("emailAddress").toString());
    }

    public void submitAndUpdateUser(ActionEvent event) {
        User createdUser = new User();

        Boolean validFirstName = createdUser.setFirstName(firstNameTextField.getText());
        Boolean validLastName = createdUser.setLastName(lastNameTextField.getText());
        Boolean validRole = createdUser.setRole(roleComboBox.getValue());
        Boolean validEmailAddress = createdUser.setEmailAddress(emailTextField.getText());
        Boolean validPhoneNumber = createdUser.setPhoneNumber(phoneNumberTextField.getText());

        if (validFirstName & validLastName & validRole & validEmailAddress & validPhoneNumber) {
            updateUser(id, "users", createdUser);
            closeStage(event);
        } else {
            errorDialog("Empty Fields", "The following fields cannot be empty: First name, last name, role, phone number and e-mail");
        }
    }

    public void cancelAction(ActionEvent event) {
        closeStage(event);
    }
    public EditUserController(String id) {
        this.id = id;
    }


}
