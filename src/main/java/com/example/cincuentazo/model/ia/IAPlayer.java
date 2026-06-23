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

    private final Difficulty difficulty;
    private final Random random;

    public IAPlayer(Difficulty difficulty) {
        this.difficulty = difficulty;
        this.random = new Random();
    }

    /**
     * Decides the card to play based in the actual estate
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
     * EASY: random
     */
    private Card randomValidCard(List<Card> hand, int currentSum) {
        List<Card> valid = getValidCards(hand, currentSum);
        if (valid.isEmpty()) return null;
        return valid.get(random.nextInt(valid.size()));
    }

    /**
     * MEDIUM: basic
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
     * HARD
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
     * Filtra cartas válidas según reglas del juego
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
     * Basic Evaluation
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
     * advanced Evaluation
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
