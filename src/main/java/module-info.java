module com.example.dictionaryjava {
    requires java.desktop;
    requires java.sql;
    requires freetts;
    requires java.net.http;
    requires org.json;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.controls;

    opens com.example.dictionaryjava to javafx.fxml;
    exports com.example.dictionaryjava;
}
