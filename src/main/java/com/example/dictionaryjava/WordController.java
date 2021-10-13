package com.example.dictionaryjava;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import com.sun.speech.freetts.*;
import java.io.IOException;
import java.util.Objects;

public class WordController {
    @FXML
    private TextArea printWord;

    @FXML
    private Button printSpeech;

    @FXML
    private TextArea printType;

    @FXML
    private TextArea printMeaning;

    DatabaseToStorage db = new DatabaseToStorage();
    private static final String VOICENAME = "kevin16";
    private static String s;

    public void meaningToMain(ActionEvent event) throws IOException {
        FXMLLoader root = new FXMLLoader(MainApplication.class.getResource("MainUI.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root.load());
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styles/style.css")).toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    /** print the chosen Word */
    public void printOutput(String searchWord) {
        printWord.setText(searchWord);
        printType.setText(db.typeStore.get(searchWord));
        printSpeech.setText(db.speechStore.get(searchWord));
        printMeaning.setText(db.meaningStore.get(searchWord));
    }

    public void wordTTS(ActionEvent event) {
        Voice voice;
        System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
        voice = VoiceManager.getInstance().getVoice("kevin16");
        if (voice != null) {
            voice.allocate();// Allocating Voice
            try {
                voice.speak(printWord.getText());

            } catch (Exception e1) {
                e1.printStackTrace();
            }

        } else {
            throw new IllegalStateException("Cannot find voice: kevin16");
        }
    }
}
