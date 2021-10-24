package com.example.dictionaryjava;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;

public class EditController {

    @FXML
    private TextField newWord;

    @FXML
    private TextField newType;

    @FXML
    private TextField newSpeech;

    @FXML
    private TextArea newMeaning;

    @FXML
    private Button submitWord;

    @FXML
    private Button resetAdd;

    @FXML
    private Button quickCopy;

    @FXML
    private TextField oldWord;

    @FXML
    private TextField oldType;

    @FXML
    private TextField oldSpeech;

    @FXML
    private TextArea oldMeaning;

    DatabaseToStorage db = new DatabaseToStorage();

    public void printOldOutput(String Word) {
        oldWord.setText(Word);
        oldSpeech.setText(db.speechStore.get(Word));
        oldType.setText(db.typeStore.get(Word));
        oldMeaning.setText(db.meaningStore.get(Word));
    }

    @FXML
    void addToDatabase(ActionEvent event) throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dictionaryDB", "root", "1613877617112001");

        if (newWord.getText().trim().isEmpty() || newSpeech.getText().trim().isEmpty() || newType.getText().trim().isEmpty() || newMeaning.getText().trim().isEmpty()) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Lỗi!");
            error.setHeaderText(null);
            error.setContentText("Hãy điền đầy đủ thông tin.");
            error.showAndWait();
        } else {
            String command = "UPDATE dict SET word = ?, speech = ?, type = ?, meaning = ? WHERE word = ?";

            try {
                PreparedStatement preparedStatement = connection.prepareStatement(command);
                preparedStatement.setObject(1, newWord.getText());
                preparedStatement.setObject(2, newSpeech.getText());
                preparedStatement.setObject(3, newType.getText());
                preparedStatement.setObject(4, newMeaning.getText());
                preparedStatement.setObject(5, oldWord.getText());
                preparedStatement.executeUpdate();

                Alert infor = new Alert(Alert.AlertType.INFORMATION);
                infor.setTitle("Thông báo");
                infor.setHeaderText(null);
                infor.setContentText("Sửa từ thành công");
                infor.showAndWait();


                FXMLLoader root = new FXMLLoader(MainApplication.class.getResource("meaningWord.fxml"));
                Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root.load());
                WordController wordController = root.getController();
                wordController.printOutput(newWord.getText());
                wordController.printSynonym(newWord.getText());
                wordController.printAntonym(newWord.getText());
                wordController.printSimilar(newWord.getText());
                wordController.printExample(newWord.getText());
                scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styles/styleWord.css")).toExternalForm());
                stage.setScene(scene);
                stage.show();

            } catch (SQLException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void addToMain(ActionEvent event) throws IOException {
        Scene scene;
        Stage stage;
        FXMLLoader root;
        if (!newWord.getText().trim().isEmpty() || !newSpeech.getText().trim().isEmpty() || !newType.getText().trim().isEmpty() || !newMeaning.getText().trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Alert!");
            alert.setHeaderText(null);
            alert.setContentText("Bạn vẫn chưa hoàn thành xong?\nBạn có chắc muốn thoát chứ?");
            if (alert.showAndWait().get() == ButtonType.OK) {
                root = new FXMLLoader(MainApplication.class.getResource("meaningWord.fxml"));
                stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
                scene = new Scene(root.load());
                WordController wordController = root.getController();
                wordController.printOutput(oldWord.getText());
                wordController.printSynonym(oldWord.getText());
                wordController.printAntonym(oldWord.getText());
                wordController.printSimilar(oldWord.getText());
                wordController.printExample(oldWord.getText());
                scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styles/styleWord.css")).toExternalForm());
                stage.setScene(scene);
                stage.show();
            }
        } else {
            root = new FXMLLoader(MainApplication.class.getResource("meaningWord.fxml"));
            stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root.load());
            WordController wordController = root.getController();
            wordController.printOutput(oldWord.getText());
            wordController.printSynonym(oldWord.getText());
            wordController.printAntonym(oldWord.getText());
            wordController.printSimilar(oldWord.getText());
            wordController.printExample(oldWord.getText());
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styles/styleWord.css")).toExternalForm());
            stage.setScene(scene);
            stage.show();
        }
    }

    @FXML
    void quickCopy(ActionEvent event) {
        newWord.setText(oldWord.getText());
        newSpeech.setText(oldSpeech.getText());
        newType.setText(oldType.getText());
        newMeaning.setText(oldMeaning.getText());
    }

    public void resetAdd(ActionEvent event) throws SQLException {
        newWord.clear();
        newSpeech.clear();
        newType.clear();
        newMeaning.clear();
    }

}
