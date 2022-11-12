package model;

public class User {
    private String id;
    private String firstName;
    private String lastName;
    private String fullName;
    private String emailAddress;
    private String phoneNumber;
    private String role;
    private boolean admin;

    public String getFirstName() {
        return firstName;
    }

    public boolean setFirstName(String firstName) {
        return firstName != null;
    }

    public String getLastName() {
        return lastName;
    }

    public boolean setLastName(String lastName) {
        return lastName != null;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public boolean setEmailAddress(String emailAddress) {
        if (emailAddress == null) {
            return false;
        } else if (emailAddress.contains("@") & emailAddress.contains(".")) {
            this.emailAddress = emailAddress;
            return true;
        }
        return false;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public boolean setPhoneNumber(String phoneNumber) {
        if (phoneNumber == null) {
            return false;
        } else if (phoneNumber.length() == 8) {
            this.phoneNumber = phoneNumber;
            return true;
        }
        return false;
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

    public boolean setRole(String role) {
        if (role == null) {
            return false;
        } else {
            this.role = role;
            return true;
        }
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

    public User(String firstName, String lastName, String emailAddress, String phoneNumber, boolean admin, String role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
        this.admin = admin;
        this.role = role;
    }

    public User(String id, String fullName, String role) {
        this.id = id;
        this.fullName = fullName;
        this.role = role;
    }

    public User() {
    }
}
