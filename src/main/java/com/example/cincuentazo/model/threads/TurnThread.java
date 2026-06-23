package com.example.cincuentazo.model.threads;

import com.example.cincuentazo.model.*;
import com.example.cincuentazo.model.ia.IAPlayer;
import javafx.application.Platform;

import java.util.List;

/**
 * Thread responsible for managing the turn lifecycle in the game.
 * .
 * Controls the time limit for each turn, the automatic execution of moves
 by the Artificial Intelligence (AI), and the updating of the graphical user interface (UI)
 using JavaFX threads.
 * .
 * * @author Jorge Castro
 * @version 1.0
 */
public class TurnThread extends Thread {

    /** The main model that contains the current state of the game. */
    private final GameModel gameModel;

    /** List of AI controlled players available in the game. */
    private final List<IAPlayer> aiPlayers;

    private final Runnable refreshUI;

    /** Maximum time in seconds allocated to each player to make their move. */
    private final int turnTimeSeconds;

    private boolean running = true;

    public TurnThread(GameModel gameModel,
                       List<IAPlayer> aiPlayers,
                       Runnable refreshUI,
                       int turnTimeSeconds) {

        this.gameModel = gameModel;
        this.aiPlayers = aiPlayers;
        this.refreshUI = refreshUI;
        this.turnTimeSeconds = turnTimeSeconds;
    }

import java.util.Random;

/**
 * Controls automatic machine turns.
 *
 * <p>This thread waits a short period of time between turns
 * and makes the machine players play automatically.
 *
 * The thread stops once the turn returns to the human player
 * or when the game reaches its end condition.
 *
 * This class contains no JavaFX code.
 *
 * @author Jose Manuel Cardona Gil
 * @version 1.0
 */
public class TurnThread extends Thread {

    /**
     * Current game.
     */
    private final Game game;

    /**
     * Callback executed after every machine move.
     */
    private final Runnable onUpdate;

    /**
     * Random number generator.
     */
    private final Random random;

    /**
     * Delay between machine turns.
     */
    private static final long TURN_DELAY = 1200;

    /**
     * Creates a new turn thread.
     *
     * @param game current game
     * @param onUpdate callback used to refresh the UI
     */
    public TurnThread(Game game, Runnable onUpdate) {

        this.game = game;

        this.onUpdate = onUpdate;

        this.random = new Random();
    }

    /**
     * Executes machine turns.
     */
    @Override
    public void run() {

        while (running && !gameModel.getTurnSystem().checkVictoryCondition()) {

            Player current = gameModel.getTurnSystem().getCurrentPlayer();

            // show turn in UI
            Platform.runLater(refreshUI);

            System.out.println("[TURN] Turno de: " + current.getName());

            boolean played = false;

            for (int t = turnTimeSeconds; t > 0; t--) {

                int finalT = t;

                // refresh visual timer
                Platform.runLater(() -> {
                    System.out.println("Tiempo restante: " + finalT);
                });

                // if is IA → plays automatically
                if (!current.isHuman()) {

                    try {
                        Thread.sleep(1000); // simulate thinking
                    } catch (InterruptedException e) {}

                    int index = gameModel.getPlayers().indexOf(current) - 1;
                    IAPlayer ai = aiPlayers.get(index);

                    Card choice = ai.chooseCard(current, gameModel.getTableSum());

                    if (choice != null) {
                        try {
                            gameModel.playTurnAction(current, choice);
                            played = true;
                        } catch (Exception ignored) {}
                    }

                    break;
                }

                // if is human wait input
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {}
            }

            // if machine dont played is eliminated
            if (!played && !current.isHuman()) {
                gameModel.eliminatePlayer(current);
                System.out.println(current.getName() + " eliminado por tiempo");
            }

            // next turn
            gameModel.getTurnSystem().advanceTurn();

            Platform.runLater(refreshUI);
        }

        System.out.println("Juego terminado");
    }

    public void stopManager() {
        running = false;
    }
}
