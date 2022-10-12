package Classes.Users;

public abstract class User {
    protected String name;
    protected int phoneNumber;
    protected String id;

    public User(String name, int phoneNumber, String id) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.id = id;
    }
}
