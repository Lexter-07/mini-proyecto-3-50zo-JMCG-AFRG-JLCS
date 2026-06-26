package com.example.cincuentazo.model;

import com.example.cincuentazo.model.intefaces.ICard;

/**
 * Represents a playing card with a specific suit and rank.
 * Automatically generates its resource image path based on its properties.
 * @author Andres Felipe Rodríguez, Jorge Luis Castro
 * @version 1.0
 */
public class Card implements ICard {

    private final Suit suit;
    private final int rank; // 1 (Ace) to 13 (King)

    /**
     * Constructs a new Card with the specified suit and rank.
     * @param suit the suit of the card
     * @param rank the rank of the card (1-13)
     */
    public Card(Suit suit, int rank) {
        this.suit = suit;
        this.rank = rank;
    }

    public Suit getSuit() {
        return suit;
    }

    public int getRank() {
        return rank;
    }

    /**
     * Dynamically generates the path to the card's PNG image asset.
     * Format example: "/com/example/cincuentazo/CARDS_PNG/SPADES-01.png"
     * @return the absolute resource path string of the card image
     */
    public String getImagePath() {
        return String.format("/com/example/cincuentazo/CARDS_PNG/%s-%02d.png", suit.name(), rank);
    }

    @Override
    public String toString() {
        return suit.name() + "-" + rank;
    }
}
