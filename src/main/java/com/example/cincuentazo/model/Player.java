package com.example.cincuentazo.model;

import com.example.cincuentazo.model.intefaces.IPlayer;

import java.util.ArrayList;
import java.util.List;

/**
 * Domain entity model representing a player participating in the match.
 * Holds their hand data, identification metrics, and active gameplay standing.
 * * @author Senior MVC Architect
 * @version 1.0
 */
public class Player implements IPlayer {

    private final String name;
    private final boolean isHuman;
    private final List<Card> hand;
    private boolean eliminated;

    public Player(String name, boolean isHuman) {
        this.name = name;
        this.isHuman = isHuman;
        this.hand = new ArrayList<>();
        this.eliminated = false;
    }

    public String getName() {
        return name;
    }

    public boolean isHuman() {
        return isHuman;
    }

    public List<Card> getHand() {
        return hand;
    }

    public boolean isEliminated() {
        return eliminated;
    }

    public void setEliminated(boolean eliminated) {
        this.eliminated = eliminated;
    }

    public void addCardToHand(Card card) {
        if (hand.size() < 4) {
            hand.add(card);
        }
    }

    public void removeCardFromHand(Card card) {
        hand.remove(card);
    }
}
