package com.example.dictionaryjava;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

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
    private TextField oldWord;

    @FXML
    private TextField oldType;

    @FXML
    private TextField oldSpeech;

    @FXML
    private TextArea oldMeaning;

    @FXML
    void addToDatabase(ActionEvent event) {

    }

    @FXML
    void addToMain(ActionEvent event) {

    }

}

