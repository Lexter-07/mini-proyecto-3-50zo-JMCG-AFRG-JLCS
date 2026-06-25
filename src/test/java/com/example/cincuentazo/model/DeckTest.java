package com.example.cincuentazo.model;

import com.example.cincuentazo.exceptions.EmptyDeckException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeckTest {

    @Test
    void shouldGenerate52Cards() {

        Deck deck = new Deck();

        deck.generateDeck();

        assertEquals(52, deck.getRemainingCount());
    }

    @Test
    void shouldDrawOneCard() throws EmptyDeckException {

        Deck deck = new Deck();

        deck.generateDeck();

        Card card = deck.drawCard();

        assertNotNull(card);
        assertEquals(51, deck.getRemainingCount());
    }

    @Test
    void shouldShuffleWithoutChangingSize() {

        Deck deck = new Deck();

        deck.generateDeck();

        deck.shuffleDeck();

        assertEquals(52, deck.getRemainingCount());
    }

    @Test
    void shouldThrowExceptionWhenDeckEmpty() {

        Deck deck = new Deck();

        assertThrows(
                EmptyDeckException.class,
                deck::drawCard
        );
    }
}