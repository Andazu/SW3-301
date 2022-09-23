package Classes.Users;

public abstract class User {
    protected String name;
    protected int phoneNumber;
    protected String id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getId() {
        return id;
    }

    public User(String name, int phoneNumber, String id) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.id = id;
    }
}
