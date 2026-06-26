package com.example.cincuentazo.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link GameRules} class. <p>
 *
 * Verifies the calculation of card values according to
 * the game rules and the validation of legal and illegal
 * player moves.
 *
 * @author Jose Manuel Cardona Gil
 * @version 1.0
 */
class GameRulesTest {

    /**
     * Verifies that an Ace is valued at ten points
     * whenever doing so does not exceed the maximum allowed table value.
     */
    @Test
    void aceShouldBeWorthTenWhenPossible() {

        Card ace = new Card(Suit.HEARTS, 1);

        assertEquals(
                10,
                GameRules.calculateCardValue(ace, 20)
        );
    }

    /**
     * Verifies that an Ace is valued at one point
     * when assigning ten points would exceed the table limit.
     */
    @Test
    void aceShouldBeWorthOneWhenNearFifty() {

        Card ace = new Card(Suit.HEARTS, 1);

        assertEquals(
                1,
                GameRules.calculateCardValue(ace, 45)
        );
    }

    /**
     * Verifies that a Nine contributes zero points
     * according to the game rules.
     */
    @Test
    void nineShouldBeWorthZero() {

        Card nine = new Card(Suit.HEARTS, 9);

        assertEquals(
                0,
                GameRules.calculateCardValue(nine, 20)
        );
    }

    /**
     * Verifies that a King subtracts ten points
     * from the current table value.
     */
    @Test
    void kingShouldSubtractTen() {

        Card king = new Card(Suit.SPADES, 13);

        assertEquals(
                -10,
                GameRules.calculateCardValue(king, 30)
        );
    }

    /**
     * Verifies that a move is considered valid
     * when the resulting table value does not exceed the maximum allowed limit.
     */
    @Test
    void shouldValidateLegalMove() {

        Card five = new Card(Suit.CLUBS, 5);

        assertTrue(
                GameRules.isValidMove(five, 40)
        );
    }

    /**
     * Verifies that a move is rejected when
     * playing the selected card would cause the table value to exceed fifty.
     */
    @Test
    void shouldRejectIllegalMove() {

        Card ten = new Card(Suit.CLUBS, 10);

        assertFalse(
                GameRules.isValidMove(ten, 45)
        );
    }
}