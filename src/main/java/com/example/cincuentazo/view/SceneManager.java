package com.example.cincuentazo.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;

public class SceneManager {

    private static Stage primaryStage;

    public SceneManager(Stage stage) {
        primaryStage = stage;
    }

    public static void changeScene(String fxmlPath) throws IOException {
        if (primaryStage == null) {
            throw new IllegalStateException("SceneManager no inicializado.");
        }

        System.out.println("[TEST] Intentando cargar el recurso FXML: " + fxmlPath);
        URL resourceUrl = SceneManager.class.getResource(fxmlPath);

        if (resourceUrl == null) {
            System.err.println("[TEST ALERTA] No se encontró el FXML en la ruta: " + fxmlPath);
            throw new java.io.FileNotFoundException("Ruta no encontrada: " + fxmlPath);
        }

        Parent root = FXMLLoader.load(resourceUrl);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
        System.out.println("[TEST] Escena cambiada con éxito.");
    }
}