module com.example.dictionaryjava {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.sql;
    requires freetts;
    requires httpcore;
    requires httpclient;
    requires java.net.http;
    requires org.json;

    opens com.example.dictionaryjava to javafx.fxml;
    exports com.example.dictionaryjava;
}
