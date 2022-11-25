package controller;

import com.mongodb.client.*;
import com.mongodb.client.model.*;
import model.*;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.time.format.DateTimeFormatter;
import java.util.Set;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Sorts.ascending;
import static controller.OverviewEmployeeController.createTaskToDisplay;

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
        assert coll != null;
        return coll.find(eq("_id", objectId)).first();
    }

     static MongoCollection<Document> getDBColl(String collName) {
        if (checkConnection()) {
            try {
                MongoClient mongoClient = MongoClients.create(url);
                return mongoClient.getDatabase("project").getCollection(collName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

     static ArrayList<Task> getTasksFromDB(Bson filter, boolean isActive, String collName) {
         ArrayList<Task> taskList = new ArrayList<>();

         MongoCollection<Document> coll = getDBColl(collName);

         assert coll != null;
         for (Document doc : coll.find(Filters.and(filter, eq("active", isActive))).sort(ascending("date"))) {
             ArrayList<Object> values = new ArrayList<>(doc.values());

             taskList.add(createTaskToDisplay(values));
         }
         return taskList;
    }

     static ArrayList<User> getEmployeesFromDB(boolean isAdmin, String collName) {
        ArrayList<User> employees = new ArrayList<>();

        MongoCollection<Document> coll = getDBColl(collName);

         assert coll != null;
         for (Document doc : coll.find(eq("admin", isAdmin)).sort(ascending("role", "firstName"))) {
            ArrayList<Object> values = new ArrayList<>(doc.values());

            ObjectId id = new ObjectId(values.get(0).toString());
            employees.add(new User(id, values.get(1).toString(), values.get(2).toString(), values.get(3).toString(), values.get(4).toString(), (Boolean) values.get(5), values.get(6).toString()));
        }
        return employees;
    }
    default String exportTaskToDatabase(Task task, String collName){
        try {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM HH:mm");
            LocalDateTime now = LocalDateTime.now();

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
                document.append("lastEdit", dateTimeFormatter.format(now));

                MongoCollection<Document> coll = getDBColl(collName);
                assert coll != null;
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
                assert coll != null;
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
        assert collection != null;
        collection.updateOne(Filters.eq("_id", objectId), Updates.addToSet("comments", comment));
    }

    default void lastEditUpdate(String id, String comment, String collName){
        ObjectId objectId = new ObjectId(id);
        MongoCollection<Document> collection = getDBColl(collName);
        assert collection != null;
        collection.updateOne(Filters.eq("_id", objectId), Updates.addToSet("comments", comment));
    }

    default void completeTask(String id, String collName, boolean setActive) {
        ObjectId objectId = new ObjectId(id);
        MongoCollection<Document> collection = getDBColl(collName);
        assert collection != null;

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM HH:mm");
        LocalDateTime now = LocalDateTime.now();

        Task task = getTaskFromDB(objectId, collection);

        if (setActive) {
            collection.updateOne(Filters.eq("_id", objectId), Updates.set("active", true));
        } else {
            changeDateAndUpdateInDB(task.getFrequency(), objectId, collection, task.getDbDate(), false);
        }

        collection.updateOne(Filters.eq("_id", objectId), Updates.set("lastEdit", dateTimeFormatter.format(now)));
    }

    default Task getTaskFromDB(ObjectId objectId, MongoCollection<Document> collection) {
        Document doc = collection.find(Filters.eq("_id", objectId)).first();
        ArrayList<Object> values = new ArrayList<>(doc.values());

        return createTaskToDisplay(values);
    }

    default void changeDateAndUpdateInDB(String frequency, ObjectId objectId, MongoCollection<Document> collection, Date date, Boolean setActive) {
        final long oneDayMS = 86_400_000;
        int month;

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        int today = cal.get(Calendar.DATE);
        cal.add(cal.MONTH, 1);
        int nextMonthDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        if (nextMonthDays < daysInMonth && today > nextMonthDays) {
            month = daysInMonth - (today - nextMonthDays);
        } else if (daysInMonth < nextMonthDays){
            month = nextMonthDays;
        } else {
            month = daysInMonth;
        }

        switch (frequency.toLowerCase()) {
            case "every day" -> {
                date.setTime(date.getTime() + oneDayMS);
                collection.updateOne(Filters.eq("_id", objectId), Updates.set("date", date));
            }
            case "every other day" -> {
                date.setTime(date.getTime() + oneDayMS * 2);
                collection.updateOne(Filters.eq("_id", objectId), Updates.set("date", date));
            }
            case "every week" -> {
                date.setTime(date.getTime() + oneDayMS * 7);
                collection.updateOne(Filters.eq("_id", objectId), Updates.set("date", date));
            }
            case "every month" -> {
                date.setTime(date.getTime() + oneDayMS * month);
                collection.updateOne(Filters.eq("_id", objectId), Updates.set("date", date));
            }
            default -> collection.updateOne(Filters.eq("_id", objectId), Updates.set("active", setActive));
        }
    }

    default void updateTask(String id, String collName, Task task) {
        ObjectId objectId = new ObjectId(id);

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM HH:mm");
        LocalDateTime now = LocalDateTime.now();

        MongoCollection<Document> collection = getDBColl(collName);
        assert collection != null;
        collection.updateOne(Filters.eq("_id", objectId),
                Updates.combine(
                        Updates.set("title", task.getTitle()),
                        Updates.set("description", task.getDescription()),
                        Updates.set("frequency", task.getFrequency()),
                        Updates.set("urgency", task.getUrgency()),
                        Updates.set("type", task.getType()),
                        Updates.set("assignees", task.getAssignees()),
                        Updates.set("date", task.getDate()),
                        Updates.set("lastEdit", dateTimeFormatter.format(now))
                )
        );
    }

    default void updateUser(String id, String collName, User user) {
        ObjectId objectId = new ObjectId(id);

        MongoCollection<Document> collection = getDBColl(collName);
        assert collection != null;
        collection.updateOne(Filters.eq("_id", objectId),
                Updates.combine(
                        Updates.set("firstName", user.getFirstName()),
                        Updates.set("lastName", user.getLastName()),
                        Updates.set("role", user.getRole()),
                        Updates.set("emailAddress", user.getEmailAddress()),
                        Updates.set("phoneNumber", user.getPhoneNumber())
                )
        );
    }

    default double updateProgressBarInDBAndReturnValue(String id, String dropdownMenuPercent, String collName) throws ParseException {
        ObjectId objectId = new ObjectId(id);

        MongoCollection<Document> collection = getDBColl(collName);

        if (dropdownMenuPercent.equals("0%")) {
            assert collection != null;
            collection.updateOne(Filters.eq("_id", objectId), Updates.set("progress", 0.0));
            return 0.0;
        }
        else {
            Number value = new DecimalFormat("0.0#%").parse(dropdownMenuPercent);
            assert collection != null;
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
         assert coll != null;
         coll.deleteMany(new Document());

         return coll.countDocuments() == 0;
     }

    default void deleteFromDB(String id, String collName){
        MongoCollection<Document> coll = getDBColl(collName);
        ObjectId objectId = new ObjectId(id);
        coll.deleteOne(Filters.eq("_id", objectId));
    }
}
