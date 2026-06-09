package com.example.cincuentazo.controller;

import com.example.cincuentazo.view.Path;
import com.example.cincuentazo.view.SceneManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;

public class MenuController {

    @FXML
    void onHandlePlay2(ActionEvent event) throws IOException {
        SceneManager.changeScene(Path.GameView);
    }

    @FXML
    void onHandleExit(ActionEvent event) {
        System.exit(0);
    }
}
