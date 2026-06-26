package com.example.cincuentazo.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link GameModel} class. <p>
 *
 * Verifies the initialization of a new match and the
 * correct handling of player elimination.
 *
 * @author Jose Manuel Cardona Gil
 * @version 1.0
 */
class GameModelTest {

    /**
     * Verifies that a new game is initialized correctly. <p>
     *
     * Ensures that each player receives four cards,
     * an initial card is placed on the table and the round counter starts at one.
     */
    @Test
    void shouldStartNewGameCorrectly() {

        GameModel model = new GameModel(
                List.of("Human", "Machine1")
        );

        model.startNewGame();

        assertEquals(
                4,
                model.getPlayers().get(0).getHand().size()
        );

        assertNotNull(
                model.getTopTableCard()
        );

        assertEquals(
                1,
                model.getRoundNumber()
        );
    }

    /**
     * Verifies that eliminating a player correctly updates
     * their participation status.
     */
    @Test
    void shouldEliminatePlayer() {

        GameModel model = new GameModel(
                List.of("Human", "Machine1")
        );

        Player player = model.getPlayers().get(1);

        model.eliminatePlayer(player);

        assertTrue(player.isEliminated());
    }
}