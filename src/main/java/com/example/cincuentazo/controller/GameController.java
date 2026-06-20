package com.example.cincuentazo.controller;

import com.example.cincuentazo.model.Card;
import com.example.cincuentazo.model.Game;
import com.example.cincuentazo.model.Player;
import com.example.cincuentazo.model.exceptions.EmptyDeckException;
import com.example.cincuentazo.model.exceptions.InvalidCardPlayException;
import com.example.cincuentazo.model.threads.TurnThread;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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
    private ImageView playedCardImage;

    @FXML
    private Button playedCardButton;

    @FXML
    private Button playCardButton;

    @FXML
    private Label sumLabel;

    @FXML
    private Label deckCountLabel;

    @FXML
    private Label roundLabel;

    @FXML
    private Label timeLabel;

    @FXML
    private ListView<String> historyList;

    private Game game;

    private int selectedCardIndex = -1;

    @FXML
    public void initialize() {

        try {

            game = new Game();

            configureCardEvents();

            refreshUI();

        } catch (Exception exception) {

            exception.printStackTrace();
        }
    }

    /**
     * Configures all UI events.
     */
    private void configureCardEvents() {

        cardButton1.setOnAction(event -> selectCard(0));

        cardButton2.setOnAction(event -> selectCard(1));

        cardButton3.setOnAction(event -> selectCard(2));

        cardButton4.setOnAction(event -> selectCard(3));

        playCardButton.setOnAction(event -> {
            try {
                playSelectedCard();
            } catch (InvalidCardPlayException e) {
                throw new RuntimeException(e);
            } catch (EmptyDeckException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Selects a card from the player's hand.
     *
     * @param index selected card index
     */
    private void selectCard(int index) {

        Player humanPlayer = game.getPlayer(0);

        if (index >= humanPlayer.getHandSize()) {
            return;
        }

        selectedCardIndex = index;

        updateSelectionStyle();
    }

    /**
     * Updates selected card visual state.
     */
    private void updateSelectionStyle() {

        cardButton1.getStyleClass().remove("card-selected");
        cardButton2.getStyleClass().remove("card-selected");
        cardButton3.getStyleClass().remove("card-selected");
        cardButton4.getStyleClass().remove("card-selected");

        switch (selectedCardIndex) {

            case 0 -> cardButton1.getStyleClass().add("card-selected");

            case 1 -> cardButton2.getStyleClass().add("card-selected");

            case 2 -> cardButton3.getStyleClass().add("card-selected");

            case 3 -> cardButton4.getStyleClass().add("card-selected");
        }
    }

    /**
     * Plays the selected card.
     */
    private void playSelectedCard() throws InvalidCardPlayException, EmptyDeckException {

        if (selectedCardIndex == -1) {
            return;
        }

        Player humanPlayer = game.getPlayer(0);

        if (selectedCardIndex >= humanPlayer.getHandSize()) {
            return;
        }

        game.playCard(selectedCardIndex);

        selectedCardIndex = -1;

        refreshUI();

        startMachineTurns();
    }

    /**
     * Starts machine turns.
     */
    private void startMachineTurns() {

        TurnThread turnThread = new TurnThread(
                game,
                () -> Platform.runLater(this::refreshUI)
        );

        turnThread.setDaemon(true);

        turnThread.start();
    }

    /**
     * Updates the whole interface.
     */
    private void refreshUI() {

        updatePlayerHand();

        updatePlayedCard();

        updateLabels();

        updateHistory();

        updateSelectionStyle();
    }

    /**
     * Updates player's hand images.
     */
    private void updatePlayerHand() {

        Player humanPlayer = game.getPlayer(0);

        ImageView[] cardImages = {
                cardImage1,
                cardImage2,
                cardImage3,
                cardImage4
        };

        for (int i = 0; i < cardImages.length; i++) {

            if (i < humanPlayer.getHandSize()) {

                Card card = humanPlayer.getHand().get(i);

                try {

                    Image image = new Image(
                            getClass()
                                    .getResourceAsStream(
                                            card.getImagePath()
                                    )
                    );

                    cardImages[i].setImage(image);

                } catch (Exception exception) {

                    cardImages[i].setImage(null);

                    System.out.println(
                            "Image not found: "
                                    + card.getImagePath()
                    );
                }

            } else {

                cardImages[i].setImage(null);
            }
        }
    }

    /**
     * Updates the card currently played.
     */
    private void updatePlayedCard() {

        Card card = game.getLastPlayedCard();

        if (card == null) {

            playedCardImage.setImage(null);

            return;
        }

        try {

            Image image = new Image(
                    getClass()
                            .getResourceAsStream(
                                    card.getImagePath()
                            )
            );

            playedCardImage.setImage(image);

        } catch (Exception exception) {

            playedCardImage.setImage(null);

            System.out.println(
                    "Played card image not found: "
                            + card.getImagePath()
            );
        }
    }

    /**
     * Updates labels.
     */
    private void updateLabels() {

        sumLabel.setText(
                String.valueOf(
                        game.getCurrentSum()
                )
        );

        deckCountLabel.setText(
                String.valueOf(
                        game.getDeck().size()
                )
        );
    }

    /**
     * Updates history panel.
     */
    private void updateHistory() {

        historyList.getItems().setAll(
                game.getHistory()
        );

        if (!historyList.getItems().isEmpty()) {

            historyList.scrollTo(
                    historyList.getItems().size() - 1
            );
        }
    }
}