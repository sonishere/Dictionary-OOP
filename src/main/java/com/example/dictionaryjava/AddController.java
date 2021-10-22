package com.example.dictionaryjava;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.sql.*;
import java.util.Locale;
import java.util.Objects;

public class AddController {
    @FXML
    private TextField addWord;

    @FXML
    private TextField addSpeech;

    @FXML
    private TextField addType;

    @FXML
    private TextArea addMeaning;

    @FXML
    private Button backToMain;

    @FXML
    private Button resetAdd;

    DatabaseToStorage db = new DatabaseToStorage();


    public void backToMain(ActionEvent event) throws IOException {
        Scene scene;
        Stage stage;
        FXMLLoader root;
        if (!addWord.getText().trim().isEmpty() || !addSpeech.getText().trim().isEmpty() || !addType.getText().trim().isEmpty() || !addMeaning.getText().trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Alert!");
            alert.setHeaderText(null);
            alert.setContentText("You haven't finished your work yet! \nDo you really want to quit?");
            if (alert.showAndWait().get() == ButtonType.OK) {
                root = new FXMLLoader(MainApplication.class.getResource("mainUI.fxml"));
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                scene = new Scene(root.load());
                scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styles/style.css")).toExternalForm());
                stage.setScene(scene);
                stage.show();
            }
        } else {
            root = new FXMLLoader(MainApplication.class.getResource("mainUI.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root.load());
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styles/style.css")).toExternalForm());
            stage.setScene(scene);
            stage.show();
        }
    }

    /**
     * add word to database
     */
    public void addToDatabase(ActionEvent event) throws SQLException {
        if (addWord.getText().trim().isEmpty() && addSpeech.getText().trim().isEmpty() && addType.getText().trim().isEmpty() && addMeaning.getText().trim().isEmpty()) {
            System.out.println("sus");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thiếu dữ liệu nhập vào");
            alert.setHeaderText("Thông báo:");
            alert.setContentText("Hãy điền vào tất cả các mục!");
            alert.showAndWait();

        } else {
            System.out.println("not sus");
            String key = addWord.getText().toLowerCase(Locale.ROOT);
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/dictionaryDB", "root", "0912231212Abc");
            String command = "INSERT INTO dict (word, speech, type, meaning) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = con.prepareStatement(command);
            statement.setObject(1, db.wordStore);
            statement.setObject(2, db.speechStore);
            statement.setObject(3, db.typeStore);
            statement.setObject(4, db.meaningStore);
            statement.execute();
            db.wordStore.add(key);
            db.speechStore.put(key, addSpeech.getText().toLowerCase(Locale.ROOT));
            db.typeStore.put(key, addType.getText().toLowerCase(Locale.ROOT));
            db.meaningStore.put(key, addMeaning.getText().toLowerCase(Locale.ROOT));
        }
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dictionaryDB", "root", "1613877617112001");
        if (addWord.getText().trim().isEmpty() || addSpeech.getText().trim().isEmpty() || addType.getText().trim().isEmpty() || addMeaning.getText().trim().isEmpty()) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Lỗi!");
            error.setHeaderText(null);
            error.setContentText("Hãy điền đầy đủ thông tin.");
            error.showAndWait();
        } else {
            String command = "INSERT INTO dict(word, speech, type, meaning) VALUES(?, ?, ?, ?)";

            try {
                PreparedStatement preparedStatement = connection.prepareStatement(command);
                preparedStatement.setObject(1, addWord.getText());
                preparedStatement.setObject(2, addSpeech.getText());
                preparedStatement.setObject(3, addType.getText());
                preparedStatement.setObject(4, addMeaning.getText());
                preparedStatement.executeUpdate();

                Alert infor = new Alert(Alert.AlertType.INFORMATION);
                infor.setTitle("Thông báo");
                infor.setHeaderText(null);
                infor.setContentText("Thêm từ thành công");
                infor.showAndWait();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void resetAdd(ActionEvent event) throws SQLException {
        addWord.clear();
        addSpeech.clear();
        addType.clear();
        addMeaning.clear();
    }

}
