package com.example.cincuentazo.controller;

import com.example.cincuentazo.view.Path;
import com.example.cincuentazo.view.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.IOException;

/**
 * Controller class for the match-over / game-over summary screen in Cincuentazo.
 * <p>
 * This class is bound to the FXML end-game view and is responsible for rendering
 * the final match statistics (winner name, total rounds played, and elapsed time)
 * transferred statically from the game controller. It also handles navigational
 * inputs to restart the game loop or return to the main menu.
 *
 * @author Jose Manuel Cardona Gil
 * @version 1.0
 */
public class FinalController {

    @FXML private Label winnerLabel;
    @FXML private Label roundsLabel;
    @FXML private Label timeLabel;

    /** Cached name of the winner to be injected into the UI upon initialization. */
    private static String winnerName;
    /** Cached number of rounds played to be injected into the UI upon initialization. */
    private static int roundsPlayed;
    /** Cached formatted time string to be injected into the UI upon initialization. */
    private static String totalTime;

    /**
     * Statically transfers final match metrics from the game engine context
     * to this controller before transitioning scenes. <p>
     *
     * Since JavaFX controllers are typically instantiated dynamically via the
     * {@code FXMLLoader}, this static bridge pattern ensures data persists
     * seamlessly right before the end-screen scene rendering occurs.
     *
     * @param winner The name or title of the winning entity (e.g., "Player 1" or "AI").
     * @param rounds The total integer count of rounds played during the match.
     * @param time   The pre-formatted duration of the match (e.g., "MM:SS").
     */
    public static void setMatchResults(
            String winner,
            int rounds,
            String time
    ) {
        winnerName = winner;
        roundsPlayed = rounds;
        totalTime = time;
    }

    /**
     * Automatically starts by the JavaFX runtime architecture after the FXML file
     * has been completely loaded and parsed. <p>
     *
     * This method binds the cached static parameters ({@code winnerName}, {@code roundsPlayed},
     * and {@code totalTime}) onto their respective structural UI JavaFX Labels.
     */
    @FXML
    public void initialize() {

        winnerLabel.setText(winnerName);

        roundsLabel.setText(
                String.format("%02d", roundsPlayed)
        );

        timeLabel.setText(totalTime);
    }

    /**
     * Action event handler bound to the "Play Again" button UI element.
     * Triggers a scene switch back to the primary match map or gameplay board
     * layout to reset and start a new game.
     *
     * @throws IOException If a critical loading exception occurs while reading or
     * rendering the target FXML file path.
     * @see SceneManager#changeScene(String)
     */
    @FXML
    private void handlePlayAgain() throws IOException {

        SceneManager.changeScene(Path.GameView);
    }

    /**
     * Action event handler bound to the "Back to Menu" button UI element.
     * Triggers a scene switch navigating the window back to the main startup
     *
     * @throws IOException If a critical loading exception occurs while reading or
     * rendering the target FXML file path.
     * @see SceneManager#changeScene(String)
     */
    @FXML
    private void handleBackToMenu() throws IOException {

        SceneManager.changeScene(Path.MenuView);
    }
}