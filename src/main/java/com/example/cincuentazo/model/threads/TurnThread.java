package com.example.cincuentazo.model.threads;

import com.example.cincuentazo.model.*;
import com.example.cincuentazo.model.ia.IAPlayer;
import javafx.application.Platform;
import java.util.function.Consumer;

import java.util.List;
import java.util.*;

/**
 * Thread responsible for managing the turn lifecycle in the game. <p>
 *
 * Coordinates player rotation, executes AI decisions, processes
 * human-player synchronization, and schedules graphical interface
 * updates through the JavaFX Application Thread. <p>
 *
 * The thread remains active until a victory condition is reached,
 * or it is explicitly stopped.
 *
 * @author Jorge Luis Castro Scarpetta
 * @version 2.1
 */
public class TurnThread extends Thread {

    /** The main model that contains the current state of the game. */
    private final GameModel gameModel;

    /** List of AI controlled players available in the game. */
    private final List<IAPlayer> aiPlayers;


    /** Callback used to refresh the graphical interface. */
    private final Runnable refreshUI;
    /** Callback executed when a victory condition is reached. */
    private final Runnable onGameEnd;
    /** Consumer responsible for recording events in the match history. */
    private final Consumer<String> historyLogger;


    /** Indicates whether the thread should continue executing. */
    private volatile boolean running = true;

    /** Signals that the human player has completed their turn. */
    private volatile boolean humanTurnDone = false;

    /** Indicates whether turn processing is temporarily suspended. */
    private volatile boolean paused = false;



    /**
     * Creates a turn management thread associated with the current match. <p>
     *
     * Receives references to the game model, AI participants, UI update
     * callbacks, history logging functionality, and the game-ending handler.
     *
     * @param gameModel central game state manager
     * @param aiPlayers list of AI-controlled players
     * @param refreshUI callback responsible for refreshing the interface
     * @param historyLogger callback used to register gameplay events
     * @param onGameEnd callback executed when the match concludes
     */
    public TurnThread(GameModel gameModel,
                      List<IAPlayer> aiPlayers,
                      Runnable refreshUI,
                      Consumer<String> historyLogger,
                      Runnable onGameEnd) {

        this.gameModel = gameModel;
        this.aiPlayers = aiPlayers;
        this.refreshUI = refreshUI;
        this.historyLogger = historyLogger;
        this.onGameEnd = onGameEnd;

        setDaemon(true);
    }


    /**
     * Notifies the thread that the human player has completed
     * their move and the turn cycle may continue.
     */
    public void notifyHumanPlayed() {
        humanTurnDone = true;
    }


    /**
     * Main execution loop of the turn manager. <p>
     *
     * Continuously evaluates the current player, executes AI actions,
     * waits for human interaction when necessary, processes eliminations,
     * and updates the graphical interface until a victory condition
     * is reached or the thread is stopped.
     */
    @Override
    public void run() {

        while (running && !gameModel.getTurnSystem().checkVictoryCondition()) {

            while (paused && running) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    return;
                }
            }

            Player current = gameModel.getTurnSystem().getCurrentPlayer();

            // update UI
            Platform.runLater(refreshUI);

            if (!current.isHuman() && !current.isEliminated()) {

                try {
                    Thread.sleep(1500 + (int) (Math.random() * 1500));
                } catch (InterruptedException e) {
                    return;
                }

                int index = gameModel.getPlayers().indexOf(current) - 1;
                IAPlayer ai = aiPlayers.get(index);

                Card choice = ai.chooseCard(current, gameModel.getTableSum());


                try {
                    if (choice != null) {
                        gameModel.playTurnAction(current, choice);

                        Platform.runLater(() -> {
                            historyLogger.accept(
                                    current.getName() + " jugó " + choice +
                                            " | Suma: " + gameModel.getTableSum()
                            );
                            refreshUI.run();
                        });

                    } else {
                        gameModel.eliminatePlayer(current);

                        Platform.runLater(() -> historyLogger.accept(
                                current.getName() + " fue ELIMINADO!"
                        ));
                    }

                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        return;
                    }

                    gameModel.getTurnSystem().advanceTurn();
                    Platform.runLater(refreshUI);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            else {
                boolean hasValidMove = false;
                for (Card c : current.getHand()) {
                    if (GameRules.isValidMove(c, gameModel.getTableSum())) {
                        hasValidMove = true;
                        break;
                    }
                }

                if (!hasValidMove && !current.isEliminated()) {
                    Platform.runLater(refreshUI);

                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        return;
                    }
                    continue;
                }

                // Wait until human plays
                while (!humanTurnDone && running) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        return;
                    }
                }

                // reset flag
                humanTurnDone = false;
            }
        }
        if (running) {
            onGameEnd.run();
        }
    }


    /**
     * Stops the execution of the turn management thread
     * and interrupts any ongoing waiting operation.
     */
    public void stopThread() {
        running = false;
        this.interrupt();
    }


    /**
     * Temporarily suspends turn processing. <p>
     *
     * While paused, the thread remains alive but does not
     * advance turns or execute AI actions.
     */
    public void pauseThread() {
        paused = true;
    }


    /** Resumes turn processing after a pause request. */
    public void resumeThread() {
        paused = false;
    }

}
