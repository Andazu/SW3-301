package controller;
import com.mongodb.client.model.Aggregates;
import org.bson.Document;
import org.bson.conversions.Bson;
import com.mongodb.client.ChangeStreamIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.changestream.FullDocument;

import java.util.Arrays;
import java.util.List;

public class UpdateTest {
    public static void main( String[] args ) {
        // Replace the uri string with your MongoDB deployment's connection string
        String url = "mongodb+srv://admin:admin@cluster0.ztdigfr.mongodb.net/?retryWrites=true&w=majority";
        try (MongoClient mongoClient = MongoClients.create(url)) {
            MongoDatabase database = mongoClient.getDatabase("project");
            MongoCollection<Document> collection = database.getCollection("tasks");
            List<Bson> pipeline = Arrays.asList(
                    Aggregates.match(
                            Filters.in("operationType",
                                    Arrays.asList("insert", "update"))));
            ChangeStreamIterable<Document> changeStream = database.watch(pipeline)
                    .fullDocument(FullDocument.UPDATE_LOOKUP);
            // variables referenced in a lambda must be final; final array gives us a mutable integer
            final int[] numberOfEvents = {0};
            changeStream.forEach(event -> {
                System.out.println("Received a change to the collection: " + event);
                if (++numberOfEvents[0] >= 2) {
                    System.exit(0);
                }
            });
        }
    }
}

