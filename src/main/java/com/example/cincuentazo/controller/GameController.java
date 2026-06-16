package com.example.cincuentazo.controller;

import com.example.cincuentazo.model.Deck;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

public class GameController {

    @FXML
    private Button cardButton1;

    @FXML
    private Button cardButton2;

    @FXML
    private Button cardButton3;

    @FXML
    private Button cardButton4;

    @FXML
    private ImageView cardImage1;

    @FXML
    private ImageView cardImage2;

    @FXML
    private ImageView cardImage3;

    @FXML
    private ImageView cardImage4;

    @FXML
    private Label deckCountLabel;

    @FXML
    private ListView<?> historyList;

    @FXML
    private Button playCardButton;

    @FXML
    private Button playedCardButton;

    @FXML
    private StackPane playedCardContainer;

    @FXML
    private ImageView playedCardImage;

    @FXML
    private Label roundLabel;

    @FXML
    private Button settingsButton;

    @FXML
    private Label sumLabel;

    @FXML
    private AnchorPane tablePane;

    @FXML
    private Label timeLabel;



    private Deck deck;

    @FXML
    public void initialize(){
        deck = new Deck();

        deck.generateDeck();
        deck.shuffleDeck();
    }

}
