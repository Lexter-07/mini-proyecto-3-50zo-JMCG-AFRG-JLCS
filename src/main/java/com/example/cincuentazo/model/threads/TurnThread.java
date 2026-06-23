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
}
