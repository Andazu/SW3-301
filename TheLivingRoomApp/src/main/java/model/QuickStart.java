// THIS IS AN EXAMPLE FOR MONGODB COMMUNICATION

package model;

import com.mongodb.client.*;

import static com.mongodb.client.model.Filters.eq;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;

public class QuickStart {
    public static void main( String[] args ) {
        // Replace the uri string with your MongoDB deployment's connection string
        String uri = "mongodb+srv://admin:admin@cluster0.ztdigfr.mongodb.net/?retryWrites=true&w=majority";
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            //Getting a database and collection
            //MongoDatabase database = mongoClient.getDatabase("sample_mflix");
            //MongoCollection<Document> collection = database.getCollection("movies");

            //Finding specific document and printing data
            //Document doc = collection.find(eq("title", "Back to the Future")).first();
            //System.out.println(doc.toJson());

            //Creating a collection
            //database.createCollection("gamers");

            //Preparing a document
            /*
            Document document = new Document();
            document.append("name", "Ram");
            document.append("age", 26);
            document.append("city", "Lego City");
             */

            //Inserting the document into the collection
            //database.getCollection("gamers").insertOne(document);

            //Dropping a collection
            //database.getCollection("gamers").drop();

            //Deleting a document
            //database.getCollection("gamers").deleteOne(eq("name", "Ram"));

            //Retrieving all documents from a collection
            /*
            FindIterable<Document> iterDoc = collection.find();
            Iterator it = iterDoc.iterator();
            while (it.hasNext()) {
                System.out.println(it.next());
            }
             */

            //Updating a document
            /*
            MongoCollection<Document> collection2 = database.getCollection("gamers");
            collection2.updateOne(Filters.eq("name", "Ram"), Updates.set("city", "Lego City"));
             */

            User user = new User("Anne", "Hansen", "example@gmail.com", "+4531405826", false);
            //user.exportDocument();

            // Random data to save
            ArrayList<String> comments = new ArrayList<String>();
            comments.add("wack");
            comments.add("nice job");
            comments.add("bomboclaat");
            comments.add("buyaka buyaka");

            ArrayList<String> assignees = new ArrayList<String>();
            assignees.add("assigneeId1");
            assignees.add("assigneeId2");

            LocalDate date = LocalDate.of(2022,10,1);
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

            User testUser = new User("Anne", "Hansen", "example@gmail.com", "+4531405826", false);
            //database.getCollection("users").insertOne(testUser);

            Task task = new Task("Sandwich Time", "Butter + chicken", "Every week", "Extremely urgent", "Miscellaneous", 75, true, comments, assignees, date);
            //task.exportDocument();
        }
        catch (Exception e) {
            System.out.println("Something went wrong.");
        }
    }
}
