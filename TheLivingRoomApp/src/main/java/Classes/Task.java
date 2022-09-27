package Classes;

import java.security.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

public class Task {
    private String title;
    private Timer frequency;
    private Date date;
    private LocalDateTime time;
    private String status;
    private String assignee;
    private String description;
    private int levelOfUrgency;
    private Date deadline;
    private String label;
    private List<Map<Integer, String>> allComments;
    private String comment;
    private boolean deleteIfResolved;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Timer getFrequency() {
        return frequency;
    }

    public void setFrequency(Timer frequency) {
        this.frequency = frequency;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public int getLevelOfUrgency() {
        return levelOfUrgency;
    }

    public void setLevelOfUrgency(int levelOfUrgency) {
        this.levelOfUrgency = levelOfUrgency;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setComment(String comment) {
    }

    public boolean isDeleteIfResolved() {
        return deleteIfResolved;
    }

    public void setDeleteIfResolved(boolean deleteIfResolved) {
        this.deleteIfResolved = deleteIfResolved;
    }

    public void sendAlert() {

    }

    public void deleteTaskIfResolved() {

    }

    public Task(String title, Timer frequency, String status, String assignee, String description, int levelOfUrgency, Date deadline, String label, List<Map<Integer, String>> allComments, String comment, boolean deleteIfResolved) {
        this.title = title;
        this.frequency = frequency;
        this.status = status;
        this.assignee = assignee;
        this.description = description;
        this.levelOfUrgency = levelOfUrgency;
        this.deadline = deadline;
        this.label = label;
        this.allComments = allComments;
        this.comment = comment;
        this.deleteIfResolved = deleteIfResolved;
    }

    public Task(String title, Timer frequency) {
        this.title = title;
        this.frequency = frequency;
    }
}
