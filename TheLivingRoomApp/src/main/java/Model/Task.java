package Model;

import java.util.ArrayList;

public class Task {
    private String title;
    private ArrayList<User> assignee;
    private String Description;

    public Task(String title, ArrayList<User> assignee, String description) {
        this.title = title;
        this.assignee = assignee;
        Description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<User> getAssignee() {
        return assignee;
    }

    public void setAssignee(ArrayList<User> assignee) {
        this.assignee = assignee;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
}
