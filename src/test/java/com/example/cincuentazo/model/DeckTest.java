package com.example.cincuentazo.model;

import com.example.cincuentazo.exceptions.EmptyDeckException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Deck} class. <p>
 *
 * Verifies deck generation, card extraction,
 * shuffling behavior, and exception handling
 * when attempting to draw from an empty deck.
 *
 * @author Jose Manuel Cardona Gil
 * @version 1.0
 */
class DeckTest {

    /**
     * Verifies that a newly generated deck
     * contains exactly fifty-two cards.
     */
    @Test
    void shouldGenerate52Cards() {

        Deck deck = new Deck();

        deck.generateDeck();

        assertEquals(52, deck.getRemainingCount());
    }

    /**
     * Verifies that drawing a card returns
     * a valid card and decreases the deck size by one.
     */
    @Test
    void shouldDrawOneCard() throws EmptyDeckException {

        Deck deck = new Deck();

        deck.generateDeck();

        Card card = deck.drawCard();

        assertNotNull(card);
        assertEquals(51, deck.getRemainingCount());
    }

    /**
     * Verifies that shuffling the deck
     * does not modify the total number of available cards.
     */
    @Test
    void shouldShuffleWithoutChangingSize() {

        Deck deck = new Deck();

        deck.generateDeck();

        deck.shuffleDeck();

        assertEquals(52, deck.getRemainingCount());
    }

    /**
     * Verifies that attempting to draw
     * from an empty deck throws the expected exception.
     */
    @Test
    void shouldThrowExceptionWhenDeckEmpty() {

        Deck deck = new Deck();

        assertThrows(
                EmptyDeckException.class,
                deck::drawCard
        );
    }
}