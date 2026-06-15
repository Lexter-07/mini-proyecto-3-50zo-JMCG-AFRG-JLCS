package com.example.cincuentazo.model;

/**
 * Represents the turn order during a Cincuentazo match.
 *
 * <p>The game consists of four participants:
 * <ul>
 *     <li>The human player.</li>
 *     <li>Machine 1.</li>
 *     <li>Machine 2.</li>
 *     <li>Machine 3.</li>
 * </ul>
 *
 * <p>This enumeration also provides utility methods to
 * determine the next player's turn.
 *
 * @author Jose Manuel Cardona Gil
 * @version 1.0
 */
public enum Turn {

    /**
     * Human player's turn.
     */
    HUMAN,

    /**
     * First computer player's turn.
     */
    MACHINE_1,

    /**
     * Second computer player's turn.
     */
    MACHINE_2,

    /**
     * Third computer player's turn.
     */
    MACHINE_3;

    /**
     * Returns the next turn in the game sequence.
     *
     * <pre>
     * HUMAN     → MACHINE_1
     * MACHINE_1 → MACHINE_2
     * MACHINE_2 → MACHINE_3
     * MACHINE_3 → HUMAN
     * </pre>
     *
     * @return the next turn
     */
    public Turn next() {

        return switch (this) {

            case HUMAN -> MACHINE_1;

            case MACHINE_1 -> MACHINE_2;

            case MACHINE_2 -> MACHINE_3;

            case MACHINE_3 -> HUMAN;
        };
    }

    /**
     * Returns whether the current turn belongs to the human player.
     *
     * @return {@code true} if it is the human player's turn
     */
    public boolean isHuman() {
        return this == HUMAN;
    }

    /**
     * Returns whether the current turn belongs to a computer player.
     *
     * @return {@code true} if it is a machine's turn
     */
    public boolean isMachine() {
        return this != HUMAN;
    }

}