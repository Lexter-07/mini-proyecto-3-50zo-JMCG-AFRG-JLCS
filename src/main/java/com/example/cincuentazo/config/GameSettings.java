package com.example.cincuentazo.config;

/**
 * Global configuration container responsible for storing
 * match initialization preferences selected before starting
 * a game session. <p>
 *
 * Currently maintains the number of AI-controlled opponents
 * that will participate in the upcoming match.
 * Values are Stores game settings selected in the menu.
 *
 * @author Jose Manuel Cardona Gil
 * @version 1.0
 */
public class GameSettings {

    private static int machineCount = 1;

    /**
     * Retrieves the configured number of machine opponents.
     * @return the amount of AI players selected for the match
     */
    public static int getMachineCount() {
        return machineCount;
    }

    /**
     * Updates the number of machine opponents that will participate
     * in future game sessions.
     * @param machineCount number of AI-controlled players
     */
    public static void setMachineCount(int machineCount) {
        GameSettings.machineCount = machineCount;
    }
}