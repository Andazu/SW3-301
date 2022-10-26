package controller;

import com.mongodb.client.*;
import com.mongodb.client.model.*;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
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

     static MongoCollection<Document> getDBColl(String collName) {
        checkConnection();
        MongoClient mongoClient = MongoClients.create(url);
        MongoCollection<Document> coll = mongoClient.getDatabase("project").getCollection(collName);
        return coll;
    }

     static ArrayList<Task> getTasksFromDB(boolean isActive, String collName) {
        ArrayList<Task> taskList = new ArrayList<>();

        MongoCollection<Document> coll = getDBColl(collName);

        for (Document doc : coll.find(eq("active", isActive))) {
            ArrayList<Object> values = new ArrayList<>(doc.values());

            taskList.add(createTaskToDisplay(values));
        }
        return taskList;
    }

     static ArrayList<User> getEmployeesFromDB(boolean isAdmin, String collName) {
        ArrayList<User> employees = new ArrayList<>();

        MongoCollection<Document> coll = getDBColl(collName);

        for (Document doc : coll.find(eq("admin", isAdmin)).sort(Sorts.ascending("role", "firstName"))) {
            ArrayList<Object> values = new ArrayList<>(doc.values());

            employees.add(new User(values.get(0).toString(), values.get(1).toString() + ' ' + values.get(2).toString(), values.get(6).toString()));
        }
        return employees;
    }
    default void exportTaskToDatabase(Task task, String collName){
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
            mongoClient.getDatabase("project").getCollection(collName).insertOne(document);
        }
        catch (Exception e) {
            System.out.println("Something went wrong with MongoDB during exportDocument call.");
        }
    }

    default void exportUserToDatabase(User user, String collName){
        try {
            checkConnection();

            Document document = new Document();
            document.append("firstName", user.getFirstName());
            document.append("lastName", user.getLastName());
            document.append("emailAddress", user.getEmailAddress());
            document.append("phoneNumber", user.getPhoneNumber());
            document.append("admin", user.isAdmin());

            MongoClient mongoClient = MongoClients.create(url);
            mongoClient.getDatabase("project").getCollection(collName).insertOne(document);
        }
        catch (Exception e) {
            System.out.println("Something went wrong with MongoDB during exportDocument call.");
        }
    }

    default void addCommentToDB(String stringId, String comment, String collName) {
        ObjectId id = new ObjectId(stringId);
        MongoCollection<Document> collection = getDBColl(collName);
        collection.updateOne(Filters.eq("_id", id), Updates.addToSet("comments", comment));

    }

    default void updateTask(String id, String collName) {
        ObjectId objectId = new ObjectId(id);

        MongoCollection<Document> collection = getDBColl(collName);
        collection.updateOne(Filters.eq("_id", objectId), Updates.set("active", false));
    }

    default void updateProgressBarInDB(String id, ComboBox dropdownMenuPercent, String collName) throws ParseException {
        ObjectId objectId = new ObjectId(id);

        MongoCollection<Document> collection = getDBColl(collName);

        if(dropdownMenuPercent.getValue() == "0%"){
            collection.updateOne(Filters.eq("_id", objectId), Updates.set("progress", 0.0));
        }
        else {
            String value = dropdownMenuPercent.getValue().toString();
            collection.updateOne(Filters.eq("_id", objectId), Updates.set("progress", (new DecimalFormat("0.0#%").parse(value))));
        }
     }

    static ArrayList<String> getCommentsFromDB(String idString, String collName) {
        Document doc = getDocumentById(idString, collName);

        ArrayList<Object> values = new ArrayList<>(doc.values());

        return (ArrayList<String>) values.get(8);
    }

     default void emptyCollection(String collName){
         MongoCollection<Document> coll = getDBColl(collName);
         coll.deleteMany(new Document());
     }
}
