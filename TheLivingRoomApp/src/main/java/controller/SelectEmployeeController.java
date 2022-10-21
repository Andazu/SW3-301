package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import model.User;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SelectEmployeeController implements DatabaseMethods, UIMethods, Initializable {
    @FXML
    private Button cancelButton;
    @FXML
    private Button okButton;
    @FXML
    private TableView<User> employeeTableView;
    @FXML
    private TableColumn<User, String> id;
    @FXML
    private TableColumn<User, String> firstName;
    @FXML
    private TableColumn<User, String> lastName;
    @FXML
    private TableColumn<User, String> role;
    @FXML
    private BorderPane selectEmployeeBorderPane;
    private ArrayList<User> users;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        makeButtonsCancelAndDefault(cancelButton, okButton);
        employeeTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        setEmployeeTableView();

        employeeTableView.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                ObservableList<User> selectedEmployees = employeeTableView.getSelectionModel().getSelectedItems();

                ArrayList<User> exportSelectedEmployees = new ArrayList<>();
                for (User user : selectedEmployees) {
                    exportSelectedEmployees.add(new User(user.getIdProperty(),user.getFirstNameProperty(), user.getLastNameProperty(), user.getRoleNameProperty()));
                }

                users = exportSelectedEmployees;
            }
        });
    }

    public void setEmployeeTableView() {
        ArrayList<User> employeesFromDB = DatabaseMethods.getEmployeesFromDB(false);
        ObservableList<User> employeeList = FXCollections.observableArrayList(employeesFromDB);

        id.setCellValueFactory(new PropertyValueFactory<>("idProperty"));
        firstName.setCellValueFactory(new PropertyValueFactory<>("firstNameProperty"));
        lastName.setCellValueFactory(new PropertyValueFactory<>("lastNameProperty"));
        role.setCellValueFactory(new PropertyValueFactory<>("roleNameProperty"));

        employeeTableView.setItems(employeeList);
    }

    public void onActionOkButton(ActionEvent event) {
        if (users != null) {
            for (int i = 0; i < users.size(); i++) {
                exportSelectedEmployeesToDB(users.get(i));
            }

            switchScene(selectEmployeeBorderPane, "task-form-page.fxml");
        } else {
            errorDialog("Selected Assignees Cannot Be Zero");
        }
    }

    public void onActionCancelButton(ActionEvent event) {
        switchScene(selectEmployeeBorderPane, "task-form-page.fxml");
    }
}
