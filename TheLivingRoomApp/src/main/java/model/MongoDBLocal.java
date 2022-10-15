package model;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

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
}
