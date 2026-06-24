package com.example.cincuentazo.controller;

import com.example.cincuentazo.view.Path;
import com.example.cincuentazo.view.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.IOException;

public class FinalController {

    @FXML
    private Label winnerLabel;

    @FXML
    private Label roundsLabel;

    @FXML
    private Label timeLabel;

    private static String winnerName;
    private static int roundsPlayed;
    private static String totalTime;

    public static void setMatchResults(
            String winner,
            int rounds,
            String time
    ) {
        winnerName = winner;
        roundsPlayed = rounds;
        totalTime = time;
    }

    @FXML
    public void initialize() {

        winnerLabel.setText(winnerName);

        roundsLabel.setText(
                String.format("%02d", roundsPlayed)
        );

        timeLabel.setText(totalTime);
    }

    @FXML
    private void handlePlayAgain() throws IOException {

        SceneManager.changeScene(Path.GameView);
    }

    @FXML
    private void handleBackToMenu() throws IOException {

        SceneManager.changeScene(Path.MenuView);
    }
}