package com.example.cincuentazo.model;

import com.example.cincuentazo.model.exceptions.EmptyDeckException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents the deck used during a Cincuentazo match.
 *
 * <p>The deck is responsible for:
 * <ul>
 *     <li>Creating the 52 standard playing cards.</li>
 *     <li>Shuffling the cards.</li>
 *     <li>Drawing cards from the top of the deck.</li>
 *     <li>Keeping track of the remaining cards.</li>
 * </ul>
 *
 * The deck automatically generates all cards when it is created.
 *
 * @author Jose Manuel Cardona Gil
 * @version 1.0
 */
public class Deck {

    /**
     * List containing all remaining cards.
     */
    private final List<Card> cards;

    /**
     * Creates a new shuffled deck.
     */
    public Deck() {

        cards = new ArrayList<>();

        initializeDeck();

        shuffle();
    }

    /**
     * Generates the 52 cards of a standard deck.
     */
    private void initializeDeck() {

        for (Card.Suit suit : Card.Suit.values()) {

            for (Card.Rank rank : Card.Rank.values()) {

                cards.add(new Card(
                        suit,
                        rank,
                        buildImagePath(suit, rank)
                ));
            }
        }
    }

    /**
     * Builds the image path of a card.
     *
     * Example:
     * /com/example/cincuentazo/CARDS_PNG/HEARTS-01.png
     *
     * @param suit card suit
     * @param rank card rank
     * @return image path
     */
    private String buildImagePath(Card.Suit suit, Card.Rank rank) {

        return "/com/example/cincuentazo/CARDS_PNG/"
                + suit.name()
                + "-"
                + String.format("%02d", getRankNumber(rank))
                + ".png";
    }

    /**
     * Converts a rank into its numerical representation
     * used by the image names.
     *
     * ACE = 1
     * TWO = 2
     * ...
     * KING = 13
     *
     * @param rank card rank
     * @return numerical value from 1 to 13
     */
    private int getRankNumber(Card.Rank rank) {

        return switch (rank) {

            case ACE -> 1;

            case TWO -> 2;

            case THREE -> 3;

            case FOUR -> 4;

            case FIVE -> 5;

            case SIX -> 6;

            case SEVEN -> 7;

            case EIGHT -> 8;

            case NINE -> 9;

            case TEN -> 10;

            case JACK -> 11;

            case QUEEN -> 12;

            case KING -> 13;
        };
    }

    /**
     * Randomly shuffles the deck.
     */
    public void shuffle() {
        Collections.shuffle(cards);
    }

    /**
     * Removes and returns the top card of the deck.
     *
     * @return the first card of the deck
     * @throws IllegalStateException if the deck is empty
     */
    public Card drawCard() throws EmptyDeckException {

        if(cards.isEmpty()){

            throw new EmptyDeckException(
                    "The deck is empty."
            );
        }

        return cards.remove(0);
    }

    /**
     * Returns whether the deck still contains cards.
     *
     * @return true if the deck is empty
     */
    public boolean isEmpty() {
        return cards.isEmpty();
    }

    /**
     * Returns the number of remaining cards.
     *
     * @return remaining cards
     */
    public int size() {
        return cards.size();
    }

    /**
     * Returns a copy of the remaining cards.
     *
     * @return list of cards
     */
    public List<Card> getCards() {
        return new ArrayList<>(cards);
    }

    /**
     * Adds multiple cards to the deck.
     *
     * @param cards cards to add
     */
    public void addCards(List<Card> cards) {

        this.cards.addAll(cards);

        shuffle();
    }

    /**
     * Restores the deck to its original state.
     */
    public void reset() {

        cards.clear();

        initializeDeck();

        shuffle();
    }
}