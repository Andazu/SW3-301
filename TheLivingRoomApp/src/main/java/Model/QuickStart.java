// THIS IS AN EXAMPLE FOR MONGODB COMMUNICATION

<<<<<<< Updated upstream:TheLivingRoomApp/src/main/java/Model/QuickStart.java
package Model;

import static com.mongodb.client.model.Filters.eq;

import com.mongodb.client.*;
import org.bson.Document;
=======
package models;
import com.mongodb.client.*;

import static com.mongodb.client.model.Filters.eq;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
>>>>>>> Stashed changes:TheLivingRoomApp/src/main/java/models/QuickStart.java

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
            user.exportDocument();

            // Random data to save
            ArrayList<String> comments = new ArrayList<String>();
            comments.add("wack");
            comments.add("nice job");
            comments.add("bomboclaat");
            comments.add("buyaka buyaka");

            ArrayList<String> assignees = new ArrayList<String>();
            assignees.add("assigneeID1");
            assignees.add("assigneeID2");

            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

            Task task = new Task("Sandwich Time", "Butter + chicken", "Every week", "Extremely urgent", "Miscellaneous", 75, true, comments, assignees, date);
            task.exportDocument();
        }
        catch (Exception e) {
            System.out.println("Something went wrong.");
        }
    }
}
