package com.example.cincuentazo.controller;

import com.example.cincuentazo.exceptions.InvalidMoveException;
import com.example.cincuentazo.model.Card;
import com.example.cincuentazo.model.GameModel;
import com.example.cincuentazo.model.Player;
import com.example.cincuentazo.config.GameSettings;
import com.example.cincuentazo.model.threads.TimeThread;
import com.example.cincuentazo.model.Translation;
import com.example.cincuentazo.model.ia.IAPlayer;
import com.example.cincuentazo.model.threads.TurnThread;
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
 * Main gameplay coordinator and UI controller strictly mapped to the custom FX view layout.  <p>
 *
 * This class serves as the core supervisor for the Cincuentazo match, driving both visual updates
 *  * and operational turn advancements. To ensure single-responsibility and loose coupling, it delegates
 *  * specialized processes to external services, including localized translation engines
 *  * ({@link Translation}) and safe asynchronous concurrency wrappers ({@link TimeThread}, {@code TurnThread}). <p>
 *
 * Threading Architecture:
 *
 * <li><b>  JavaFX Application Thread:</b> Safely intercepts peripheral user gestures, keyboard bindings,
 * and drives layout/canvas component re-rendering routines.</li>
 * <li><b>  timeworker Daemon:</b> Handles standard stopwatch time tracking completely isolated from the UI frame cycle.</li>
 * <li><b>  Turn Loop Controller:</b> Manages AI heuristic reasoning pauses and sequentially coordinates active seating turn shifts.</li>
 * </ul>
 *
 * @author - Andrés Felipe Rodríguez García <p>
 *         - Jorge Luis Castro Scarpetta <p>
 *         - Jose Manuel Cardona Gil <p>
 * @version 2.3
 */
public class GameController {

    @FXML private AnchorPane tablePane;
    @FXML private Label sumLabel;
    @FXML private Label roundLabel;
    @FXML private Label timeLabel;
    @FXML private Label turnLabel;
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


    @FXML private Button pauseButton;

    @FXML private VBox pauseOverlay;

    // COMPUTER WRAPPER BOXES FOR DYNAMIC LAYOUT MANAGEMENT AND HIDE THEM
    @FXML private VBox machine1Box;
    @FXML private VBox machine2Box;
    @FXML private VBox machine3Box;

    private GameModel gameModel;
    private int selectedHandIndex = -1;
    private List<String> configurationNames;

    // Concurrency controls
    private volatile boolean isGameRunning = false;
    private boolean humanEliminationHandled = false;
    private boolean gameEndHandled = false;
    private boolean gamePaused = false;


    /** Background manager handling game-time calculations asynchronously. */
    private TimeThread timeThread; // <-- Externalizado
    /** Custom engine regulating game turn flows, preventing UI thread lockup. */
    private TurnThread turnThread;

    /** Pool containing AI strategic frameworks mapping to active machine opponents. */
    private List<IAPlayer> aiPlayers;


    /**
     * Automatically triggered by the JavaFX FXMLLoader runtime lifecycle once layout resources are fully ready. <p>
     *
     * Initializes core operational collections, configures responsive visual layout spaces
     * based on user engine settings, assigns listeners, and starts decoupled timing loops
     * alongside keyboard macro event captures.
     */
    @FXML
    public void initialize() {
        setupCardInteractionEvents();

        pauseButton.setOnAction(event -> togglePauseMenu());

        int machineCount = GameSettings.getMachineCount();

        // Hide AIs don't used in the view
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

        aiPlayers = new ArrayList<>();
        for (int i = 1; i < gameModel.getPlayers().size(); i++) {
            aiPlayers.add(new IAPlayer(IAPlayer.Difficulty.HARD));
        }

        if (roundLabel != null) roundLabel.setText("1");

        isGameRunning = true;

        // Fire Externalized Time Mechanism Thread
        timeThread = new TimeThread(timeLabel);
        timeThread.start();

        turnThread = new TurnThread(
                gameModel,
                aiPlayers,
                this::refreshGraphicInterface,
                message -> {
                    if (historyList != null) {
                        historyList.getItems().add(0, message);
                    }
                },
                this::handleGameEnd
        );

        turnThread.setDaemon(true);
        turnThread.start();

        refreshGraphicInterface();

        // Enforce structural window keystroke captures upon runtime layout attachment
        Platform.runLater(() -> {
            if (tablePane != null && tablePane.getScene() != null) {
                tablePane.getScene().addEventHandler(KeyEvent.KEY_PRESSED, this::handleSystemKeyboardStroke);
                tablePane.requestFocus();
            }
        });
    }


    /**
     * Binds mouse click interactions to human hand selector button slots.
     */
    private void setupCardInteractionEvents() {
        if (cardButton1 != null) cardButton1.setOnMouseClicked(event -> highlightSelectedCard(0));
        if (cardButton2 != null) cardButton2.setOnMouseClicked(event -> highlightSelectedCard(1));
        if (cardButton3 != null) cardButton3.setOnMouseClicked(event -> highlightSelectedCard(2));
        if (cardButton4 != null) cardButton4.setOnMouseClicked(event -> highlightSelectedCard(3));

        if (playCardButton != null) {
            playCardButton.setOnAction(event -> processHumanMoveConfirmation());
        }
    }


    /**
     * Updates the CSS style context to highlight the selected card in the player's hand.
     *
     * @param handIndex Integer index position (0 to 3) representing the chosen card asset slot.
     */
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


    /**
     * Intercepts scene-wide keystrokes to bind structural macro triggers.
     * Specifically maps the {@link KeyCode#SPACE} shortcut
     * to confirm and play the highlighted card selection.
     *
     * @param event The triggered JavaFX {@link KeyEvent} context.
     */
    private void handleSystemKeyboardStroke(KeyEvent event) {
        if (event.getCode() == KeyCode.SPACE) {
            event.consume();
            processHumanMoveConfirmation();
        }
    }


    /**
     * Validates and submits the human player's card choice to the game engine. <p>
     *
     * If the move is legal, updates the match log utilizing localized translations
     * via {@link Translation#generateSpanishCardName(Card)} and shifts the active turn authority.
     * Displays alerts if selections are missing or illegal.
     */
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

            // Utilizing decentralized translation parsing services
            if (historyList != null) {
                historyList.getItems().add(0, "Jugaste: " + Translation.generateSpanishCardName(choice) + " | Suma: " + gameModel.getTableSum());
            }

            selectedHandIndex = -1;
            gameModel.getTurnSystem().advanceTurn();
            refreshGraphicInterface();
            //checkMatchTermination();

            if (turnThread != null) {
                turnThread.notifyHumanPlayed();
            }

        } catch (InvalidMoveException e) {
            displayStatusNotification("Jugada Ilegal", "Esta carta supera los 50 puntos. Intenta con otra.");
        }
    }


    /**
     * Synchronizes and re-renders JavaFX elements with the current state of the game model. <p>
     *
     * Evaluates human player move options (triggering instant elimination if no legal moves remain),
     * streams card image binary paths, updates running summary metrics, and configures turn badges.
     */
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

                if (!humanHasValidMoves  && !humanEliminationHandled) {
                    humanEliminationHandled = true;
                    displayStatusNotification("¡Eliminado!", "No tienes cartas válidas. Quedas eliminado.");

                    gameModel.eliminatePlayer(currentTurnPlayer);

                    if (historyList != null) {
                        historyList.getItems().add(0, "Tú (Humano) fuiste ELIMINADO.");
                    }
                    gameModel.getTurnSystem().advanceTurn();
                    refreshGraphicInterface();

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
        if (roundLabel != null) {
            roundLabel.setText(String.valueOf(
                    gameModel.getRoundNumber()
            ));
        }
    }


    /**
     * Evaluates win conditions to determine if the active match has concluded. <p></P>
     *
     * If conditions are met, running loops are closed, performance stats are compiled,
     * and a static transfer is processed right before transitioning to the post-game summary screen.
     */
    private void checkMatchTermination() {

        if (gameEndHandled) {
            return;
        }

        if (!gameModel.getTurnSystem().checkVictoryCondition()
                || !isGameRunning) {
            return;
        }

        gameEndHandled = true;
        isGameRunning = false;

        if (timeThread != null) timeThread.stopTimer();
        if (turnThread != null) turnThread.stopThread();

        Player winner = gameModel.getPlayers()
                .stream()
                .filter(player -> !player.isEliminated())
                .findFirst()
                .orElse(null);

        String winnerName =
                winner != null
                        ? winner.getName()
                        : "No Winner";

        String finalTime =
                timeThread != null
                        ? timeThread.getFormattedTime()
                        : "00:00";

        FinalController.setMatchResults(
                winnerName,
                gameModel.getRoundNumber(),
                finalTime
        );

        try {

            SceneManager.changeScene(Path.FinalView);

        } catch (IOException e) {

            e.printStackTrace();
        }
    }


    /**
     * Asynchronous callback interface link passed onto background threads
     * to safely alert the system loop of match termination on the FX thread.
     */
    private void handleGameEnd() {
        Platform.runLater(this::checkMatchTermination);
    }



    /**
     * Generates a structural modal confirmation or warning window container.
     *
     * @param title   Header layout identifier for the modal alert.
     * @param summary Context body string message providing notification details.
     */
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

        // Toggle execution locks over interactable control buttons
        playCardButton.setDisable(!humanTurn);

        // Hand cards
        cardButton1.setDisable(!humanTurn);
        cardButton2.setDisable(!humanTurn);
        cardButton3.setDisable(!humanTurn);
        cardButton4.setDisable(!humanTurn);
    }


    /**
     * Toggles the game's paused state, updates the visibility of the menu overlay,
     * and alerts background threads to halt or resume time calculations.
     */
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

        if (turnThread != null)
            if (gamePaused) {
                turnThread.pauseThread();
            } else {
                turnThread.resumeThread();
            }


        playCardButton.setDisable(gamePaused);

        cardButton1.setDisable(gamePaused);
        cardButton2.setDisable(gamePaused);
        cardButton3.setDisable(gamePaused);
        cardButton4.setDisable(gamePaused);
    }


    /**
     * Closes the active pause overlay and resumes background gameplay processes.
     */
    @FXML
    private void handleResumeGame() {

        gamePaused = false;

        pauseOverlay.setVisible(false);
        pauseOverlay.setManaged(false);

        if (timeThread != null) timeThread.resumeTimer();
        if (turnThread != null) turnThread.resumeThread();

        refreshGraphicInterface();
    }


    /**
     * Terminates the current game loop context and redirects the stage back to the main menu.
     */
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


    /**
     * Resets the match state engine and clears layout properties to instantly launch a new game.
     */
    @FXML
    private void handleNewGame() {

        if (turnThread != null) turnThread.stopThread();

        gameModel = new GameModel(configurationNames);
        gameModel.startNewGame();

        aiPlayers = new ArrayList<>();
        for (int i = 1; i < gameModel.getPlayers().size(); i++) {
            aiPlayers.add(new IAPlayer(IAPlayer.Difficulty.HARD));
        }

        selectedHandIndex = -1;

        if (historyList != null) {
            historyList.getItems().clear();
        }

        gameEndHandled = false;
        gamePaused = false;
        humanEliminationHandled = false;

        pauseOverlay.setVisible(false);
        pauseOverlay.setManaged(false);

        turnThread = new TurnThread(
                gameModel,
                aiPlayers,
                this::refreshGraphicInterface,
                message -> {
                    if (historyList != null) historyList.getItems().add(0, message);
                },
                this::handleGameEnd
        );
        turnThread.start();
        timeThread = new TimeThread(timeLabel);
        timeThread.start();

        refreshGraphicInterface();
    }
}
