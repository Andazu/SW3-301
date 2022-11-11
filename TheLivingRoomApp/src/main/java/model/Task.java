package model;

import org.bson.types.ObjectId;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class Task {
    private ObjectId id;
    private String title;
    private String description;
    private String frequency;
    private String urgency;
    private String type;
    private double progress;
    private boolean active;
    private ArrayList<String> comments = new ArrayList<>(50);
    private ArrayList<String> assignees;
    private LocalDate date;
    private Date dbDate;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getUrgency() {
        return urgency;
    }

    public void setUrgency(String urgency) {
        this.urgency = urgency;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getProgress() {
        return progress;
    }

    public void setProgress(double progress) {
        this.progress = progress;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public ArrayList<String> getComments() {
        return comments;
    }

    public void setComments(ArrayList<String> comments) {
        this.comments = comments;
    }

    public ArrayList<String> getAssignees() {
        return assignees;
    }

    public void setAssignees(ArrayList<String> assignees) {
        this.assignees = assignees;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Date getDbDate() {
        return dbDate;
    }

    public void setDbDate(Date dbDate) {
        this.dbDate = dbDate;
    }

    public Task(ObjectId id, String title, String description, double progress, ArrayList<String> assignees) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.progress = progress;
        this.assignees = assignees;
    }
    public Task(ObjectId id, String title, String urgency, String description, double progress, ArrayList<String> assignees) {
        this.id = id;
        this.title = title;
        this.urgency = urgency;
        this.description = description;
        this.progress = progress;
        this.assignees = assignees;
    }

    public Task(String title, String description, String frequency, String urgency, String type, int progress, boolean active, ArrayList<String> comments, ArrayList<String> assignees) {
        this.title = title;
        this.description = description;
        this.frequency = frequency;
        this.urgency = urgency;
        this.type = type;
        this.progress = progress;
        this.active = active;
        this.comments = comments;
        this.assignees = assignees;
        this.date = date;
    }

    public Task(String title, String description, String frequency, String urgency, String type, int progress, boolean active, ArrayList<String> assignees, LocalDate date) {
        this.title = title;
        this.description = description;
        this.frequency = frequency;
        this.urgency = urgency;
        this.type = type;
        this.progress = progress;
        this.active = active;
        this.assignees = assignees;
        this.date = date;
    }
    public Task(String title, String description, double progress, boolean active, ArrayList<String> comments, ArrayList<String> assignees) {
        this.title = title;
        this.description = description;
        this.progress = progress;
        this.active = active;
        this.comments = comments;
        this.assignees = assignees;
    }

    public Task(ObjectId id, String title, String description, String frequency, String urgency, String type, double progress, boolean active, ArrayList<String> comments, ArrayList<String> assignees, Date dbDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.frequency = frequency;
        this.urgency = urgency;
        this.type = type;
        this.progress = progress;
        this.active = active;
        this.comments = comments;
        this.assignees = assignees;
        this.dbDate = dbDate;
    }
}
