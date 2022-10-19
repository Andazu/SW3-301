package controller;

import com.mongodb.client.*;
import model.*;
import org.bson.Document;

import java.util.ArrayList;

import static com.mongodb.client.model.Filters.eq;
import static controller.OverviewController.createTaskToDisplay;

public interface DatabaseMethods {
     String url = "mongodb+srv://admin:admin@cluster0.ztdigfr.mongodb.net/?retryWrites=true&w=majority";

     static void checkConnection(){
        try (MongoClient mongoClient = MongoClients.create(url)) {
            System.out.println("Connection successful.");
        }
        catch (Exception e) {
            System.out.println("Something went wrong with MongoDB.");
        }
    }

     static MongoCollection<Document> getDBColl(String collPath) {
        checkConnection();
        MongoClient mongoClient = MongoClients.create(url);
        MongoCollection<Document> coll = mongoClient.getDatabase("project").getCollection(collPath);
        return coll;
    }

     static ArrayList<Task> getTasksFromDB(boolean isActive) {
        ArrayList<Task> taskList = new ArrayList<>();

        MongoCollection<Document> coll = getDBColl("tasks");

        for (Document doc : coll.find(eq("active", isActive))) {
            ArrayList<Object> values = new ArrayList<>(doc.values());

            taskList.add(createTaskToDisplay(values));
        }
        return taskList;
    }

     static ArrayList<String> getEmployeesFromDB(boolean isAdmin) {
        ArrayList<String> employees = new ArrayList<>();

        MongoCollection<Document> coll = getDBColl("users");

        for (Document doc : coll.find(eq("admin", isAdmin))) {
            ArrayList<Object> values = new ArrayList<>(doc.values());
            String fullName = values.get(1).toString() + ' ' + values.get(2).toString();

            employees.add(fullName);
        }
        return employees;
    }

    default void exportTaskToDatabase(Task task){
        try {
            checkConnection();

            Document document = new Document();
            document.append("title", task.getTitle());
            document.append("description", task.getDescription());
            document.append("frequency", task.getFrequency());
            document.append("urgency", task.getUrgency());
            document.append("type", task.getUrgency());
            document.append("progress", task.getProgress());
            document.append("active", task.isActive());
            document.append("comments", task.getComments());
            document.append("assignees", task.getAssignees());
            document.append("date", task.getDate());

            MongoClient mongoClient = MongoClients.create(url);
            mongoClient.getDatabase("project").getCollection("tasks").insertOne(document);
        }
        catch (Exception e) {
            System.out.println("Something went wrong with MongoDB during exportDocument call.");
        }
    }

    default void exportUserToDatabase(User user){
        try {
            checkConnection();

            Document document = new Document();
            document.append("firstName", user.getFirstName());
            document.append("lastName", user.getLastName());
            document.append("emailAddress", user.getEmailAddress());
            document.append("phoneNumber", user.getPhoneNumber());
            document.append("admin", user.isAdmin());

            MongoClient mongoClient = MongoClients.create(url);
            mongoClient.getDatabase("project").getCollection("users").insertOne(document);
        }
        catch (Exception e) {
            System.out.println("Something went wrong with MongoDB during exportDocument call.");
        }
    }
}
