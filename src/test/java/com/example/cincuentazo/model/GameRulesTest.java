package com.example.cincuentazo.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameRulesTest {

    @Test
    void aceShouldBeWorthTenWhenPossible() {

        Card ace = new Card(Suit.HEARTS, 1);

        assertEquals(
                10,
                GameRules.calculateCardValue(ace, 20)
        );
    }

    @Test
    void aceShouldBeWorthOneWhenNearFifty() {

        Card ace = new Card(Suit.HEARTS, 1);

        assertEquals(
                1,
                GameRules.calculateCardValue(ace, 45)
        );
    }

    @Test
    void nineShouldBeWorthZero() {

        Card nine = new Card(Suit.HEARTS, 9);

        assertEquals(
                0,
                GameRules.calculateCardValue(nine, 20)
        );
    }

    @Test
    void kingShouldSubtractTen() {

        Card king = new Card(Suit.SPADES, 13);

        assertEquals(
                -10,
                GameRules.calculateCardValue(king, 30)
        );
    }

    @Test
    void shouldValidateLegalMove() {

        Card five = new Card(Suit.CLUBS, 5);

        assertTrue(
                GameRules.isValidMove(five, 40)
        );
    }

    @Test
    void shouldRejectIllegalMove() {

        Card ten = new Card(Suit.CLUBS, 10);

        assertFalse(
                GameRules.isValidMove(ten, 45)
        );
    }
}