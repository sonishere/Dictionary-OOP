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
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;


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
            error.setTitle("Error!");
            error.setHeaderText(null);
            error.setContentText("Please fill in all the fields.");
            error.showAndWait();

        } else if (!Pattern.matches("[a-zA-ZÀ-ȕ.'\s-]{1,100}", newWord.getText())) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Error!");
            error.setHeaderText(null);
            error.setContentText("Dont type any weird characters!");
            error.showAndWait();

        } else {
            String command = "UPDATE dict SET word = ?, speech = ?, type = ?, meaning = ? WHERE word = ?";

            try {
                PreparedStatement preparedStatement = connection.prepareStatement(command);
                preparedStatement.setString(1, newWord.getText().trim().toLowerCase(Locale.ROOT).replaceAll("\\s+", " "));
                preparedStatement.setString(2, newSpeech.getText());
                preparedStatement.setString(3, newType.getText());
                preparedStatement.setString(4, newMeaning.getText());
                preparedStatement.setString(5, oldWord.getText());
                preparedStatement.executeUpdate();

                Alert infor = new Alert(Alert.AlertType.INFORMATION);
                infor.setTitle("Success");
                infor.setHeaderText(null);
                infor.setContentText("Edited successfully!");
                infor.showAndWait();


                FXMLLoader root = new FXMLLoader(MainApplication.class.getResource("meaningWord.fxml"));
                Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root.load());
                WordController wordController = root.getController();
                wordController.printOutput(newWord.getText().trim().replaceAll("\\s+", " "));
                wordController.printSynonym(newWord.getText().trim().replaceAll("\\s+", " "));
                wordController.printAntonym(newWord.getText().trim().replaceAll("\\s+", " "));
                wordController.printSimilar(newWord.getText().trim().replaceAll("\\s+", " "));
                wordController.printExample(newWord.getText().trim().replaceAll("\\s+", " "));
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
            alert.setContentText("You haven't finished your work yet.\nAre you sure you want to exit?");
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
    void wordCopy(ActionEvent event) {
        newWord.setText(oldWord.getText());
    }

    @FXML
    void speechCopy(ActionEvent event) {
        newSpeech.setText(oldSpeech.getText());
    }

    @FXML
    void typeCopy(ActionEvent event) {
        newType.setText(oldType.getText());
    }

    @FXML
    void meaningCopy(ActionEvent event) {
        newMeaning.setText(oldMeaning.getText());
    }

    public void resetAdd(ActionEvent event) throws SQLException {
        newWord.clear();
        newSpeech.clear();
        newType.clear();
        newMeaning.clear();
    }
}

