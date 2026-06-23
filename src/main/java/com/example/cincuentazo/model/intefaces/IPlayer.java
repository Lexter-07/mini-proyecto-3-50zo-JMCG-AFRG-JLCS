package com.example.cincuentazo.model.intefaces;

import com.example.cincuentazo.model.Card;

import java.util.List;

/**
 *
 */

public interface IPlayer {

    String getName();
    boolean isHuman();
    List<Card> getHand();
    boolean isEliminated();
    void addCardToHand(Card card);
    void removeCardFromHand(Card card);
}
