package com.example.cincuentazo.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a player in the Cincuentazo game.
 *
 * <p>A player has:
 * <ul>
 *     <li>A name.</li>
 *     <li>A hand of cards.</li>
 *     <li>A type (human or CPU).</li>
 * </ul>
 *
 * This class provides methods for managing the player's hand during the game.
 *
 * @author Jose Manuel Cardona Gil
 * @version 1.0
 */
public class Player {

    /**
     * Player's name.
     */
    private final String name;

    /**
     * Indicates whether the player is controlled by the computer.
     */
    private final boolean cpu;

    /**
     * Cards currently held by the player.
     */
    private final List<Card> hand;

    /**
     * Creates a new player.
     *
     * @param name player's name
     * @param cpu true if the player is controlled by the computer
     */
    public Player(String name, boolean cpu) {

        this.name = name;
        this.cpu = cpu;
        this.hand = new ArrayList<>();
    }

    /**
     * Returns the player's name.
     *
     * @return player's name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns whether this player is controlled by the computer.
     *
     * @return true if this player is a CPU
     */
    public boolean isCpu() {
        return cpu;
    }

    /**
     * Returns an unmodifiable view of the player's hand.
     *
     * @return player's hand
     */
    public List<Card> getHand() {
        return Collections.unmodifiableList(hand);
    }

    /**
     * Adds a card to the player's hand.
     *
     * @param card card to be added
     */
    public void addCard(Card card) {
        hand.add(card);
    }

    /**
     * Removes and returns the selected card from the player's hand.
     *
     * @param index position of the card
     * @return removed card
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    public Card playCard(int index) {
        return hand.remove(index);
    }

    /**
     * Removes all cards from the player's hand.
     */
    public void clearHand() {
        hand.clear();
    }

    /**
     * Returns the number of cards currently in the player's hand.
     *
     * @return hand size
     */
    public int getHandSize() {
        return hand.size();
    }

    /**
     * Returns whether the player has any cards left.
     *
     * @return true if the hand is empty
     */
    public boolean hasNoCards() {
        return hand.isEmpty();
    }

    /**
     * Returns a string representation of the player.
     *
     * @return player's information
     */
    @Override
    public String toString() {
        return name + " (" + (cpu ? "CPU" : "Human") + ")";
    }

}