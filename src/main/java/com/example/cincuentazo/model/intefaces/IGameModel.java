package com.example.cincuentazo.model.intefaces;

import com.example.cincuentazo.model.Card;
import com.example.cincuentazo.model.Player;

/**
 * Defines the core operations required by any game model
 * implementation within the Cincuentazo system. <p>
 *
 * Establishes the contract for match initialization,
 * turn execution, and player elimination management,
 * ensuring consistent interaction between controllers
 * and game state engines.
 */
public interface IGameModel {

    /** Initializes a new match and prepares all game elements for play. */
    void startNewGame();

    /** Executes a player's selected move using the specified card.
     *
     * @param player participant performing the action
     * @param card card selected for the turn */
    void playTurnAction(Player player, Card card);

    /** Removes a player from active participation in the match.
     * @param player player to be eliminated */
    void eliminatePlayer(Player player);

}
