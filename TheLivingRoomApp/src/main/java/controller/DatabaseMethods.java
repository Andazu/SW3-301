package controller;

import com.mongodb.client.*;
import com.mongodb.client.model.*;
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

     static boolean checkConnection(){
        try (MongoClient mongoClient = MongoClients.create(url)) {
            System.out.println("Connection successful.");
            return true;
        }
        catch (Exception e) {
            System.out.println("Something went wrong with MongoDB.");
            return false;
        }
    }

    static Document getDocumentById(String id, String collName) {
        MongoCollection<Document> coll = getDBColl(collName);
        ObjectId objectId = new ObjectId(id);
        Document doc = coll.find(eq("_id", objectId)).first();
        return doc;
    }

     static MongoCollection<Document> getDBColl(String collName) {
        if (checkConnection()) {
            MongoClient mongoClient = MongoClients.create(url);
            return mongoClient.getDatabase("project").getCollection(collName);
        }
        return null;
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
    default String exportTaskToDatabase(Task task, String collName){
        try {
            if (checkConnection()) {
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

                MongoCollection<Document> coll = getDBColl(collName);
                coll.insertOne(document);
                return String.valueOf(document.getObjectId("_id"));
            }
        }
        catch (Exception e) {
            System.out.println("Something went wrong with MongoDB during exportDocument call.");
        }
        return null;
    }

    default String exportUserToDatabase(User user, String collName){
        try {
            if (checkConnection()) {
                Document document = new Document();
                document.append("firstName", user.getFirstName());
                document.append("lastName", user.getLastName());
                document.append("emailAddress", user.getEmailAddress());
                document.append("phoneNumber", user.getPhoneNumber());
                document.append("admin", user.isAdmin());
                document.append("role", user.getRole());

                MongoCollection<Document> coll = getDBColl(collName);
                coll.insertOne(document);
                return String.valueOf(document.getObjectId("_id"));
            }
        }
        catch (Exception e) {
            System.out.println("Something went wrong with MongoDB during exportDocument call.");
        }
        return null;
    }

    default void addCommentToDB(String id, String comment, String collName) {
        ObjectId objectId = new ObjectId(id);
        MongoCollection<Document> collection = getDBColl(collName);
        collection.updateOne(Filters.eq("_id", objectId), Updates.addToSet("comments", comment));
    }

    default void updateTask(String id, String collName, boolean SetActive) {
        ObjectId objectId = new ObjectId(id);

        MongoCollection<Document> collection = getDBColl(collName);
        collection.updateOne(Filters.eq("_id", objectId), Updates.set("active", SetActive));
    }

    default double updateProgressBarInDBAndReturnValue(String id, String dropdownMenuPercent, String collName) throws ParseException {
        ObjectId objectId = new ObjectId(id);

        MongoCollection<Document> collection = getDBColl(collName);

        if(dropdownMenuPercent == "0%"){
            collection.updateOne(Filters.eq("_id", objectId), Updates.set("progress", 0.0));
            return 0.0;
        }
        else {
            Number value = new DecimalFormat("0.0#%").parse(dropdownMenuPercent);
            collection.updateOne(Filters.eq("_id", objectId), Updates.set("progress", (new DecimalFormat("0.0#%").parse(dropdownMenuPercent))));
            return value.doubleValue();
        }
     }

    static ArrayList<String> getCommentsFromDB(String id, String collName) {
        Document doc = getDocumentById(id, collName);

        ArrayList<Object> values = new ArrayList<>(doc.values());

        return (ArrayList<String>) values.get(8);
    }

     static Boolean emptyCollection(String collName){
         MongoCollection<Document> coll = getDBColl(collName);
         coll.deleteMany(new Document());

         if (coll.countDocuments() == 0) {
             return true;
         } else {
             return false;
         }
     }
}
