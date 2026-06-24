package com.example.cincuentazo.controller;

import com.example.cincuentazo.exceptions.InvalidMoveException;
import com.example.cincuentazo.model.Card;
import com.example.cincuentazo.model.GameModel;
import com.example.cincuentazo.model.Player;
import com.example.cincuentazo.config.GameSettings;
import com.example.cincuentazo.model.threads.TimeThread;
import com.example.cincuentazo.model.Translation;

import com.example.cincuentazo.view.Path;
import com.example.cincuentazo.view.SceneManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Controller strictly mapped to the custom user UI structure.
 * Relies on external services (Translation, TimeThread) for higher cohesion.
 * @author Andrés Felipe Rodríguez García
 * @version 2.1
 */
public class GameController {

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

    @FXML private Button playCardButton;

    @FXML private HBox turnBadge;
    @FXML private Label turnLabel;

    @FXML private Button pauseButton;

    @FXML private VBox pauseOverlay;

    // === CONTENEDORES DE LAS MÁQUINAS PARA OCULTARLAS ===
    @FXML private VBox machine1Box;
    @FXML private VBox machine2Box;
    @FXML private VBox machine3Box;

    private GameModel gameModel;
    private int selectedHandIndex = -1;
    private List<String> configurationNames;

    // Concurrency controls
    private volatile boolean isGameRunning = false;
    private TimeThread timeThread; // <-- Externalizado
    private Thread machineThread;
    private boolean gamePaused = false;

    @FXML
    public void initialize() {
        setupCardInteractionEvents();

        pauseButton.setOnAction(event -> togglePauseMenu());

        int machineCount = GameSettings.getMachineCount();

        // Ocultar IAs no utilizadas visualmente
        if (machine1Box != null) { machine1Box.setVisible(machineCount >= 1); machine1Box.setManaged(machineCount >= 1); }
        if (machine2Box != null) { machine2Box.setVisible(machineCount >= 2); machine2Box.setManaged(machineCount >= 2); }
        if (machine3Box != null) { machine3Box.setVisible(machineCount >= 3); machine3Box.setManaged(machineCount >= 3); }

        configurationNames = new ArrayList<>();
        configurationNames.add("Tú (Humano)");
        for (int i = 1; i <= machineCount; i++) {
            configurationNames.add("Máquina " + i);
        }

        gameModel = new GameModel(configurationNames);
        gameModel.startNewGame();

        if (roundLabel != null) roundLabel.setText("1");

        isGameRunning = true;

        // Iniciar Hilo de Tiempo Externo
        timeThread = new TimeThread(timeLabel);
        timeThread.start();


        startMachineThread();
        refreshGraphicInterface();

        Platform.runLater(() -> {
            if (tablePane != null && tablePane.getScene() != null) {
                tablePane.getScene().addEventHandler(KeyEvent.KEY_PRESSED, this::handleSystemKeyboardStroke);
                tablePane.requestFocus();
            }
        });
    }

    private void setupCardInteractionEvents() {
        if (cardButton1 != null) cardButton1.setOnMouseClicked(event -> highlightSelectedCard(0));
        if (cardButton2 != null) cardButton2.setOnMouseClicked(event -> highlightSelectedCard(1));
        if (cardButton3 != null) cardButton3.setOnMouseClicked(event -> highlightSelectedCard(2));
        if (cardButton4 != null) cardButton4.setOnMouseClicked(event -> highlightSelectedCard(3));

        if (playCardButton != null) {
            playCardButton.setOnAction(event -> processHumanMoveConfirmation());
        }
    }

    private void highlightSelectedCard(int handIndex) {
        selectedHandIndex = handIndex;
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
        if (event.getCode() == KeyCode.SPACE) {
            event.consume();
            processHumanMoveConfirmation();
        }
    }

    private void processHumanMoveConfirmation() {
        Player human = gameModel.getPlayers().get(0);
        if (gameModel.getTurnSystem().getCurrentPlayer() != human) return;
        if (selectedHandIndex < 0 || selectedHandIndex >= human.getHand().size()) {
            displayStatusNotification("Falta Selección", "Selecciona una carta haciendo clic antes de jugar.");
            return;
        }

        Card choice = human.getHand().get(selectedHandIndex);
        try {
            gameModel.playTurnAction(human, choice);

            // USO DE CLASE EXTERNA DE TRADUCCIÓN
            if (historyList != null) {
                historyList.getItems().add(0, "Jugaste: " + Translation.generateSpanishCardName(choice) + " | Suma: " + gameModel.getTableSum());
            }

            selectedHandIndex = -1;
            gameModel.getTurnSystem().advanceTurn();
            refreshGraphicInterface();
            checkMatchTermination();

        } catch (InvalidMoveException e) {
            displayStatusNotification("Jugada Ilegal", "Esta carta supera los 50 puntos. Intenta con otra.");
        }
    }

    private void startMachineThread() {
        machineThread = new Thread(() -> {
            while (isGameRunning) {
                try {
                    Thread.sleep(1000);
                    if (!isGameRunning) break;

                    Player current = gameModel.getTurnSystem().getCurrentPlayer();
                    if (!current.isHuman() && !current.isEliminated()) {

                        Thread.sleep(2000 + (long)(Math.random() * 2000));
                        if (!isGameRunning) break;

                        Platform.runLater(() -> handleAutomatedTurnPass(current));
                        Thread.sleep(1000 + (long)(Math.random() * 1000));
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        machineThread.setDaemon(true);
        machineThread.start();
    }

    private void handleAutomatedTurnPass(Player current) {
        if (!isGameRunning || gameModel.getTurnSystem().checkVictoryCondition()) return;

        Card safeChoice = null;
        for (Card card : current.getHand()) {
            if (com.example.cincuentazo.model.GameRules.isValidMove(card, gameModel.getTableSum())) {
                safeChoice = card;
                break;
            }
        }

        if (safeChoice != null) {
            try {
                gameModel.playTurnAction(current, safeChoice);
                if (historyList != null) {
                    // USO DE CLASE EXTERNA DE TRADUCCIÓN
                    historyList.getItems().add(0, current.getName() + " jugó " + Translation.generateSpanishCardName(safeChoice));
                }
            } catch (InvalidMoveException ignored) {}
        } else {
            gameModel.eliminatePlayer(current);
            if (historyList != null) {
                historyList.getItems().add(0, current.getName() + " ELIMINADO.");
            }
        }

        gameModel.getTurnSystem().advanceTurn();
        refreshGraphicInterface();
        checkMatchTermination();
    }

    private void refreshGraphicInterface() {
        if (isGameRunning) {
            Player currentTurnPlayer = gameModel.getTurnSystem().getCurrentPlayer();
            if (currentTurnPlayer.isHuman() && !currentTurnPlayer.isEliminated()) {
                boolean humanHasValidMoves = false;
                for (Card c : currentTurnPlayer.getHand()) {
                    if (com.example.cincuentazo.model.GameRules.isValidMove(c, gameModel.getTableSum())) {
                        humanHasValidMoves = true;
                        break;
                    }
                }

                if (!humanHasValidMoves) {
                    Platform.runLater(() -> {
                        displayStatusNotification("¡Eliminado!", "No tienes cartas válidas. Quedas eliminado.");
                        gameModel.eliminatePlayer(currentTurnPlayer);
                        if (historyList != null) historyList.getItems().add(0, "Tú (Humano) fuiste ELIMINADO.");
                        gameModel.getTurnSystem().advanceTurn();
                        refreshGraphicInterface();
                        checkMatchTermination();
                    });
                    return;
                }
            }
        }

        Player human = gameModel.getPlayers().get(0);
        List<ImageView> views = Arrays.asList(cardImage1, cardImage2, cardImage3, cardImage4);
        List<Button> buttons = Arrays.asList(cardButton1, cardButton2, cardButton3, cardButton4);

        for (int i = 0; i < 4; i++) {
            if (i < human.getHand().size()) {
                Card card = human.getHand().get(i);
                try {
                    InputStream is = getClass().getResourceAsStream(card.getImagePath());
                    if (is != null) views.get(i).setImage(new Image(is));
                    else views.get(i).setImage(null);
                } catch (Exception e) { views.get(i).setImage(null); }
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

        if (sumLabel != null) sumLabel.setText(String.valueOf(gameModel.getTableSum()));
        if (deckCountLabel != null) deckCountLabel.setText(String.valueOf(gameModel.getDeck().getRemainingCount()));
        updateTurnIndicator();
    }

    private void checkMatchTermination() {
        if (gameModel.getTurnSystem().checkVictoryCondition() && isGameRunning) {
            isGameRunning = false;

            // Detener hilos usando referencias seguras
            if (timeThread != null) timeThread.stopTimer();
            if (machineThread != null) machineThread.interrupt();

            Player winner = gameModel.getPlayers().stream().filter(p -> !p.isEliminated()).findFirst().orElse(null);
            String winnerName = (winner != null) ? winner.getName() : "Nadie";

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Fin del Juego - Cincuentazo");
            alert.setHeaderText("¡Juego Terminado!");
            alert.setContentText("El ganador es: " + winnerName + ".\n¿Qué deseas hacer ahora?");

            ButtonType btnPlayAgain = new ButtonType("Jugar otra ronda");
            ButtonType btnExit = new ButtonType("Terminar juego", ButtonBar.ButtonData.CANCEL_CLOSE);

            alert.getButtonTypes().setAll(btnPlayAgain, btnExit);

            alert.showAndWait().ifPresent(type -> {
                if (type == btnPlayAgain) {
                    restartGame();
                } else {
                    Platform.exit();
                    System.exit(0);
                }
            });
        }
    }

    private void restartGame() {
        gameModel.startNewGame();
        if (historyList != null) historyList.getItems().clear();
        selectedHandIndex = -1;
        isGameRunning = true;

        // Re-iniciar Hilos
        timeThread = new TimeThread(timeLabel);
        timeThread.start();
        startMachineThread();

        refreshGraphicInterface();
    }

    private void displayStatusNotification(String title, String summary) {
        Alert notification = new Alert(Alert.AlertType.INFORMATION);
        notification.setTitle(title);
        notification.setHeaderText(null);
        notification.setContentText(summary);
        notification.showAndWait();
    }

    /**
     * Updates the visual turn indicator and enables/disables
     * the human controls depending on whose turn it is.
     */
    private void updateTurnIndicator() {

        Player currentPlayer = gameModel.getTurnSystem().getCurrentPlayer();

        boolean humanTurn = currentPlayer.isHuman();

        // Badge
        turnBadge.setOpacity(humanTurn ? 1.0 : 0.30);

        // Text
        if (humanTurn) {
            turnLabel.setText("★ Tu turno");
        } else {
            turnLabel.setText("★ " + currentPlayer.getName());
        }

        // Main play button
        playCardButton.setDisable(!humanTurn);

        // Hand cards
        cardButton1.setDisable(!humanTurn);
        cardButton2.setDisable(!humanTurn);
        cardButton3.setDisable(!humanTurn);
        cardButton4.setDisable(!humanTurn);
    }

    private void togglePauseMenu() {

        gamePaused = !gamePaused;

        pauseOverlay.setVisible(gamePaused);
        pauseOverlay.setManaged(gamePaused);

        if (timeThread != null) {

            if (gamePaused) {
                timeThread.pauseTimer();
            } else {
                timeThread.resumeTimer();
            }
        }

        playCardButton.setDisable(gamePaused);

        cardButton1.setDisable(gamePaused);
        cardButton2.setDisable(gamePaused);
        cardButton3.setDisable(gamePaused);
        cardButton4.setDisable(gamePaused);
    }

    @FXML
    private void handleResumeGame() {

        gamePaused = false;

        pauseOverlay.setVisible(false);
        pauseOverlay.setManaged(false);

        if (timeThread != null) {
            timeThread.resumeTimer();
        }

        refreshGraphicInterface();
    }

    @FXML
    private void handleBackToMenu() {

        try {

            SceneManager.changeScene(Path.MenuView);

        } catch (IOException e) {

            displayStatusNotification(
                    "Error",
                    "Could not return to the main menu."
            );

            e.printStackTrace();
        }
    }

    @FXML
    private void handleNewGame() {

        gameModel = new GameModel(configurationNames);
        gameModel.startNewGame();

        selectedHandIndex = -1;

        if (historyList != null) {
            historyList.getItems().clear();
        }

        gamePaused = false;

        pauseOverlay.setVisible(false);
        pauseOverlay.setManaged(false);

        refreshGraphicInterface();
    }
}
