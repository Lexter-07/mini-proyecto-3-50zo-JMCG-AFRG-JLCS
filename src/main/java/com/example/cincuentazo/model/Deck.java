package com.example.cincuentazo.model;

import com.example.cincuentazo.exceptions.EmptyDeckException;
import com.example.cincuentazo.model.intefaces.IDeck;

import java.util.Collections;
import java.util.List;
import java.util.Stack;

/**
 * Manages the draw deck pile using explicit Card objects.
 * Supports initialization, shuffling, and explicit drawing.
 * @author Jorge Luis Castro, Andres Felipe Rodríguez
 * @version 1.0
 */
public class Deck implements IDeck {

    /**
     * A LIFO structure that stores the remaining cards.
     * The removal and addition operations are performed on the top of the stack.
     */
    private final Stack<Card> drawPile;

    /**
     * Initializes an empty deck stack structure.
     */
    public Deck() {
        this.drawPile = new Stack<>();
    }

    /**
     * Generates a standard deck of 52 cards (4 suits, 13 ranks each).
     */
    public void generateDeck() {
        drawPile.clear();
        for (Suit suit : Suit.values()) {
            for (int rank = 1; rank <= 13; rank++) {
                drawPile.push(new Card(suit, rank));
            }
        }
    }

    /**
     * Shuffles the current cards remaining in the draw pile.
     */
    public void shuffleDeck() {
        Collections.shuffle(drawPile);
    }

    /**
     * Draws the top card from the deck pile.
     * @return the drawn Card object
     * @throws EmptyDeckException if no cards are available to draw
     */
    public Card drawCard() throws EmptyDeckException {
        if (drawPile.isEmpty()) {
            throw new EmptyDeckException("The draw pile is completely empty.");
        }
        return drawPile.pop();
    }

    /**
     * Restores the deck using discarded table cards during recycling.
     * @param recycledCards list of cards recovered from the table pile
     */
    public void recycleDiscardPile(List<Card> recycledCards) {
        Collections.shuffle(recycledCards);
        drawPile.addAll(recycledCards);
    }

    /**
     * Retrieves the total number of cards currently remaining in the draw pile.
     * This count represents the available stock of cards left for players to draw from
     * during the match.
     *
     * @return The integer number of items currently inside the draw pile collection.
     */
    public int getRemainingCount() {
        return drawPile.size();
    }
}