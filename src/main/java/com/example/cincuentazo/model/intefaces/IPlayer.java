package com.example.cincuentazo.model.intefaces;

import com.example.cincuentazo.model.Card;

import java.util.List;

/**
 * Defines the required behavior of a participant
 * within the Cincuentazo game. <p>
 *
 * Provides access to player identity, hand management,
 * elimination status, and card manipulation operations,
 * regardless of whether the participant is human or AI-controlled.
 *
 * @author Jorge Luis Castro
 * @version 1.0
 */
public interface IPlayer {

    /** Retrieves the player's display name.
     * @return player name */
    String getName();

    /** Determines whether the participant is controlled by a human user.
     * @return true if the player is human */
    boolean isHuman();

    /** Retrieves the cards currently held by the player.
     * @return player's hand */
    List<Card> getHand();

    /** Indicates whether the player has been eliminated from the current match.
     * @return true if eliminated */
    boolean isEliminated();

    /** Adds a card to the player's hand.
     * @param card card to be added */
    void addCardToHand(Card card);

    /** Removes a card from the player's hand.
     * @param card card to be removed */
    void removeCardFromHand(Card card);
}
