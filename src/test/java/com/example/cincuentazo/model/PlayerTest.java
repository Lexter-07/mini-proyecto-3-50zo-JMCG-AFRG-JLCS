package com.example.cincuentazo.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Player} class. <p>
 *
 * Verifies player creation, hand management,
 * and card capacity restrictions.
 *
 * @author Jose Manuel Cardona Gil
 * @version 1.0
 */
class PlayerTest {

    /**
     * Verifies that a human player is created
     * with the expected initial state.
     */
    @Test
    void shouldCreateHumanPlayer() {

        Player player = new Player("Jose", true);

        assertEquals("Jose", player.getName());
        assertTrue(player.isHuman());
        assertFalse(player.isEliminated());
    }

    /**
     * Verifies that adding a card correctly
     * stores it in the player's hand.
     */
    @Test
    void shouldAddCardToHand() {

        Player player = new Player("Jose", true);

        player.addCardToHand(new Card(Suit.HEARTS, 5));

        assertEquals(1, player.getHand().size());
    }

    /**
     * Verifies that removing a card correctly
     * updates the player's hand.
     */
    @Test
    void shouldRemoveCardFromHand() {

        Player player = new Player("Jose", true);

        Card card = new Card(Suit.HEARTS, 5);

        player.addCardToHand(card);
        player.removeCardFromHand(card);

        assertTrue(player.getHand().isEmpty());
    }

    /**
     * Verifies that the player's hand never
     * exceeds the maximum capacity of four cards.
     */
    @Test
    void shouldNotAllowMoreThanFourCards() {

        Player player = new Player("Jose", true);

        for (int i = 0; i < 6; i++) {
            player.addCardToHand(new Card(Suit.HEARTS, i + 1));
        }

        assertEquals(4, player.getHand().size());
    }
}