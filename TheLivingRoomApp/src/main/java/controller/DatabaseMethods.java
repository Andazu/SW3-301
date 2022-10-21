package controller;

import com.mongodb.MongoException;
import com.mongodb.client.*;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.Updates;
import com.mongodb.client.model.changestream.FullDocument;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import model.*;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.ne;
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

    static Document getDocumentById(String id, String collName) {
        MongoCollection<Document> coll = getDBColl(collName);
        ObjectId objectId = new ObjectId(id);
        Document doc = coll.find(eq("_id", objectId)).first();
        return doc;
    }

    static void updateSelectedAssigneeDoc(String id, String collName, boolean delete) {
        MongoCollection<Document> collection = getDBColl(collName);
        ObjectId objectId = new ObjectId(id);
        collection.updateOne(Filters.eq("_id", objectId), Updates.set("delete", delete));
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

     static ArrayList<User> getEmployeesFromDB(boolean isAdmin) {
        ArrayList<User> employees = new ArrayList<>();

        MongoCollection<Document> coll = getDBColl("users");

        for (Document doc : coll.find(eq("admin", isAdmin)).sort(Sorts.ascending("role", "firstName"))) {
            ArrayList<Object> values = new ArrayList<>(doc.values());

            employees.add(new User(values.get(0).toString(), values.get(1).toString(), values.get(2).toString(), values.get(6).toString()));
        }
        return employees;
    }

    static ArrayList<User> getSelectedAssigneesFromDB() {
         ArrayList<User> selectedAssignees = new ArrayList<>();

         MongoCollection<Document> coll = getDBColl("selectedEmployees");

        for (Document doc : coll.find()) {
            ArrayList<Object> values = new ArrayList<>(doc.values());

            selectedAssignees.add(new User(values.get(0).toString(), values.get(1).toString(), values.get(2).toString()));
        }
        return selectedAssignees;
    }

    static ArrayList<String> getSelectedAssigneesFromDBToUI() {
        ArrayList<String> selectedAssignees = new ArrayList<>();

        MongoCollection<Document> coll = getDBColl("selectedEmployees");

        for (Document doc : coll.find()) {
            ArrayList<Object> values = new ArrayList<>(doc.values());

            selectedAssignees.add(values.get(1).toString());
        }
        return selectedAssignees;
    }

    default void deleteSelectedEmployees() {
        checkConnection();
        MongoCollection<Document> coll = getDBColl("selectedEmployees");

        coll.deleteMany(ne("fullName", " "));
    }

    default void deleteSelectedEmployeesFromFormPage() {
        checkConnection();
        MongoCollection<Document> coll = getDBColl("selectedEmployees");

        coll.deleteMany(eq("delete", true));
    }

    default void exportSelectedEmployeesToDB(User user) {
         try {
             checkConnection();

             Document document = new Document();
             ObjectId objectId = new ObjectId(user.getIdProperty());
             document.append("_id", objectId);
             document.append("fullName", user.getFirstNameProperty() + ' ' + user.getLastNameProperty());
             document.append("role", user.getRoleNameProperty());
             document.append("delete", false);

             MongoClient mongoClient = MongoClients.create(url);
             mongoClient.getDatabase("project").getCollection("selectedEmployees").insertOne(document);
         } catch (Exception e) {
             e.printStackTrace();
         }
    }

    default void exportTaskToDatabase(Task task){
        try {
            checkConnection();

            Document document = new Document();
            document.append("title", task.getTitle());
            document.append("description", task.getDescription());
            document.append("frequency", task.getFrequency());
            document.append("urgency", task.getUrgency());
            document.append("type", task.getType());
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

    default ObjectId getTaskIdFromButton(ActionEvent event) {
        Node n = (Node) event.getSource();
        Node p = n.getParent();
        ObjectId id = new ObjectId(p.getId());
        return id;
    }

    default void addCommentToDB(BorderPane addCommentBorderPane, String comment) {
        ObjectId id = new ObjectId(addCommentBorderPane.getId());
        MongoCollection<Document> collection = getDBColl("tasks");
        collection.updateOne(Filters.eq("_id", id), Updates.addToSet("comments", comment));

    }

    default void updateTask(Node parent) {
        ObjectId id = new ObjectId(parent.getId());

        MongoCollection<Document> collection = getDBColl("tasks");
        collection.updateOne(Filters.eq("_id", id), Updates.set("active", false));
    }

    default void updateProgressBarInDB(Node parent, ComboBox dropdownMenuPercent) throws ParseException {
        ObjectId id = new ObjectId(parent.getId());

        MongoCollection<Document> collection = getDBColl("tasks");
        collection.updateOne(Filters.eq("_id", id), Updates.set("progress", (new DecimalFormat("0.0#%").parse(dropdownMenuPercent.getValue().toString()))));
    }
}
