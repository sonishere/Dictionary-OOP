package com.example.dictionaryjava;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
    private Button A, B, C, D;

    @FXML
    private HBox alphabetBar;

    private Stage stage;
    private Scene scene;
    private FXMLLoader root;
    String defaultImage = "finalimages/1.png";
    DatabaseToStorage db = new DatabaseToStorage();

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        searchInfo.setMouseTransparent(true);
        searchInfo.setFocusTraversable(false);
        ObservableList<String> welcomeText = FXCollections.observableArrayList();
        welcomeText.add("\nWelcome to Adict-ed! \nTo get start, you can: ");
        welcomeText.add(" → Click on the search bar");
        welcomeText.add(" → Click on alphabet button");
        searchInfo.setItems(welcomeText);

    }

    /**
     * Switch scene
     */
    public void switchToWord(MouseEvent event) throws IOException {
        String searchWord = searchInfo.getSelectionModel().getSelectedItem();
        if (searchWord != null) {
            searchInfo.getSelectionModel().selectedItemProperty();
            root = new FXMLLoader(MainApplication.class.getResource("meaningWord.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root.load());
            WordController wordController = root.getController();
            wordController.printOutput(searchWord);
            wordController.printSynonym(searchWord);
            wordController.printAntonym(searchWord);
            wordController.printSimilar(searchWord);
            wordController.printExample(searchWord);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styles/styleWord.css")).toExternalForm());
            stage.setScene(scene);
            stage.show();
        }
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
    public void onTyping() throws IOException {
        boolean isRun = false;
        searchInfo.setMouseTransparent(false);
        searchInfo.setFocusTraversable(true);

        ObservableList<String> filterList = FXCollections.observableArrayList();
        String compareText = input.getText();
        if (!compareText.equals(" ")) {
            for (String s : db.wordStore) {
                if (s.startsWith(compareText.toLowerCase())) {
                    filterList.add(s);
                    if (!isRun) {
                        String buttonId = Character.toString(s.charAt(0)).toUpperCase();
                        String newImage = defaultImage.replace("1", buttonId);
                        Image ima = new Image(Objects.requireNonNull(getClass().getResourceAsStream(newImage)));
                        wordImage.setImage(ima);

                        root = new FXMLLoader(MainApplication.class.getResource("mainUI.fxml"));
                        stage = (Stage) mainPane.getScene().getWindow();
                        scene = new Scene(root.load());
                        char i = 'A';
                        String str = "#";
                        System.out.println(i);

                        while (i < 'Z') {
                            StringBuilder sb = new StringBuilder();
                            sb.append(str).append(i);
                            Button b = (Button) alphabetBar.lookup(sb.toString());

                            System.out.println(b.getId());
                            if (compareText.toLowerCase(Locale.ROOT).startsWith(b.getId().toLowerCase(Locale.ROOT))) {
                                b.setStyle("-fx-background-color: #f97449;");
                                System.out.println("success");
                            } else {
                                b.setStyle("-fx-background-color: transparent;");
                                System.out.println("fail");
                            }
                            i++;

                        }
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
        searchInfo.setItems(filterList);
    }


    /**
     * Change alphabet's picture when click button
     */
    public void onClick(ActionEvent event) throws IOException {
        input.clear();
        searchInfo.setMouseTransparent(false);
        searchInfo.setFocusTraversable(true);
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


}
