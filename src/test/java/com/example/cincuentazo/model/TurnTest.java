package com.example.cincuentazo.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TurnTest {

    @Test
    void shouldAdvanceTurn() {

        List<Player> players = new ArrayList<>();

        players.add(new Player("Human", true));
        players.add(new Player("AI1", false));

        Turn turn = new Turn(players);

        turn.advanceTurn();

        assertEquals(
                "AI1",
                turn.getCurrentPlayer().getName()
        );
    }

    @Test
    void shouldSkipEliminatedPlayer() {

        Player human = new Player("Human", true);
        Player ai1 = new Player("AI1", false);
        Player ai2 = new Player("AI2", false);

        ai1.setEliminated(true);

        List<Player> players = List.of(
                human,
                ai1,
                ai2
        );

        Turn turn = new Turn(players);

        turn.advanceTurn();

        assertEquals(
                "AI2",
                turn.getCurrentPlayer().getName()
        );
    }

    @Test
    void shouldDetectVictoryCondition() {

        Player human = new Player("Human", true);
        Player ai = new Player("AI", false);

        ai.setEliminated(true);

        Turn turn = new Turn(List.of(human, ai));

        assertTrue(turn.checkVictoryCondition());
    }
}