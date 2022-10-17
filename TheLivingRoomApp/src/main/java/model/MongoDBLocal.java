package model;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

public class MongoDBLocal {
    final String url = "mongodb+srv://admin:admin@cluster0.ztdigfr.mongodb.net/?retryWrites=true&w=majority";

    public MongoDBLocal() {
    }

    public String getUrl() {
        return url;
    }

    public void checkConnection(){
        try (MongoClient mongoClient = MongoClients.create(this.getUrl())) {
            System.out.println("Connection successful.");
        }
        catch (Exception e) {
            System.out.println("Something went wrong with MongoDB.");
        }
    }

    public static MongoCollection<Document> getDBColl(String collPath) {
        MongoDBLocal mongoDBLocal = new MongoDBLocal();
        mongoDBLocal.checkConnection();
        MongoClient mongoClient = MongoClients.create(mongoDBLocal.getUrl());
        MongoCollection<Document> coll = mongoClient.getDatabase("project").getCollection(collPath);
        return coll;
    }
}
