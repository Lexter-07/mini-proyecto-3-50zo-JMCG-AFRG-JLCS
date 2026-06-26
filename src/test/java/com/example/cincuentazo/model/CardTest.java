package com.example.cincuentazo.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Unit tests for the {@link Card} class.
 * Verifies correct card creation, image path generation,
 * and textual representation.
 *
 * @author Jose Manuel Cardona Gil
 * @version 1.0
 */
class CardTest {

    /**
     * Verifies that a card is created with the expected
     * suit and rank values.
     */
    @Test
    void shouldCreateCardCorrectly() {

        Card card = new Card(Suit.HEARTS, 5);

        assertEquals(Suit.HEARTS, card.getSuit());
        assertEquals(5, card.getRank());
    }

    /**
     * Verifies that the image path generated for a card
     * follows the expected naming convention.
     */
    @Test
    void shouldGenerateCorrectImagePath() {

        Card card = new Card(Suit.SPADES, 1);

        assertEquals(
                "/com/example/cincuentazo/CARDS_PNG/SPADES-01.png",
                card.getImagePath()
        );
    }

    /**
     * Verifies that the textual representation of a card
     * matches the expected format.
     */
    @Test
    void shouldReturnCorrectToString() {

        Card card = new Card(Suit.CLUBS, 10);

        assertEquals("CLUBS-10", card.toString());
    }
}