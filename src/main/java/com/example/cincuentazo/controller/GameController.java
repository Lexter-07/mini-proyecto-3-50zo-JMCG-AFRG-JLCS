package com.example.cincuentazo.controller;

import com.example.cincuentazo.exceptions.InvalidMoveException;
import com.example.cincuentazo.model.Card;
import com.example.cincuentazo.model.GameModel;
import com.example.cincuentazo.model.Player;
import com.example.cincuentazo.model.ia.IAPlayer;
import com.example.cincuentazo.config.GameSettings;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

/**
 * Controller strictly mapped to the custom user UI structure.
 * Automatically handles events without modifying the original FXML.
 */
public class GameController {

    // ==== INYECCIONES EXACTAS DE TU FXML ====
    @FXML private AnchorPane tablePane;

    @FXML private Label sumLabel;
    @FXML private Label roundLabel;
    @FXML private Label timeLabel;
    @FXML private Label deckCountLabel;
    @FXML private ListView<String> historyList;

    @FXML private StackPane playedCardContainer;
    @FXML private ImageView playedCardImage;

    @FXML private Button cardButton1;
    @FXML private Button cardButton2;
    @FXML private Button cardButton3;
    @FXML private Button cardButton4;

    @FXML private ImageView cardImage1;
    @FXML private ImageView cardImage2;
    @FXML private ImageView cardImage3;
    @FXML private ImageView cardImage4;

    @FXML
    private VBox machine1Box;

    @FXML
    private VBox machine2Box;

    @FXML
    private VBox machine3Box;

    @FXML private Button playCardButton; // El gran botón de jugar carta en tu FXML

    private GameModel gameModel;
    private int selectedHandIndex = -1;
    private IAPlayer ia = new IAPlayer(IAPlayer.Difficulty.HARD);
    private volatile boolean humanPlayed = false;
    private List<IAPlayer> aiPlayers;

    @FXML
    public void initialize() {

        System.out.println("[TEST] Inicializando GameController...");

        configureVisibleMachines();

        setupCardInteractionEvents();

        List<String> configurationNames = Arrays.asList(
                "You (Human)",
                "Máquina 1",
                "Máquina 2",
                "Máquina 3"
        );

        gameModel = new GameModel(configurationNames);
        gameModel.startNewGame();

        if (timeLabel != null) {
            timeLabel.setText("00:00");
        }

        if (roundLabel != null) {
            roundLabel.setText("1");
        }

        refreshGraphicInterface();

        Platform.runLater(() -> {

            if (tablePane != null && tablePane.getScene() != null) {

                tablePane.getScene().addEventHandler(
                        KeyEvent.KEY_PRESSED,
                        this::handleSystemKeyboardStroke
                );

                tablePane.requestFocus();
            }
        });

        System.out.println("[TEST] GameController vinculado correctamente a UI sin errores.");
    }

    private void configureVisibleMachines() {

        int machines = GameSettings.getMachineCount();

        machine1Box.setVisible(false);
        machine1Box.setManaged(false);

        machine2Box.setVisible(false);
        machine2Box.setManaged(false);

        machine3Box.setVisible(false);
        machine3Box.setManaged(false);

        if (machines >= 1) {
            machine1Box.setVisible(true);
            machine1Box.setManaged(true);
        }

        if (machines >= 2) {
            machine2Box.setVisible(true);
            machine2Box.setManaged(true);
        }

        if (machines >= 3) {
            machine3Box.setVisible(true);
            machine3Box.setManaged(true);
        }
    }

    private void setupCardInteractionEvents() {
        System.out.println("[TEST] Vinculando botones...");
        if (cardButton1 != null) cardButton1.setOnMouseClicked(event -> highlightSelectedCard(0));
        if (cardButton2 != null) cardButton2.setOnMouseClicked(event -> highlightSelectedCard(1));
        if (cardButton3 != null) cardButton3.setOnMouseClicked(event -> highlightSelectedCard(2));
        if (cardButton4 != null) cardButton4.setOnMouseClicked(event -> highlightSelectedCard(3));

        // Vincular tu botón principal del FXML
        if (playCardButton != null) {
            playCardButton.setOnAction(event -> processHumanMoveConfirmation());
        }
    }

    private void highlightSelectedCard(int handIndex) {
        selectedHandIndex = handIndex;
        System.out.println("[TEST] Carta seleccionada en el índice: " + handIndex);

        List<Button> buttons = Arrays.asList(cardButton1, cardButton2, cardButton3, cardButton4);
        for (int i = 0; i < buttons.size(); i++) {
            if (buttons.get(i) != null) {
                buttons.get(i).getStyleClass().remove("card-selected");
                if (i == handIndex) {
                    buttons.get(i).getStyleClass().add("card-selected");
                }
            }
        }
    }

    private void handleSystemKeyboardStroke(KeyEvent event) {
        KeyCode code = event.getCode();
        if (code == KeyCode.SPACE) {
            event.consume();
            processHumanMoveConfirmation();
        }
    }

    private void processHumanMoveConfirmation() {
        Player human = gameModel.getPlayers().get(0);
        if (selectedHandIndex < 0 || selectedHandIndex >= human.getHand().size()) {
            displayStatusNotification("Falta Selección", "Selecciona una carta haciendo clic antes de jugar.");
            return;
        }

        Card choice = human.getHand().get(selectedHandIndex);
        try {
            System.out.println("[TEST] Intento de jugar la carta: " + choice.toString());
            gameModel.playTurnAction(human, choice);

            if (historyList != null) {
                historyList.getItems().add(0, "Jugaste: " + choice + " | Suma: " + gameModel.getTableSum());
            }

            selectedHandIndex = -1;
            gameModel.getTurnSystem().advanceTurn();
            refreshGraphicInterface();

            // Simular turno de máquina simple
            handleAutomatedTurnPass();

        } catch (InvalidMoveException e) {
            System.out.println("[TEST ERROR] Movimiento inválido, supera 50.");
            displayStatusNotification("Jugada Ilegal", "Esta carta supera los 50 puntos. Intenta con otra.");
        }
    }

    private void handleAutomatedTurnPass() {
        Player current = gameModel.getTurnSystem().getCurrentPlayer();

        if (!current.isHuman() && !gameModel.getTurnSystem().checkVictoryCondition()) {

            System.out.println("[IA] Turno de: " + current.getName());

            Card choice = ia.chooseCard(current, gameModel.getTableSum());

            if (choice != null) {
                try {
                    gameModel.playTurnAction(current, choice);

                    if (historyList != null) {
                        historyList.getItems().add(0,
                                current.getName() + " jugó " + choice +
                                        " | Suma: " + gameModel.getTableSum());
                    }

                    gameModel.getTurnSystem().advanceTurn();

                } catch (InvalidMoveException ignored) {}
            } else {
                gameModel.eliminatePlayer(current);
                gameModel.getTurnSystem().advanceTurn();

                if (historyList != null) {
                    historyList.getItems().add(0, current.getName() + " fue ELIMINADO!");
                }
            }

            refreshGraphicInterface();
        }
    }

    private void refreshGraphicInterface() {
        Player human = gameModel.getPlayers().get(0);
        List<ImageView> views = Arrays.asList(cardImage1, cardImage2, cardImage3, cardImage4);
        List<Button> buttons = Arrays.asList(cardButton1, cardButton2, cardButton3, cardButton4);

        // Actualizar cartas con CARGA SEGURA para evitar NullPointerException
        for (int i = 0; i < 4; i++) {
            if (i < human.getHand().size()) {
                Card card = human.getHand().get(i);
                try {
                    InputStream is = getClass().getResourceAsStream(card.getImagePath());
                    if (is != null) {
                        views.get(i).setImage(new Image(is));
                    } else {
                        System.err.println("[TEST ALERTA] Falta imagen física en tu PC: " + card.getImagePath());
                        views.get(i).setImage(null);
                    }
                } catch (Exception e) {
                    views.get(i).setImage(null);
                }
                if (buttons.get(i) != null) buttons.get(i).setVisible(true);
            } else {
                if (views.get(i) != null) views.get(i).setImage(null);
                if (buttons.get(i) != null) buttons.get(i).setVisible(false);
            }
            if (buttons.get(i) != null) buttons.get(i).getStyleClass().remove("card-selected");
        }

        Card activeTop = gameModel.getTopTableCard();
        if (activeTop != null && playedCardImage != null) {
            try {
                InputStream is = getClass().getResourceAsStream(activeTop.getImagePath());
                if (is != null) playedCardImage.setImage(new Image(is));
            } catch (Exception e) { playedCardImage.setImage(null); }
        }

        // Actualizar datos
        if (sumLabel != null) sumLabel.setText(String.valueOf(gameModel.getTableSum()));
        if (deckCountLabel != null) deckCountLabel.setText(String.valueOf(gameModel.getDeck().getRemainingCount()));
    }

    private void displayStatusNotification(String title, String summary) {
        Alert notification = new Alert(Alert.AlertType.INFORMATION);
        notification.setTitle(title);
        notification.setHeaderText(null);
        notification.setContentText(summary);
        notification.showAndWait();
    }
}
