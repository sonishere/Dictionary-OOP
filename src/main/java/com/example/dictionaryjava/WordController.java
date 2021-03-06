package com.example.dictionaryjava;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;

import javafx.scene.control.*;

import javafx.stage.Stage;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;
import java.util.ResourceBundle;

public class WordController implements Initializable {
    @FXML
    private TextArea printWord;

    @FXML
    private Label printSpeech;

    @FXML
    private TextArea printType;

    @FXML
    private TextArea printMeaning;

    @FXML
    private TextArea printSyn;

    @FXML
    private TextArea printAnt;

    @FXML
    private TextArea printSim;

    @FXML
    private TextArea printEx;

    DatabaseToStorage db = new DatabaseToStorage();
    String defaultURL = "https://wordsapiv1.p.rapidapi.com/words/*/&";

    public void meaningToMain(ActionEvent event) throws IOException {
        FXMLLoader root = new FXMLLoader(MainApplication.class.getResource("MainUI.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root.load());
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styles/style.css")).toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    /**
     * print the chosen Word
     */
    public void printOutput(String searchWord) {
        printWord.setText(searchWord);
        printType.setText(db.typeStore.get(searchWord));
        printSpeech.setText(db.speechStore.get(searchWord));
        printMeaning.setText(db.meaningStore.get(searchWord));
    }

    public void printSynonym(String searchWord) {

        if (searchWord.contains(" ")) {
            searchWord = searchWord.replace(" ", "%20");
        }

        String newURL = defaultURL.replace("*", searchWord);
        String synURL = newURL.replace("&", "synonyms");
//        String exURL = newURL.replace("&", "examples");
        System.out.println(newURL);

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(synURL))
                    .header("x-rapidapi-host", "wordsapiv1.p.rapidapi.com")
                    .header("x-rapidapi-key", "a1cb4a03cbmsh356febaaaaadf1fp1e52f7jsn24e947c5d6db")
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();
            HttpResponse<String> responseSyn = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            JSONObject myObject1 = new JSONObject(responseSyn.body());

            if (myObject1.isNull("synonyms")) {
                printSyn.setText("Doesn't have synonym!");
                return ;
            }

            JSONArray synonym = (JSONArray) myObject1.get("synonyms");

            for (Object c : synonym) {
                String syn = c + "\n";
                printSyn.appendText(syn);
            }

            if (synonym.length() == 0) {
                printSyn.setText("This word doesn't have synonym!");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void printAntonym(String searchWord) {

        if (searchWord.contains(" ")) {
            searchWord = searchWord.replace(" ", "%20");
        }

        String newURL = defaultURL.replace("*", searchWord);
        String antURL = newURL.replace("&", "antonyms");
        System.out.println(newURL);

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(antURL))
                    .header("x-rapidapi-host", "wordsapiv1.p.rapidapi.com")
                    .header("x-rapidapi-key", "a1cb4a03cbmsh356febaaaaadf1fp1e52f7jsn24e947c5d6db")
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            JSONObject myObject = new JSONObject(response.body());

            if (myObject.isNull("antonyms")) {
                printAnt.setText("Doesn't have antonyms!");
                throw new Exception();
            }

            JSONArray antonym = (JSONArray) myObject.get("antonyms");

            for (Object c : antonym) {
                String ant = c + "\n";
                printAnt.appendText(ant);
            }

            if (antonym.length() == 0) {
                printAnt.setText("This word doesn't have antonym!");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void printSimilar(String searchWord) {
        if (searchWord.contains(" ")) {
            searchWord = searchWord.replace(" ", "%20");
        }

        String newURL = defaultURL.replace("*", searchWord);
        String simURL = newURL.replace("&", "similarTo");

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(simURL))
                    .header("x-rapidapi-host", "wordsapiv1.p.rapidapi.com")
                    .header("x-rapidapi-key", "a1cb4a03cbmsh356febaaaaadf1fp1e52f7jsn24e947c5d6db")
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            JSONObject myObject = new JSONObject(response.body());

            if (myObject.isNull("similarTo")) {
                printSim.setText("Doesn't have similar words!");
                throw new Exception();
            }

            JSONArray similar = (JSONArray) myObject.get("similarTo");

            for (Object c : similar) {
                String sim = c + "\n";
                printSim.appendText(sim);
            }

            if (similar.length() == 0) {
                printSim.setText("This word doesn't have similar words!");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void printExample(String searchWord) {

        if (searchWord.contains(" ")) {
            searchWord = searchWord.replace(" ", "%20");
        }

        String newURL = defaultURL.replace("*", searchWord);
        String exURL = newURL.replace("&", "examples");

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(exURL))
                    .header("x-rapidapi-host", "wordsapiv1.p.rapidapi.com")
                    .header("x-rapidapi-key", "a1cb4a03cbmsh356febaaaaadf1fp1e52f7jsn24e947c5d6db")
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            JSONObject myObject = new JSONObject(response.body());

            if (myObject.isNull("examples")) {
                printEx.setText("Doesn't have examples!");
                return ;
            }

            JSONArray example = (JSONArray) myObject.get("examples");

            for (Object c : example) {
                String ex = c + "\n";
                printEx.appendText(ex);
            }

            if (example.length() == 0) {
                printEx.setText("This word doesn't have example!");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


    }

    public void removeWord(ActionEvent event) throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dictionaryDB", "root", "0912231212Abc");

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Warning!");
        confirm.setHeaderText(null);
        confirm.setContentText("Are you sure you want to delete this word?");

        if (confirm.showAndWait().get() == ButtonType.OK) {

            String command = "DELETE FROM dict WHERE word = ?";

            try {
                PreparedStatement preparedStatement = connection.prepareStatement(command);
                preparedStatement.setObject(1, printWord.getText());
                preparedStatement.executeUpdate();

                Alert infor = new Alert(Alert.AlertType.INFORMATION);
                infor.setTitle("Success!");
                infor.setHeaderText(null);
                infor.setContentText("Deleted successfully!");

                // quay ve main
                FXMLLoader root = new FXMLLoader(MainApplication.class.getResource("MainUI.fxml"));
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root.load());
                scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styles/style.css")).toExternalForm());
                stage.setScene(scene);
                stage.show();
            } catch (SQLException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void switchToEdit(ActionEvent event) throws IOException {

        FXMLLoader root = new FXMLLoader(MainApplication.class.getResource("editWord.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root.load());

        EditController editController = root.getController();
        editController.printOldOutput(printWord.getText());

        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styles/styleWord.css")).toExternalForm());
        stage.setScene(scene);
        stage.show();
    }
}
