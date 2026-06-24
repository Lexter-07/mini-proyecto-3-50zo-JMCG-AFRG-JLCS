package com.example.cincuentazo.model.threads;

import com.example.cincuentazo.model.*;
import com.example.cincuentazo.model.ia.IAPlayer;
import javafx.application.Platform;
import java.util.function.Consumer;

import java.util.List;
import java.util.*;

/**
 * Thread responsible for managing the turn lifecycle in the game.
 * .
 * Controls the time limit for each turn, the automatic execution of moves
 by the Artificial Intelligence (AI), and the updating of the graphical user interface (UI)
 using JavaFX threads.
 * .
 * * @author Jorge Castro
 * @version 2.0
 */
public class TurnThread extends Thread {

    /** The main model that contains the current state of the game. */
    private final GameModel gameModel;

    /** List of AI controlled players available in the game. */
    private final List<IAPlayer> aiPlayers;

    private final Runnable refreshUI;
    private final Runnable onGameEnd;

    private final Consumer<String> historyLogger;
    private volatile boolean running = true;
    private volatile boolean humanTurnDone = false;

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

    public void notifyHumanPlayed() {
        humanTurnDone = true;
    }

    @Override
    public void run() {

        while (running && !gameModel.getTurnSystem().checkVictoryCondition()) {

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

                        Platform.runLater(() -> historyLogger.accept(
                                current.getName() + " jugó " + choice +
                                        " | Suma: " + gameModel.getTableSum()
                        ));

                    } else {
                        gameModel.eliminatePlayer(current);

                        Platform.runLater(() -> historyLogger.accept(
                                current.getName() + " fue ELIMINADO!"
                        ));
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
                    gameModel.eliminatePlayer(current);
                    gameModel.getTurnSystem().advanceTurn();
                    Platform.runLater(() -> {
                        historyLogger.accept(current.getName() + " fue ELIMINADO!");
                        refreshUI.run();
                    });
                    continue;
                }

                // esperar hasta que el humano juegue
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


    public void stopThread() {
        running = false;
        this.interrupt();
    }

}
