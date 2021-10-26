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
import java.util.regex.Pattern;

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
    public final String passwordSQL = "0912231212Abc";


    /**
     * quay ve main screen
     */
    public void backToMain(ActionEvent event) throws IOException {
        Scene scene;
        Stage stage;
        FXMLLoader root;
        if (!addWord.getText().trim().isEmpty() || !addSpeech.getText().trim().isEmpty() || !addType.getText().trim().isEmpty() || !addMeaning.getText().trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Warning!");
            alert.setHeaderText(null);
            alert.setContentText("You haven't finished your work yet.\nAre you sure you want to exit?");
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

    public void addToDatabase(ActionEvent event) throws SQLException, IOException {
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dictionaryDB", "root", "0912231212Abc");

        if (addWord.getText().trim().isEmpty() || addSpeech.getText().trim().isEmpty() || addType.getText().trim().isEmpty() || addMeaning.getText().trim().isEmpty()) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Error!");
            error.setHeaderText(null);
            error.setContentText("Please fill in all the fields.");
            error.showAndWait();

        } else if (db.checkDuplicate(addWord.getText().trim().toLowerCase(Locale.ROOT).replaceAll("\\s+", " "))) {

            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Error!");
            error.setHeaderText("This word is already exist.");
            error.setContentText("Click OK to browse this word.");
            error.showAndWait();

            FXMLLoader root = new FXMLLoader(MainApplication.class.getResource("meaningWord.fxml"));
            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root.load());

            WordController wordController = root.getController();
            wordController.printOutput(addWord.getText().trim().toLowerCase(Locale.ROOT).replaceAll("\\s+", " "));
            wordController.printSynonym(addWord.getText().trim().toLowerCase(Locale.ROOT).replaceAll("\\s+", " "));
            wordController.printAntonym(addWord.getText().trim().toLowerCase(Locale.ROOT).replaceAll("\\s+", " "));
            wordController.printSimilar(addWord.getText().trim().toLowerCase(Locale.ROOT).replaceAll("\\s+", " "));
            wordController.printExample(addWord.getText().trim().toLowerCase(Locale.ROOT).replaceAll("\\s+", " "));

            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styles/styleWord.css")).toExternalForm());
            stage.setScene(scene);
            stage.show();

        } else if (!Pattern.matches("[a-zA-ZÀ-ȕ.'\s-]{1,100}", addWord.getText())) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Error!");
            error.setHeaderText(null);
            error.setContentText("Dont type any weird characters!");
            error.showAndWait();

        } else {

            String command = "INSERT INTO dict(word, speech, type, meaning) VALUES(?, ?, ?, ?)";

            try {
                PreparedStatement preparedStatement = connection.prepareStatement(command);
                preparedStatement.setString(1, addWord.getText().trim().toLowerCase(Locale.ROOT).replaceAll("\\s+", " "));
                preparedStatement.setString(2, addSpeech.getText());
                preparedStatement.setString(3, addType.getText());
                preparedStatement.setString(4, addMeaning.getText());
                preparedStatement.executeUpdate();

                // hoi user co muon tiep tuc them tu
                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                confirm.setTitle("Success!");
                confirm.setHeaderText("Added successfully!");
                confirm.setContentText("Do you want to add another one?");
                if (confirm.showAndWait().get() == ButtonType.OK) {
                    addWord.clear();
                    addSpeech.clear();
                    addType.clear();
                    addMeaning.clear();
                } else {
                    FXMLLoader root = new FXMLLoader(MainApplication.class.getResource("mainUI.fxml"));
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    Scene scene = new Scene(root.load());
                    scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styles/style.css")).toExternalForm());
                    stage.setScene(scene);
                    stage.show();
                }
            } catch (SQLException | IOException e) {
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
