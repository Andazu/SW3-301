module com.example.testafbootstrapjavafx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires org.mongodb.bson;
    requires org.mongodb.driver.sync.client;
    requires org.mongodb.driver.core;

    opens controller to javafx.fxml;
    exports controller;
    exports launcher;
    opens launcher to javafx.fxml;
}