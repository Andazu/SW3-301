// THIS IS AN EXAMPLE FOR MONGODB COMMUNICATION

package Classes;

import static com.mongodb.client.model.Filters.eq;

import com.mongodb.client.model.Filters;
import org.bson.Document;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.conversions.Bson;

public class QuickStart {
    public static void main( String[] args ) {
        // Replace the uri string with your MongoDB deployment's connection string
        String uri = "mongodb+srv://admin:admin@cluster0.ztdigfr.mongodb.net/?retryWrites=true&w=majority";
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("sample_mflix");
            MongoCollection<Document> collection = database.getCollection("movies");
            Document doc = collection.find(eq("title", "Back to the Future")).first();
            System.out.println(doc.toJson());

            /*
            //Creating a collection
            database.createCollection("gamers");

            //Preparing a document
            Document document = new Document();
            document.append("name", "Ram");
            document.append("age", 26);
            document.append("city", "Lego City");
            
            //Inserting the document into the collection
            database.getCollection("gamers").insertOne(document);
            System.out.println("Document inserted successfully");
            */

            //Drop collection
            //database.getCollection("gamers").drop();

            //Delete document
            //database.getCollection("gamers").deleteOne(eq("name", "Ram"));
        }
    }
}
