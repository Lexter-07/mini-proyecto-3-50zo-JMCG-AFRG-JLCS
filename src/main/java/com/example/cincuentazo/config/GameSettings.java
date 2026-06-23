package com.example.cincuentazo.config;

/**
 * Stores game settings selected in the menu.
 */
public class GameSettings {

    private static int machineCount = 1;

    public static int getMachineCount() {
        return machineCount;
    }

    public static void setMachineCount(int machineCount) {
        GameSettings.machineCount = machineCount;
    }
}