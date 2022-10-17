package model;

import com.mongodb.client.*;
import org.bson.Document;

import java.util.ArrayList;

import static com.mongodb.client.model.Filters.eq;

public class User {
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String phoneNumber;
    private boolean admin;

    public User(String firstName, String lastName, String emailAddress, String phoneNumber, boolean admin) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
        this.admin = admin;
    }

    public static ArrayList<String> getEmployeesFromDB() {
        ArrayList<String> employees = new ArrayList<>();

        MongoCollection<Document> coll = MongoDBLocal.getDBColl("users");

        for (Document doc : coll.find(eq("admin", false))) {
            ArrayList<Object> values = new ArrayList<>(doc.values());
            String fullName = values.get(1).toString() + ' ' + values.get(2).toString();

            employees.add(fullName);
        }
        return employees;
    }

    public void exportDocument(){
        try {
            MongoDBLocal mongoDBLocal = new MongoDBLocal();
            mongoDBLocal.checkConnection();

            Document document = new Document();
            document.append("firstName", this.firstName);
            document.append("lastName", this.lastName);
            document.append("emailAddress", this.emailAddress);
            document.append("phoneNumber", this.phoneNumber);
            document.append("admin", this.admin);

            MongoClient mongoClient = MongoClients.create(mongoDBLocal.getUrl());
            mongoClient.getDatabase("project").getCollection("users").insertOne(document);
        }
        catch (Exception e) {
            System.out.println("Something went wrong with MongoDB during exportDocument call.");
        }
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
}
