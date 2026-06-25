package com.example.cincuentazo.model.intefaces;

/**
 * Defines the behavioral contract for the deck of playing cards in the game. <p>
 *
 * Implementing classes are responsible for managing the structural lifecycle of a
 * card deck, which includes instantiating the standard set of playing cards,
 * randomizing their sequence, and tracking the remaining count as cards are drawn.
 *
 * @author Jorge Luis Castro Scarpetta
 * @version 1.0
 */
public interface IDeck {

    /**
     * Populates the deck with a complete, standard set of card entities.
     * This method is invoked during initial match setups or when a full
     * deck reset is required to clear previous states.
     */
    void generateDeck();

    /**
     * Shuffles the current cards remaining in the draw pile.
     */
    void shuffleDeck();

    /**
     * Retrieves the total number of cards currently remaining in the deck's draw pile.
     * @return An integer representing the current quantity of cards available to be drawn.
     */
    int getRemainingCount();
}
