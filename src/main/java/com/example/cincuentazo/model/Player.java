package com.example.cincuentazo.model;

import com.example.cincuentazo.model.intefaces.IPlayer;

import java.util.ArrayList;
import java.util.List;

/**
 * Domain entity model representing a player participating in the match.
 * Holds their hand data, identification metrics, and active gameplay standing.
 * @author Andres Felipe Rodríguez García
 * @version 1.0
 */
public class Player implements IPlayer {

    /** Display name identifying the participant. */
    private final String name;

    /** Indicates whether the participant is controlled by a human user. */
    private final boolean isHuman;

    /** Collection of cards currently held by the player. */
    private final List<Card> hand;

    /** Indicates whether the participant has been eliminated from the match. */
    private boolean eliminated;

    /**
     * Creates a player with the specified identity and control type.
     *
     * @param name player's display name
     * @param isHuman true if controlled by a human user
     */
    public Player(String name, boolean isHuman) {
        this.name = name;
        this.isHuman = isHuman;
        this.hand = new ArrayList<>();
        this.eliminated = false;
    }

    /** Retrieves the player's display name.
     * @return player name */
    public String getName() {
        return name;
    }

    /** Determines whether the participant is controlled by a human user.
     * @return true if the player is human */
    public boolean isHuman() {
        return isHuman;
    }

    /** Retrieves the cards currently held by the player.
     * @return player's hand */
    public List<Card> getHand() {
        return hand;
    }

    /** Indicates whether the player has been eliminated from the current match.
     * @return true if eliminated */
    public boolean isEliminated() {
        return eliminated;
    }

    /**
     * Updates the player's elimination status.
     *
     * @param eliminated new elimination state
     */
    public void setEliminated(boolean eliminated) {
        this.eliminated = eliminated;
    }


    /** Adds a card to the player's hand.
     * @param card card to be added */
    public void addCardToHand(Card card) {
        if (hand.size() < 4) {
            hand.add(card);
        }
    }

    /** Removes a card from the player's hand.
     * @param card card to be removed */
    public void removeCardFromHand(Card card) {
        hand.remove(card);
    }
}
