package controller;

import com.mongodb.client.MongoCollection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import model.User;
import org.bson.Document;
import org.bson.types.ObjectId;

import static com.mongodb.client.model.Filters.eq;
import static controller.DatabaseMethods.getDBColl;

public class SelectedEmployeeBoxController {
    @FXML
    private Label name;
    @FXML
    private Label role;
    @FXML
    private CheckBox checkBox;

    public void setLabelInBoxUI(User user) {
        checkBox.setId(user.getId());
        name.setText(user.getFullName());
        role.setText(user.getRole());
    }

    public void selectionEvent(ActionEvent event) {
        MongoCollection<Document> userColl = getDBColl("users");
        MongoCollection<Document> tempColl = getDBColl("tempUsers");

        assert tempColl != null;
        Document docFoundInTemp = tempColl.find(eq("_id", new ObjectId(checkBox.getId()))).first();

        if (docFoundInTemp == null){
            assert userColl != null;
            Document docToBeInserted = userColl.find(eq("_id", new ObjectId(checkBox.getId()))).first();
            assert docToBeInserted != null;
            tempColl.insertOne(docToBeInserted);
        }
        else{
            tempColl.deleteOne(docFoundInTemp);
        }
    }
}
