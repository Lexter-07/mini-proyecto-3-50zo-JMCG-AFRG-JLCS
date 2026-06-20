package com.example.cincuentazo.model.threads;

import com.example.cincuentazo.model.Card;
import com.example.cincuentazo.model.Game;
import com.example.cincuentazo.model.Player;
import com.example.cincuentazo.model.Turn;

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

        try {

            while (game.getCurrentTurn().isMachine()) {

                Thread.sleep(TURN_DELAY);

                playMachineTurn();

                if (onUpdate != null) {
                    onUpdate.run();
                }
            }

        } catch (InterruptedException exception) {

            interrupt();
        }
    }

    /**
     * Executes the current machine move.
     */
    private void playMachineTurn() {

        Player currentPlayer = game.getCurrentPlayer();

        if (!currentPlayer.isCpu()) {
            return;
        }

        for (int i = 0; i < currentPlayer.getHandSize(); i++) {

            Card card =
                    currentPlayer.getHand()
                            .get(i);

            if (card.canBePlayed(game.getCurrentSum())) {

                try {

                    game.playCard(i);

                } catch (Exception ignored) {
                }

                return;
            }
        }
    }
}
