package com.example.cincuentazo.model;

import java.util.Objects;

/**
 * Represents a playing card used in the Cincuentazo game.
 *
 * <p>Each card has:
 * <ul>
 *     <li>A suit (Clubs, Diamonds, Hearts or Spades).</li>
 *     <li>A rank (Ace through King).</li>
 *     <li>A numerical value used during the game.</li>
 *     <li>The path of its corresponding image resource.</li>
 * </ul>
 *
 * <p>Card objects are immutable once created.
 *
 * @author Jose Manuel Cardona Gil
 * @version 1.0
 */
public class Card {

    /**
     * Enumeration of the four suits in a standard deck.
     */
    public enum Suit {
        CLUBS,
        DIAMONDS,
        HEARTS,
        SPADES
    }

    /**
     * Enumeration of all possible card ranks.
     */
    public enum Rank {
        ACE,
        TWO,
        THREE,
        FOUR,
        FIVE,
        SIX,
        SEVEN,
        EIGHT,
        NINE,
        TEN,
        JACK,
        QUEEN,
        KING
    }

    /**
     * The suit of the card.
     */
    private final Suit suit;

    /**
     * The rank of the card.
     */
    private final Rank rank;

    /**
     * The image path associated with this card.
     */
    private final String imagePath;

    /**
     * Creates a new card.
     *
     * @param suit the suit of the card
     * @param rank the rank of the card
     * @param imagePath the image resource path
     */
    public Card(Suit suit, Rank rank, String imagePath) {
        this.suit = suit;
        this.rank = rank;
        this.imagePath = imagePath;
    }

    /**
     * Returns the suit of the card.
     *
     * @return the card suit
     */
    public Suit getSuit() {
        return suit;
    }

    /**
     * Returns the rank of the card.
     *
     * @return the card rank
     */
    public Rank getRank() {
        return rank;
    }

    /**
     * Returns the image path associated with this card.
     *
     * @return the image path
     */
    public String getImagePath() {
        return imagePath;
    }

    /**
     * Returns the numerical value of the card according to the
     * Cincuentazo rules.
     *
     * <ul>
     *     <li>Ace = 1</li>
     *     <li>Two to Ten = Their numeric value</li>
     *     <li>Jack, Queen and King = 10</li>
     * </ul>
     *
     * @return the numerical value of the card
     */
    public int getValue() {

        return switch (rank) {

            case ACE -> 1 | 10;

            case TWO -> 2;

            case THREE -> 3;

            case FOUR -> 4;

            case FIVE -> 5;

            case SIX -> 6;

            case SEVEN -> 7;

            case EIGHT -> 8;

            case NINE -> 0;

            case TEN-> 10;

            case JACK, QUEEN, KING -> -10;
        };
    }

    /**
     * Returns a human-readable representation of the card.
     *
     * @return a string containing the rank and suit
     */
    @Override
    public String toString() {
        return rank + " of " + suit;
    }

    /**
     * Compares this card with another object.
     *
     * @param object the object to compare
     * @return {@code true} if both cards have the same suit and rank
     */
    @Override
    public boolean equals(Object object) {

        if (this == object) {
            return true;
        }

        if (!(object instanceof Card card)) {
            return false;
        }

        return suit == card.suit && rank == card.rank;
    }

    /**
     * Returns the hash code of the card.
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(suit, rank);
    }

    /**
     * Checks if this card can be played.
     *
     * @param currentSum current accumulated value
     * @return true if the card can be played
     */
    public boolean canBePlayed(int currentSum) {

        return currentSum + getValue() <= 50;
    }
}