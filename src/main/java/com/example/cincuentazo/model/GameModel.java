package com.example.cincuentazo.model;

import com.example.cincuentazo.exceptions.*;
import com.example.cincuentazo.model.intefaces.IGameModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the central game engine responsible for maintaining and
 * coordinating the complete state of a Cincuentazo match. <p>
 *
 * It manages the deck, players, turn system, table cards, accumulated score,
 * round progression, and delegates rule validation to {@link GameRules}.
 *
 * @author Andres Felipe Rodríguez
 * @version 1.0
 */
public class GameModel implements IGameModel {

    private final Deck deck;
    private final List<Card> tablePile;
    private final List<Player> players;
    private final Turn turnSystem;
    private int tableSum;
    private int roundNumber;

    /**
     * Constructs a new {@code GameModel} state engine instance initialized for a fresh match. <p>
     *
     * Allocates internal collections for the playing field and instantiates participating
     * entities based on the provided list of identities. <p>
     *
     * the initial entity in the list (index {@code 0}) is designated as a human player,
     * while subsequent entries are flagged as simulated/AI participants.
     *
     * @param playerNames A {@link List} of strings representing the names or profiles
     * of the players joining the room. Index 0 must represent the human user.
     */
    public GameModel(List<String> playerNames) {
        this.deck = new Deck();
        this.tablePile = new ArrayList<>();
        this.players = new ArrayList<>();
        this.tableSum = 0;

        // Setup players (First name is Human, rest are simulated)
        for (int i = 0; i < playerNames.size(); i++) {
            this.players.add(new Player(playerNames.get(i), i == 0));
        }
        this.turnSystem = new Turn(this.players);
    }

    /**
     * Prepares match elements, shuffles, distributes initial cards, and deals
     * the starting card onto the table.
     */
    public void startNewGame() {
        deck.generateDeck();
        deck.shuffleDeck();
        tableSum = 0;
        tablePile.clear();
        roundNumber = 1;

        // Deal 4 cards per player
        for (Player player : players) {
            player.getHand().clear();
            player.setEliminated(false);
            for (int c = 0; c < 4; c++) {
                try {
                    player.addCardToHand(deck.drawCard());
                } catch (EmptyDeckException ignored) {}
            }
        }

        // Drop initial table card
        try {
            Card initialCard = deck.drawCard();
            tablePile.add(initialCard);
            tableSum = GameRules.calculateCardValue(initialCard, 0);
        } catch (EmptyDeckException ignored) {}
    }

    /**
     * Core action allowing a player to execute a move.
     * * @param player the player committing the card action
     * @param card the selected card from hand
     * @throws InvalidMoveException if the choice violates the 50-point safety line
     */
    public void playTurnAction(Player player, Card card) throws InvalidMoveException {
        if (player.isEliminated()) {
            throw new PlayerEliminatedException("Eliminated players cannot perform actions.");
        }
        if (!GameRules.isValidMove(card, tableSum)) {
            throw new InvalidMoveException("Playing this card exceeds the maximum limits.");
        }

        // Apply point calculations
        tableSum += GameRules.calculateCardValue(card, tableSum);
        player.removeCardFromHand(card);
        tablePile.add(card);
        roundNumber++;

        // Draw replenishment card
        try {
            player.addCardToHand(deck.drawCard());
        } catch (EmptyDeckException e) {
            recycleTableDeck();
            try {
                player.addCardToHand(deck.drawCard());
            } catch (EmptyDeckException extreme) {
                // Occurs only if deck is exhausted completely beyond recovery
            }
        }
    }

    /**
     * recycles discarded table items back into the operational draw pile stack,
     * protecting the immediate active top card.
     */
    private void recycleTableDeck() {
        if (tablePile.size() <= 1) return;
        Card activeTopCard = tablePile.remove(tablePile.size() - 1);

        deck.recycleDiscardPile(new ArrayList<>(tablePile));
        tablePile.clear();
        tablePile.add(activeTopCard);
    }

    /**
     * Eliminates a player and moves their remaining cards to the bottom of the deck.
     */
    public void eliminatePlayer(Player player) {
        player.setEliminated(true);
        deck.recycleDiscardPile(new ArrayList<>(player.getHand()));
        player.getHand().clear();
    }

    /**
     * Returns the current round number of the match.
     * @return the number representing the current completed turn count.
     */
    public int getRoundNumber() {return roundNumber;}

    /**
     * Returns the current accumulated value on the table.
     * @return the current table sum.
     */
    public int getTableSum() {
        return tableSum;
    }

    /**
     * Returns the collection of players participating in the match.
     * @return the list containing every player instance.
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * Returns the turn management component responsible for controlling the
     * player rotation throughout the match.
     *
     * @return the game's turn controller.
     */
    public Turn getTurnSystem() {
        return turnSystem;
    }

    /**
     * Returns the deck currently used by the match.
     * @return the active draw deck.
     */
    public Deck getDeck() {
        return deck;
    }

    /**
     * Returns the most recently played card currently visible on the table.
     *
     * @return the top card of the table pile, or {@code null} if no card is
     * currently available.
     */
    public Card getTopTableCard() {
        return tablePile.isEmpty() ? null : tablePile.get(tablePile.size() - 1);
    }
}
