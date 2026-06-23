package com.example.cincuentazo;

import com.example.cincuentazo.view.Path;
import com.example.cincuentazo.view.SceneManager;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        new SceneManager(primaryStage);

        SceneManager.changeScene(Path.MenuView);
        primaryStage.setTitle("50zo");
    }
}
