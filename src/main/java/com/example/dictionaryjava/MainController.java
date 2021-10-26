package com.example.dictionaryjava;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class MainController implements Initializable {
    @FXML
    private AnchorPane mainPane;

    @FXML
    private TextField input;

    @FXML
    private ImageView wordImage;

    @FXML
    private ListView<String> searchInfo;

    @FXML
    private HBox alphabetBar;

    private Stage stage;
    private Scene scene;
    private FXMLLoader root;
    String defaultImage = "finalimages/1.png";
    DatabaseToStorage db = new DatabaseToStorage();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        searchInfo.setMouseTransparent(true);
        searchInfo.setFocusTraversable(false);
        ObservableList<String> welcomeText = FXCollections.observableArrayList();
        welcomeText.add("\nWelcome to Adict-ed!");
        welcomeText.add(" → Type on the search bar to find \nyour word");
        welcomeText.add(" → Click on characters to browse words");
        searchInfo.setItems(welcomeText);

    }

    /**
     * Switch scene
     */
    public void switchToWord(MouseEvent event) throws IOException {
        String searchWord = searchInfo.getSelectionModel().getSelectedItem();
        if (searchWord != null) {
            searchInfo.getSelectionModel().selectedItemProperty();
            SwapStage(searchWord);
        }
    }

    public void inputToWord(ActionEvent event) throws IOException {
        String inputWord = input.getText();
        ObservableList<String> listWord = searchInfo.getItems();
        for(String word : listWord) {
            if (inputWord != null && inputWord.toLowerCase(Locale.ROOT).equals(word)) {
                SwapStage(word);
            }
        }
    }

    private void SwapStage(String word) throws IOException {
        root = new FXMLLoader(MainApplication.class.getResource("meaningWord.fxml"));
        stage = (Stage) mainPane.getScene().getWindow();
        scene = new Scene(root.load());
        WordController wordController = root.getController();
        wordController.printOutput(word);

        wordController.printSynonym(word);
        wordController.printAntonym(word);
        wordController.printSimilar(word);
        wordController.printExample(word);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styles/styleWord.css")).toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    public void switchToAdd(ActionEvent event) throws IOException {
        root = new FXMLLoader(MainApplication.class.getResource("addWord.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root.load());
        stage.setScene(scene);
        stage.show();
    }
    /** End of switch scene */

    /**
     * Search with suggestion
     */
    public void onTyping() {
        boolean isRun = false;
        searchInfo.setMouseTransparent(false);
        searchInfo.setFocusTraversable(true);

        ObservableList<String> filterList = FXCollections.observableArrayList();
        String compareText = input.getText();

        if (!compareText.equals(" ")) {
            for (String s : db.wordStore) {
                if (s.startsWith(compareText.toLowerCase())) {
                    filterList.add(s);
                    if(!isRun && !compareText.isEmpty()) {
                        String buttonId = Character.toString(s.charAt(0)).toUpperCase();
                        String newImage = defaultImage.replace("1", buttonId);
                        Image ima = new Image(Objects.requireNonNull(getClass().getResourceAsStream(newImage)));
                        wordImage.setImage(ima);

                        isRun = true;

                    }
                }
            }
            if (filterList.size() == 0) {
                String sus = "\nCan't find your word? \n\nClick the \"Add Word\" button above \nto add a new word!";
                filterList.add(sus);
                searchInfo.setMouseTransparent(true);
                searchInfo.setFocusTraversable(false);
            }
        }
        else {
            String sus = "\nCan't find your word? \n\nClick the \"Add Word\" button above \nto add a new word!";
            filterList.add(sus);
            searchInfo.setMouseTransparent(true);
            searchInfo.setFocusTraversable(false);
        }
        searchInfo.setItems(filterList);
    }


    /**
     * Change alphabet's picture when click button
     */
    public void onClick(ActionEvent event) {
        input.clear();
        alphabetBar.setStyle(null);
        searchInfo.setMouseTransparent(false);
        searchInfo.setFocusTraversable(true);

        char im = 'A';
        String str = "#";
        while (im < 'Z') {
            StringBuilder sb = new StringBuilder();
            sb.append(str).append(im);
            Button b = (Button) alphabetBar.lookup(sb.toString());
            System.out.println(b.getId());
                b.setStyle(null);
            im++;

        }

        String value = ((Button) event.getSource()).getText();
        String newImage = defaultImage.replace("1", value);
        Image i = new Image(Objects.requireNonNull(getClass().getResourceAsStream(newImage)));
        wordImage.setImage(i);
        ObservableList<String> selectedList = FXCollections.observableArrayList();
        for (String a : db.wordStore) {
            if (a.startsWith(value.toLowerCase())) {
                selectedList.add(a);
            }
        }

        searchInfo.setItems(selectedList);
    }

    /**
     * set function for custom button
     */
    public void setMinimize(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    public void setClose(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
    }

    /** end of set function for custom button */


    public void changeButton() {
        char i = 'A';
        String str = "#";
        while (i < 'Z') {
            StringBuilder sb = new StringBuilder();
            sb.append(str).append(i);
            Button b = (Button) alphabetBar.lookup(sb.toString());
            System.out.println(b.getId());
            if (input.getText().toLowerCase(Locale.ROOT).startsWith(b.getId().toLowerCase(Locale.ROOT))) {
                b.setStyle("-fx-background-color: #f97449;");
            }
            else {
                b.setStyle(null);
            }
            i++;

        }
    }
}
