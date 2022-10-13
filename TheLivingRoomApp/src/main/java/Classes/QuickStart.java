// THIS IS AN EXAMPLE FOR MONGODB COMMUNICATION

package Classes;

import static com.mongodb.client.model.Filters.eq;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.Iterator;

public class QuickStart {
    public static void main( String[] args ) {
        // Replace the uri string with your MongoDB deployment's connection string
        String uri = "mongodb+srv://admin:admin@cluster0.ztdigfr.mongodb.net/?retryWrites=true&w=majority";
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            //Getting a database and collection
            MongoDatabase database = mongoClient.getDatabase("sample_mflix");
            MongoCollection<Document> collection = database.getCollection("movies");

            //Finding specific document and printing data
            Document doc = collection.find(eq("title", "Back to the Future")).first();
            System.out.println(doc.toJson());

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
        }
        catch (Exception e) {
            System.out.println("Something went wrong.");
        }
    }
}
