package Classes.Users;

public class Manager extends User {
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void deleteTask() {

    }

    public void editTask() {

    }

    public void createTask() {

    }

    public void createCustomAlert() {

    }

    public void createUser() {

    }

    public void deleteUser() {

    }

    public void editUser() {

    }

    public Manager(String name, int phoneNumber, String id) {
        super(name, phoneNumber, id);
    }
}
