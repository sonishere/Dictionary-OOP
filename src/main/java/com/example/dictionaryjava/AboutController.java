package com.example.dictionaryjava;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class AboutController {
    @FXML
    void fb1(ActionEvent event) throws URISyntaxException, IOException {
        Desktop.getDesktop().browse(new URI("https://www.facebook.com/nguyentruong.son.7792"));
    }

    @FXML
    void fb2(ActionEvent event) throws URISyntaxException, IOException {
        Desktop.getDesktop().browse(new URI("https://www.facebook.com/mashumaroisded/"));
    }

    @FXML
    void fb3(ActionEvent event) throws URISyntaxException, IOException {
        Desktop.getDesktop().browse(new URI("https://www.facebook.com/poteto.te.kotoba/"));
    }

    @FXML
    void gh1(ActionEvent event) throws URISyntaxException, IOException {
        Desktop.getDesktop().browse(new URI("https://github.com/sonishere"));
    }

    @FXML
    void gh2(ActionEvent event) throws URISyntaxException, IOException {
        Desktop.getDesktop().browse(new URI("https://github.com/mashumarodesu"));
    }

    @FXML
    void gh3(ActionEvent event) throws URISyntaxException, IOException {
        Desktop.getDesktop().browse(new URI("https://github.com/KhoayTay"));
    }
}