package com.example.cincuentazo.model;

/**
 * Pure mathematical utility class responsible for verifying card game values
 * and assessing structural movement validity under Cincuentazo regulations.
 * @author AndresF395
 * @version 1.0
 */
public class GameRules {

    public static final int MAX_SUM_THRESHOLD = 50;

    /**
     * Computes the point impact of a specific card given the current table sum.
     * Accounts for special dynamic conditions like the Ace (1 or 10).
     * * @param card the card to evaluate
     * @param currentSum the current cumulative total score on the table
     * @return the integer value (positive or negative) added to the table
     */
    public static int calculateCardValue(Card card, int currentSum) {
        int rank = card.getRank();

        // Rule: 9 adds 0
        if (rank == 9) {
            return 0;
        }
        // Rule: J, Q, K subtract 10
        if (rank >= 11 && rank <= 13) {
            return -10;
        }
        // Rule: Ace (1) adds 10 if it fits within threshold, otherwise adds 1
        if (rank == 1) {
            if (currentSum + 10 <= MAX_SUM_THRESHOLD) {
                return 10;
            }
            return 1;
        }
        // Rule: 2-8 and 10 add their nominal value
        return rank;
    }

    /**
     * Validates if playing a card complies with the primary boundary rule (<= 50).
     * * @param card the card intended to be played
     * @param currentSum the current sum before making the move
     * @return true if the move is legal, false otherwise
     */
    public static boolean isValidMove(Card card, int currentSum) {
        int outcome = currentSum + calculateCardValue(card, currentSum);
        return outcome <= MAX_SUM_THRESHOLD;
    }
}
