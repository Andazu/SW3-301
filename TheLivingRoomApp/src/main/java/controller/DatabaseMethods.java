package controller;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.Updates;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import model.*;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.text.DecimalFormat;
import java.text.ParseException;
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

    static Document getDocumentById(String id, String collName) {
        MongoCollection<Document> coll = getDBColl(collName);
        ObjectId objectId = new ObjectId(id);
        Document doc = coll.find(eq("_id", objectId)).first();
        return doc;
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

     static ArrayList<User> getEmployeesFromDB(boolean isAdmin, String path) {
        ArrayList<User> employees = new ArrayList<>();

        MongoCollection<Document> coll = getDBColl(path);

        for (Document doc : coll.find(eq("admin", isAdmin)).sort(Sorts.ascending("role", "firstName"))) {
            ArrayList<Object> values = new ArrayList<>(doc.values());

            employees.add(new User(values.get(0).toString(), values.get(1).toString() + ' ' + values.get(2).toString(), values.get(6).toString()));
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
        return new ObjectId(p.getId());
    }

    default void addCommentToDB(String stringId, String comment) {
        ObjectId id = new ObjectId(stringId);
        MongoCollection<Document> collection = getDBColl("tasks");
        collection.updateOne(Filters.eq("_id", id), Updates.addToSet("comments", comment));

    }

    default void updateTask(String id) {
        ObjectId objectId = new ObjectId(id);

        MongoCollection<Document> collection = getDBColl("tasks");
        collection.updateOne(Filters.eq("_id", objectId), Updates.set("active", false));
    }

    default void updateProgressBarInDB(String id, ComboBox dropdownMenuPercent) throws ParseException {
        ObjectId objectId = new ObjectId(id);

        MongoCollection<Document> collection = getDBColl("tasks");

        if(dropdownMenuPercent.getValue() == "0%"){
            collection.updateOne(Filters.eq("_id", objectId), Updates.set("progress", 0.0));
        }
        else {
            collection.updateOne(Filters.eq("_id", objectId), Updates.set("progress", (new DecimalFormat("0.0#%").parse(dropdownMenuPercent.getValue().toString()))));
        }
     }

    static ArrayList<String> getCommentsFromDB(String idString) {
        Document doc = getDocumentById(idString, "tasks");

        ArrayList<Object> values = new ArrayList<>(doc.values());

        return (ArrayList<String>) values.get(8);
    }

     default void emptyCollection(String path){
         MongoCollection<Document> coll = getDBColl(path);
         coll.deleteMany(new Document());
     }
}
