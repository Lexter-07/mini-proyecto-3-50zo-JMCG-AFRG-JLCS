package com.example.cincuentazo.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardTest {

    @Test
    void shouldCreateCardCorrectly() {

        Card card = new Card(Suit.HEARTS, 5);

        assertEquals(Suit.HEARTS, card.getSuit());
        assertEquals(5, card.getRank());
    }

    @Test
    void shouldGenerateCorrectImagePath() {

        Card card = new Card(Suit.SPADES, 1);

        assertEquals(
                "/com/example/cincuentazo/CARDS_PNG/SPADES-01.png",
                card.getImagePath()
        );
    }

    @Test
    void shouldReturnCorrectToString() {

        Card card = new Card(Suit.CLUBS, 10);

        assertEquals("CLUBS-10", card.toString());
    }
}