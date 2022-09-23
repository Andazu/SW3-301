package Classes.Users;

public class Employee extends User {
    private String fieldOfWork;

    public String getFieldOfWork() {
        return fieldOfWork;
    }

    public void setFieldOfWork(String fieldOfWork) {
        this.fieldOfWork = fieldOfWork;
    }

    public void writeCommentsOnTask() {

    }

    public void changeStateOfTask() {

    }

    public Employee(String name, int phoneNumber, String id) {
        super(name, phoneNumber, id);
    }
}
