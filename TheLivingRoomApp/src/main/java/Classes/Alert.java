package Classes;

public class Alert {
    private String title;
    private String assignee;
    private String description;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void popUp() {

    }

    public Alert(String title, String assignee, String description) {
        this.title = title;
        this.assignee = assignee;
        this.description = description;
    }

    public Alert(String title) {
        this.title = title;
    }
}
