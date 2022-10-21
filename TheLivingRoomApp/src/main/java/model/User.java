package model;

import javafx.beans.property.SimpleStringProperty;

public class User {
    private String id;
    private String firstName;
    private String lastName;
    private String fullName;
    private String emailAddress;
    private String phoneNumber;
    private String role;
    private boolean admin;
    private boolean delete;
    private SimpleStringProperty idProperty;
    private SimpleStringProperty firstNameProperty;
    private SimpleStringProperty lastNameProperty;
    private SimpleStringProperty roleNameProperty;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    public String getIdProperty() {
        return idProperty.get();
    }

    public void setIdProperty(String id) {
        this.idProperty = new SimpleStringProperty(id);
    }

    public String getFirstNameProperty() {
        return firstNameProperty.get();
    }

    public void setFirstNameProperty(String firstNameProperty) {
        this.firstNameProperty = new SimpleStringProperty(firstNameProperty);
    }

    public String getLastNameProperty() {
        return lastNameProperty.get();
    }

    public void setLastNameProperty(String lastNameProperty) {
        this.lastNameProperty = new SimpleStringProperty(lastNameProperty);
    }

    public String getRoleNameProperty() {
        return roleNameProperty.get();
    }

    public void setRoleNameProperty(String roleNameProperty) {
        this.roleNameProperty = new SimpleStringProperty(roleNameProperty);
    }

    public User(String firstName, String lastName, String emailAddress, String phoneNumber, boolean admin) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
        this.admin = admin;
    }

    public User(String idProperty, String firstNameProperty, String lastNameProperty, String roleNameProperty) {
        this.idProperty = new SimpleStringProperty(idProperty);
        this.firstNameProperty = new SimpleStringProperty(firstNameProperty);
        this.lastNameProperty = new SimpleStringProperty(lastNameProperty);
        this.roleNameProperty = new SimpleStringProperty(roleNameProperty);
    }

    public User(String id, String fullName, String role) {
        this.id = id;
        this.fullName = fullName;
        this.role = role;
    }
}
