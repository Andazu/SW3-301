package controller;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import model.Task;
import model.User;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static com.mongodb.client.model.Filters.eq;
import static controller.DatabaseMethods.*;
import static org.junit.jupiter.api.Assertions.*;

public class DBTest implements DatabaseMethods {
    String collNameTask = "testTask";
    String collNameUser = "testUser";
    String taskId = "635908fbe690a6203b0f982d";
    ArrayList<String> comments = new ArrayList<>();
    ArrayList<String> assignees = new ArrayList<>();
    Task testTask = new Task("Exportdatabase Tester", "Task til testing af export", 25.0, true, comments, assignees);;
    User testUser = new User("Iben", "Torbensen", "Iben.Torbensen@gmail.com", "88888888", true);

    // Laver 2 forskellige Tasks og Users som bliver brugt af alle tests, derfor sørger hver test også
    // for at slette de ændringer der er lavet.
   @BeforeAll
    public static void beforeAll() {
        String url = "mongodb+srv://admin:admin@cluster0.ztdigfr.mongodb.net/?retryWrites=true&w=majority";

        String collNameTask = "testTask";
        String collNameUser = "testUser";

        ArrayList<String> comments = new ArrayList<>();
        ArrayList<String> assignees = new ArrayList<>();
        Task testTask = new Task("TesterTrue", "Task til testing", 25.0, true, comments, assignees);;
        User testUser = new User("Preben", "Elkjær", "Preben.Elkjær@gmail.com", "88888888", true);

       try {
           checkConnection();

           // Laver Task
           Document task = new Document();
           task.append("_id", new ObjectId("635908fbe690a6203b0f982d"));
           task.append("title", testTask.getTitle());
           task.append("description", testTask.getDescription());
           task.append("frequency", testTask.getFrequency());
           task.append("urgency", testTask.getUrgency());
           task.append("type", testTask.getType());
           task.append("progress", testTask.getProgress());
           task.append("active", testTask.isActive());
           task.append("comments", testTask.getComments());
           task.append("assignees", testTask.getAssignees());
           task.append("date", testTask.getDate());

           MongoClient mongoClient = MongoClients.create(url);
           mongoClient.getDatabase("project").getCollection(collNameTask).insertOne(task);

           task.put("active", false);
           task.put("title", "TesterFalse");
           task.put("_id", new ObjectId("635a4e0782a860e977158223"));
           mongoClient.getDatabase("project").getCollection(collNameTask).insertOne(task);

           //Laver User
           Document user = new Document();
           user.append("_id", new ObjectId("635a599682a860e977158224"));
           user.append("firstName", testUser.getFirstName());
           user.append("lastName", testUser.getLastName());
           user.append("emailAddress", testUser.getEmailAddress());
           user.append("phoneNumber", testUser.getPhoneNumber());
           user.append("admin", true);
           user.append("role", "cleaner");

           mongoClient.getDatabase("project").getCollection(collNameUser).insertOne(user);

           user.put("_id", new ObjectId("635a59b082a860e977158225"));
           user.put("firstName", "Ernst");
           user.put("admin", false);

           mongoClient.getDatabase("project").getCollection(collNameUser).insertOne(user);
        }
        catch (Exception e) {
            System.out.println("Something went wrong with MongoDB during exportDocument call.");
        }
    }

    @AfterAll
    public static void afterAll() {
        String collNameTask = "testTask";
        String collNameUser = "testUser";

        ObjectId taskId = new ObjectId("635908fbe690a6203b0f982d");
        ObjectId taskId2 = new ObjectId("635a4e0782a860e977158223");
        ObjectId taskId3 = new ObjectId("635a599682a860e977158224");
        ObjectId taskId4 = new ObjectId("635a59b082a860e977158225");

        MongoCollection<Document> collTask = getDBColl(collNameTask);
        MongoCollection<Document> collUser = getDBColl(collNameUser);
        collTask.deleteOne(eq("_id", taskId));
        collTask.deleteOne(eq("_id", taskId2));
        collUser.deleteOne(eq("_id", taskId3));
        collUser.deleteOne(eq("_id", taskId4));
    }
    @Test
    public void testGetDocumentById() {
        assertEquals("TesterTrue", getDocumentById(taskId, collNameTask).get("title"));
    }

    @Test
    public void testGetTasksFromDBActive() {
        for (Task task : getTasksFromDB(true, collNameTask)) {
            assertEquals("TesterTrue", task.getTitle());
        }
    }
    @Test
    public void testGetTasksFromDBInActive() {
        for (Task task : getTasksFromDB(false, collNameTask)) {
            assertEquals("TesterFalse", task.getTitle());
        }
    }
    @Test
    public void testGetDBColl() {
        MongoCollection<Document> coll = getDBColl(collNameTask);

        assertEquals("testTask", coll.getNamespace().getCollectionName());
    }

    @Test
    public void testAddCommentToDB() {
        ObjectId id = new ObjectId(taskId);
        MongoCollection<Document> collection = getDBColl(collNameTask);

        // Tilføjer en ny kommentar.
        addCommentToDB(taskId, "tester", collNameTask);

        comments.add("tester");

        assertEquals(comments, getCommentsFromDB(taskId, collNameTask));

        // Fjerner kommentaren efter den er blevet lagt ind i databasen.
        collection.updateOne(Filters.eq("_id", id), Updates.pullAll("comments", comments));
    }

    @Test
    public void testGetCommentsFromDB() {
        ObjectId id = new ObjectId(taskId);
        MongoCollection<Document> collection = getDBColl(collNameTask);

        collection.updateOne(Filters.eq("_id", id), Updates.addToSet("comments", "tester1"));
        collection.updateOne(Filters.eq("_id", id), Updates.addToSet("comments", "tester2"));

        comments.add("tester1");
        comments.add("tester2");

        assertEquals(comments, getCommentsFromDB(taskId, collNameTask));

        // Fjerner kommentaren efter den er blevet lagt ind i databasen.
        collection.updateOne(Filters.eq("_id", id), Updates.pullAll("comments", comments));
    }

    @Test
    public void testGetEmployeesFromDBAdmin() {
        ArrayList<User> users = getEmployeesFromDB(true, collNameUser);

        for (User user : users) {
            assertEquals("Preben Elkjær", user.getFullName());
        }
    }
    @Test
    public void testGetEmployeesFromDBNotAdmin() {
        ArrayList<User> users = getEmployeesFromDB(false, collNameUser);

        for (User user : users) {
            assertEquals("Ernst Elkjær", user.getFullName());
        }
    }

    @Test
    public void testExportTaskToDatabase() {
        exportTaskToDatabase(testTask, collNameTask);
        ArrayList<Task> tasks = getTasksFromDB(true, collNameTask);

        String titleForTask = "";

        for (Task task : tasks) {
            if (task.getTitle().equals("Exportdatabase Tester")) {
                titleForTask = task.getTitle();
            }
        }
        assertEquals("Exportdatabase Tester", titleForTask);

        MongoCollection<Document> collTask = getDBColl(collNameTask);
        collTask.deleteOne(eq("title", testTask.getTitle()));
    }
}