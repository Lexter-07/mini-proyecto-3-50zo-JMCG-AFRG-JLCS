package com.example.cincuentazo;

import com.example.cincuentazo.view.Path;
import com.example.cincuentazo.view.SceneManager;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Main application bootstrap entry point for the Cincuentazo game. <p>
 *
 * This class it's responsible for orchestrating the initial application startup,
 * initializing the global {@link SceneManager} context with the primary workspace stage,
 * configuring window hook listeners, and routing the user to the default entry dashboard view.
 *
 * @author - Jorge Luis Castro Scarpetta <p>
 *         - Jose Manuel Cardona Gil <p>
 *         - Andres Felipe Rodríguez García <p>
 * @version 1.0
 */
public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Prepares and starts the primary canvas lifecycle for the GUI context. <p>
     *
     * This method instantiates the navigation engine by coupling the primary stage, configures an
     * explicit window termination interceptor to kill running background threads cleanly, and shifts
     * the screen viewport context directly onto the main system menu.
     *
     * @param primaryStage The primary {@link Stage} container window built natively by JavaFX
     * for this application.
     * @throws IOException If a critical loading exception occurs while reading or parsing
     * the startup menu FXML file resource.
     * @see SceneManager
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        new SceneManager(primaryStage);

        primaryStage.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });

        // Load the initial menu view layout structure
        SceneManager.changeScene(Path.MenuView);
        primaryStage.setTitle("50zo");
    }
}
