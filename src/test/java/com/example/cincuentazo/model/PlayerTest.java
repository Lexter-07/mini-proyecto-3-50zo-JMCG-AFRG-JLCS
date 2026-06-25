package com.example.cincuentazo.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    @Test
    void shouldCreateHumanPlayer() {

        Player player = new Player("Jose", true);

        assertEquals("Jose", player.getName());
        assertTrue(player.isHuman());
        assertFalse(player.isEliminated());
    }

    @Test
    void shouldAddCardToHand() {

        Player player = new Player("Jose", true);

        player.addCardToHand(new Card(Suit.HEARTS, 5));

        assertEquals(1, player.getHand().size());
    }

    @Test
    void shouldRemoveCardFromHand() {

        Player player = new Player("Jose", true);

        Card card = new Card(Suit.HEARTS, 5);

        player.addCardToHand(card);
        player.removeCardFromHand(card);

        assertTrue(player.getHand().isEmpty());
    }

    @Test
    void shouldNotAllowMoreThanFourCards() {

        Player player = new Player("Jose", true);

        for (int i = 0; i < 6; i++) {
            player.addCardToHand(new Card(Suit.HEARTS, i + 1));
        }

        assertEquals(4, player.getHand().size());
    }
}