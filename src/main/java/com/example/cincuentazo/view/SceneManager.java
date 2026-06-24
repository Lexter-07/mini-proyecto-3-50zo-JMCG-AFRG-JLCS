package com.example.cincuentazo.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;

/**
 * Global scene manager for the 50zo (Cincuentazo) card game application.
 * .
 * This class implements a centralized mechanism to handle screen transitions
 * within the primary JavaFX {@link Stage} using FXML files.
 * .
 * Usage Note: Before invoking the static method {@link #changeScene(String)},
 * this class must be initialized by creating an instance and passing the primary stage.
 *
 * @author Jorge Luis Castro Scarpetta
 * @version 2.0
 */
public class SceneManager {

    /** The primary stage of the JavaFX application where scenes are rendered. */
    private static Stage primaryStage;

    /**
     * Initializes the scene manager by assigning the application's primary stage.
     *
     * @param stage The primary {@link Stage} provided by the JavaFX lifecycle
     *              (typically inside the {@code start} method).
     */
    public SceneManager(Stage stage) {
        primaryStage = stage;
    }

    /**
     * Changes the current scene of the primary stage by loading a new FXML file.
     * .
     * This method searches for the FXML file within the project resources, parses it,
     * builds the new component hierarchy ({@link Parent}), creates a new
     * {@link Scene}, and displays it automatically.
     *
     * @param fxmlPath The relative or absolute path to the FXML resource file (e.g., {@code "/views/main.fxml"}).
     * @throws IllegalStateException If an attempt is made to change the scene without
     *                               previously initializing this manager via its constructor.
     * @throws java.io.FileNotFoundException If the path specified in {@code fxmlPath}
     *                                       does not match any existing physical resource.
     * @throws IOException If a critical I/O error occurs while reading or parsing the FXML file
     *                     (e.g., syntax errors in the FXML or a misconfigured controller).
     */
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