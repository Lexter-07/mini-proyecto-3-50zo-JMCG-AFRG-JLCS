package com.example.cincuentazo.model;

import com.example.cincuentazo.model.Card;

/**
 * Utility class dedicated to translating internal game entities
 * into readable Spanish strings for the User Interface.
 * * @author Andrés Felipe Rodríguez García
 * @version 1.0
 */
public class Translation {

    /**
     * Translates internally formatted card logic to a readable Spanish UI string.
     * * @param card The card to translate
     * @return Formatted string, e.g., "rey-corazones"
     */
    public static String generateSpanishCardName(Card card) {
        String rankStr;
        switch (card.getRank()) {
            case 1: rankStr = "as"; break;
            case 11: rankStr = "jota"; break;
            case 12: rankStr = "reina"; break;
            case 13: rankStr = "rey"; break;
            default: rankStr = String.valueOf(card.getRank()); break;
        }

        String suitStr;
        switch (card.getSuit()) {
            case HEARTS: suitStr = "corazones"; break;
            case SPADES: suitStr = "picas"; break;
            case CLUBS: suitStr = "tréboles"; break;
            case DIAMONDS: suitStr = "diamantes"; break;
            default: suitStr = "desconocido"; break;
        }
        return rankStr + "-" + suitStr;
    }
}