package com.example.cincuentazo.model;

import java.util.List;

/**
 * Controls structural turn transitions and handles matching loop sequences.
 * Skippable index points are calculated based on player elimination indices.
 * @author Andres Felipe Rodríguez
 * @version 1.0
 */
public class Turn {

    private int currentPlayerIndex;
    private final List<Player> players;

    public Turn(List<Player> players) {
        this.players = players;
        this.currentPlayerIndex = 0;
    }

    /**
     * Advances the pointer to the next active player inside the rotation pool.
     * * @return the Player instance entitled to execute the upcoming move
     */
    public Player advanceTurn() {
        int attempts = 0;
        do {
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
            attempts++;
        } while (players.get(currentPlayerIndex).isEliminated() && attempts <= players.size());

        return getCurrentPlayer();
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    /**
     * Scans player metrics to establish if only a single survivor remains.
     * * @return true if the game setup meets concluding conditions
     */
    public boolean checkVictoryCondition() {
        long activeCount = players.stream().filter(p -> !p.isEliminated()).count();
        return activeCount <= 1;
    }
}
