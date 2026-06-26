package com.example.cincuentazo.model.intefaces;

import com.example.cincuentazo.model.Player;

/**
 * Defines the contract responsible for managing turn progression
 * and active player retrieval within a match. <p>
 *
 * Implementations must provide mechanisms for advancing the turn
 * sequence and identifying the participant currently entitled
 * to perform a move.
 *
 * @author Jorge Luis Castro Scarpetta
 * @version 1.0
 */
public interface ITurn {

    /** Advances the turn cycle and determines the next active player.
     * @return the player whose turn becomes active. */
    Player advanceTurn();

    /** Retrieves the participant currently holding the turn.
     * @return active player. */
    Player getCurrentPlayer();
}
