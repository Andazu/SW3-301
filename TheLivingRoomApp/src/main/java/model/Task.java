package model;

import com.mongodb.client.*;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Date;

public class Task {
    private String title;
    private String description;
    private String frequency;
    private String urgency;
    private String type;
    private int progress;
    private boolean active;
    private ArrayList<String> comments;
    private ArrayList<String> assignees;
    private Date date;

    public Task(String title, String description, ArrayList<String> assignees) {
        this.title = title;
        this.description = description;
        this.assignees = assignees;
    }

    public Task(String title, String description, String frequency, String urgency, String type, int progress, boolean active, ArrayList<String> comments, ArrayList<String> assignees, Date date) {
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

    public void exportDocument(){
        try {
            MongoDBLocal mongoDBLocal = new MongoDBLocal();
            mongoDBLocal.checkConnection();

            Document document = new Document();
            document.append("title", this.title);
            document.append("description", this.description);
            document.append("frequency", this.frequency);
            document.append("urgency", this.urgency);
            document.append("type", this.type);
            document.append("progress", this.progress);
            document.append("active", this.active);
            document.append("comments", this.comments);
            document.append("assignees", this.assignees);
            document.append("date", this.date);

            MongoClient mongoClient = MongoClients.create(mongoDBLocal.getUrl());
            mongoClient.getDatabase("project").getCollection("tasks").insertOne(document);
        }
        catch (Exception e) {
            System.out.println("Something went wrong with MongoDB during exportDocument call.");
        }
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

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
