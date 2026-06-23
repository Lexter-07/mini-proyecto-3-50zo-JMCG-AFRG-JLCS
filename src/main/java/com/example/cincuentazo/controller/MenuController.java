package com.example.cincuentazo.controller;

import com.example.cincuentazo.view.Path;
import com.example.cincuentazo.view.SceneManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;

public class MenuController {

    @FXML
    void onHandlePlay2(ActionEvent event) throws IOException {
        System.out.println("[TEST] Botón 2 Jugadores presionado. Solicitando cambio de escena...");
        SceneManager.changeScene(Path.GameView);
    }

    @FXML
    void onHandlePlay3(ActionEvent event) throws IOException {
        System.out.println("[TEST] Botón 3 Jugadores presionado. Solicitando cambio de escena...");
        SceneManager.changeScene(Path.GameView);
    }

    @FXML
    void onHandlePlay4(ActionEvent event) throws IOException {
        System.out.println("[TEST] Botón 4 Jugadores presionado. Solicitando cambio de escena...");
        SceneManager.changeScene(Path.GameView);
    }

    @FXML
    void onHandleExit(ActionEvent event) {
        System.out.println("[TEST] Cerrando aplicación...");
        System.exit(0);
    }
}