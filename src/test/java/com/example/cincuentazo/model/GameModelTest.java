package com.example.cincuentazo.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameModelTest {

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