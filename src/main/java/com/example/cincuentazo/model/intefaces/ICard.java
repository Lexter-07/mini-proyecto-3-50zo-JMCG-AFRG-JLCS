package com.example.cincuentazo.model.intefaces;

import com.example.cincuentazo.model.Suit;

/**
 * Defines the core contract for a playing card in the Cincuentazo game.
 * <p>
 * Any class representing a game card must implement this interface to provide
 * basic properties such as its suit and numerical rank, which are essential
 * for validating game rules and point calculations.
 *
 * @author Jorge Luis Castro Scarpetta
 * @version 1.0
 */
public interface ICard {

    /**
     * Retrieves the suit of the card.
     * @return The {@link Suit} enum value representing the card's suit
     *         (e.g., CLUBS, DIAMONDS, HEARTS, SPADES).*/
    Suit getSuit();

    /**
     * Retrieves the numerical rank of the card.
     * Typically ranges from 1 (Ace) to 13 (King). This value is used by the
     * game mechanics to calculate the running sum and trigger special card effects.
     *
     * @return An integer representing the card's face value or rank.
     */
    int getRank();
}
