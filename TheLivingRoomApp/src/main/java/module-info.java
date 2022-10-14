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

    opens Controller to javafx.fxml;
    exports Controller;
    exports Launcher;
    opens Launcher to javafx.fxml;
}