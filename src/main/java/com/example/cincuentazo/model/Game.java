package com.example.cincuentazo.model;

import com.example.cincuentazo.model.exceptions.EmptyDeckException;
import com.example.cincuentazo.model.exceptions.InvalidCardPlayException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a complete Cincuentazo match.
 *
 * <p>This class is responsible for managing:
 * <ul>
 *     <li>The deck.</li>
 *     <li>The players.</li>
 *     <li>The current turn.</li>
 *     <li>The current accumulated value.</li>
 *     <li>The played cards history.</li>
 * </ul>
 *
 * The Game class contains the core game logic and should not
 * contain any JavaFX-related code.
 *
 * @author Jose Manuel Cardona Gil
 * @version 1.0
 */
public class Game {

    /**
     * Number of cards dealt to each player.
     */
    private static final int INITIAL_HAND_SIZE = 4;

    /**
     * Main deck.
     */
    private Deck deck;

    /**
     * All players in the match.
     */
    private final List<Player> players;

    /**
     * Current turn.
     */
    private Turn currentTurn;

    /**
     * Current accumulated value.
     */
    private int currentSum;

    /**
     * Last played card.
     */
    private Card lastPlayedCard;

    /**
     * Game history.
     */
    private final List<String> history;

    /**
     * Played cards pile.
     */
    private final List<Card> discardPile;

    /**
     * Creates a new game instance.
     */
    public Game() throws EmptyDeckException {

        this.deck = new Deck();

        this.players = new ArrayList<>();

        this.history = new ArrayList<>();

        this.discardPile = new ArrayList<>();

        initializePlayers();

        startGame();
    }

    /**
     * Creates all game participants.
     */
    private void initializePlayers() {

        players.add(new Player("You", false));

        players.add(new Player("Machine 1", true));

        players.add(new Player("Machine 2", true));

        players.add(new Player("Machine 3", true));
    }

    /**
     * Starts a new match.
     */
    public void startGame() throws EmptyDeckException {

        currentTurn = Turn.HUMAN;

        history.clear();

        discardPile.clear();

        Card initialCard = deck.drawCard();

        lastPlayedCard = initialCard;

        currentSum = initialCard.getValue();

        discardPile.add(initialCard);

        dealCards();
    }

    /**
     * Deals cards to all players.
     */
    private void dealCards() throws EmptyDeckException {

        for (Player player : players) {

            player.clearHand();

            for (int i = 0; i < INITIAL_HAND_SIZE; i++) {

                player.addCard(deck.drawCard());
            }
        }
    }

    /**
     * Plays a card from the current player's hand.
     *
     * @param cardIndex index of the selected card
     * @return the played card
     * @throws InvalidCardPlayException if the card would exceed 50
     */
    public Card playCard(int cardIndex)
            throws InvalidCardPlayException, EmptyDeckException {

        Player player = getCurrentPlayer();

        Card selectedCard =
                player.getHand()
                        .get(cardIndex);

        if (!selectedCard.canBePlayed(currentSum)) {

            throw new InvalidCardPlayException(
                    "This card cannot be played because it would exceed 50."
            );
        }

        Card playedCard =
                player.playCard(cardIndex);

        currentSum += playedCard.getValue();

        lastPlayedCard = playedCard;

        discardPile.add(playedCard);

        if (!deck.isEmpty()) {

            player.addCard(
                    deck.drawCard()
            );
        }

        history.add(
                player.getName()
                        + " played "
                        + playedCard
                        + " (Total: "
                        + currentSum
                        + ")"
        );

        checkElimination(player);

        advanceTurn();

        return playedCard;
    }

    /**
     * Checks if a player must be eliminated.
     *
     * @param player player to check
     */
    private void checkElimination(Player player) {

        if (!player.hasPlayableCard(currentSum)) {

            player.setEliminated(true);

            deck.addCards(player.getHand());

            player.clearHand();

            history.add(
                    player.getName()
                            + " has been eliminated."
            );
        }
    }

    /**
     * Advances the turn to the next active player.
     */
    private void advanceTurn() {

        do {

            currentTurn = currentTurn.next();

        } while (
                players.get(
                        currentTurn.getPlayerIndex()
                ).isEliminated()
        );
    }

    /**
     * Returns the current player.
     *
     * @return current player
     */
    public Player getCurrentPlayer() {

        return players.get(currentTurn.getPlayerIndex());
    }

    /**
     * Returns a player by index.
     *
     * @param index player index
     * @return player
     */
    public Player getPlayer(int index) {

        return players.get(index);
    }

    /**
     * Returns all players.
     *
     * @return players list
     */
    public List<Player> getPlayers() {

        return Collections.unmodifiableList(players);
    }

    /**
     * Returns the current turn.
     *
     * @return current turn
     */
    public Turn getCurrentTurn() {

        return currentTurn;
    }

    /**
     * Returns the current accumulated value.
     *
     * @return current sum
     */
    public int getCurrentSum() {

        return currentSum;
    }

    /**
     * Returns the last played card.
     *
     * @return last played card
     */
    public Card getLastPlayedCard() {

        return lastPlayedCard;
    }

    /**
     * Returns the game deck.
     *
     * @return deck
     */
    public Deck getDeck() {

        return deck;
    }

    /**
     * Returns the game history.
     *
     * @return history list
     */
    public List<String> getHistory() {

        return Collections.unmodifiableList(history);
    }

    /**
     * Resets the entire game.
     */
    public void resetGame() throws EmptyDeckException {

        deck = new Deck();

        startGame();
    }

    public List<Card> getDiscardPile() {
        return Collections.unmodifiableList(discardPile);
    }
}