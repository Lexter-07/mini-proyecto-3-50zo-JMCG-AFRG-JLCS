package com.example.cincuentazo.model.ia;

import com.example.cincuentazo.model.Card;
import com.example.cincuentazo.model.GameRules;
import com.example.cincuentazo.model.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Represents a machine player controlled by Artificial Intelligence (AI).
 * .
 * This class manages the automated player's decision-making based on
 * three difficulty levels, applying everything from random selections to
 * advanced heuristic evaluations depending on the current state of the game.
 *
 * @author Jorge Castro
 * @version 1.0
 */

public class IAPlayer {

    /**
     * Defines the difficulty levels available for the AI player.
     */
    public enum Difficulty {
        EASY, MEDIUM, HARD
    }

    /** Difficulty level assigned to this AI instance. */
    private final Difficulty difficulty;
    /** Random number generator used for probabilistic decisions. */
    private final Random random;


    /**
     * Creates an AI player configured with the specified difficulty level.
     *
     * @param difficulty desired AI difficulty
     */
    public IAPlayer(Difficulty difficulty) {
        this.difficulty = difficulty;
        this.random = new Random();
    }


    /**
     * Selects the card to be played according to the configured
     * difficulty strategy.
     *
     * @param player AI-controlled player
     * @param currentSum current accumulated value on the table
     * @return the selected card, or {@code null} if no valid move exists
     */
    public Card chooseCard(Player player, int currentSum) {
        List<Card> hand = player.getHand();

        switch (difficulty) {
            case EASY:
                return randomValidCard(hand, currentSum);

            case MEDIUM:
                return heuristicChoice(hand, currentSum);

            case HARD:
                return bestChoice(hand, currentSum);

            default:
                return randomValidCard(hand, currentSum);
        }
    }


    /**
     * Selects a random valid card from the player's hand. <p>
     * Used by the EASY difficulty level to simulate simple
     * and unpredictable behavior.
     *
     * @param hand player's current hand
     * @param currentSum current accumulated table value
     * @return a random valid card, or {@code null} if none exist
     */
    private Card randomValidCard(List<Card> hand, int currentSum) {
        List<Card> valid = getValidCards(hand, currentSum);
        if (valid.isEmpty()) return null;
        return valid.get(random.nextInt(valid.size()));
    }


    /**
     * Selects the highest-scoring valid card according to a basic heuristic evaluation. <p>
     * Used by the MEDIUM (Basic) difficulty level.
     *
     * @param hand player's current hand
     * @param currentSum current accumulated table value
     * @return the most favorable valid card, or {@code null} if none exist
     */
    private Card heuristicChoice(List<Card> hand, int currentSum) {
        List<Card> valid = getValidCards(hand, currentSum);
        if (valid.isEmpty()) return null;

        Card best = null;
        int bestScore = Integer.MIN_VALUE;

        for (Card c : valid) {
            int score = evaluateCard(c, currentSum);

            if (score > bestScore) {
                bestScore = score;
                best = c;
            }
        }

        return best;
    }


    /**
     * Selects the best possible card using an advanced evaluation strategy. <p>
     *
     * Used to by HARD difficulty level.
     * Exist a small probability of error is intentionally introduced
     * to avoid perfectly optimal behavior and create a more
     * realistic opponent.
     *
     * @param hand player's current hand
     * @param currentSum current accumulated table value
     * @return the optimal card choice, or {@code null} if no valid move exists
     */
    private Card bestChoice(List<Card> hand, int currentSum) {

        // 15% of error
        if (random.nextDouble() < 0.15) {
            return randomValidCard(hand, currentSum);
        }

        List<Card> valid = getValidCards(hand, currentSum);
        if (valid.isEmpty()) return null;

        Card best = null;
        int bestScore = Integer.MIN_VALUE;

        for (Card c : valid) {
            int score = evaluateCardAdvanced(c, currentSum);

            if (score > bestScore) {
                bestScore = score;
                best = c;
            }
        }

        return best;
    }


    /**
     * Filters the player's hand and retrieves only the cards
     * that can be legally played according to the game rules.
     *
     * @param hand player's current hand
     * @param currentSum current accumulated table value
     * @return list of valid playable cards
     */
    private List<Card> getValidCards(List<Card> hand, int currentSum) {
        List<Card> valid = new ArrayList<>();

        for (Card c : hand) {
            if (GameRules.isValidMove(c, currentSum)) {
                valid.add(c);
            }
        }

        return valid;
    }


    /**
     * Performs a basic heuristic evaluation of a card. <p>
     *
     * The score favors moves that bring the accumulated value
     * closer to fifty while avoiding excessively safe positions.
     *
     * @param card card being evaluated
     * @param currentSum current accumulated table value
     * @return heuristic score assigned to the card
     */
    private int evaluateCard(Card card, int currentSum) {
        int newSum = currentSum + GameRules.calculateCardValue(card, currentSum);

        int score = 0;

        // approach to 50
        score += newSum * 2;

        // bonus if leaves in dangerous zone
        if (newSum >= 47) score += 25;

        // penalize falling too low
        if (newSum < 30) score -= 10;

        return score;
    }


    /**
     * Performs an advanced heuristic evaluation of a card. <p>
     *
     * In addition to approaching the target value, this method
     * considers strategic factors such as forcing opponents into
     * dangerous situations and optimizing the use of special cards.
     *
     * @param card card being evaluated
     * @param currentSum current accumulated table value
     * @return advanced strategic score assigned to the card
     */
    private int evaluateCardAdvanced(Card card, int currentSum) {
        int value = GameRules.calculateCardValue(card, currentSum);
        int newSum = currentSum + value;

        int score = 0;
        score += newSum * 3;

        // leave the opponent in a dangerous zone
        if (newSum >= 47) score += 50;

        // avoid comfortable numbers for the opponent
        if (newSum >= 40 && newSum <= 45) score -= 10;

        // intelligent use of special cards
        if (card.getRank() >= 11 && card.getRank() <= 13) {
            if (currentSum > 40) score += 30; //to save
            else score -= 5;
        }

        if (card.getRank() == 9) {
            if (currentSum >= 45) score += 20;
            else score -= 5;
        }

        if (card.getRank() == 1) {
            if (value == 10) score += 15;
            else score += 5; // use defensive
        }

        return score;
    }
}
