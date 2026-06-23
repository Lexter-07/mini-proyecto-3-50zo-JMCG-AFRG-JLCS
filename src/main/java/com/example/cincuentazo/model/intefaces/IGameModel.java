package com.example.cincuentazo.model.intefaces;

import com.example.cincuentazo.model.Card;
import com.example.cincuentazo.model.Player;

public interface IGameModel {

    void startNewGame();
    void playTurnAction(Player player, Card card);
    void eliminatePlayer(Player player);

}
