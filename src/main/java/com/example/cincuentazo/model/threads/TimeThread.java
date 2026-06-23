package com.example.cincuentazo.model.threads;

import javafx.application.Platform;
import javafx.scene.control.Label;

/**
 * Independent thread responsible for handling the game's timer.
 * Updates the JavaFX UI safely without blocking the main application thread.
 * * @author Andrés Felipe Rodríguez García
 * @version 1.0
 */
public class TimeThread extends Thread {

    private volatile boolean running = true;
    private int secondsElapsed = 0;
    private final Label timeLabel;

    /**
     * Constructs the TimeThread linked to a specific UI Label.
     * * @param timeLabel The JavaFX Label where the time will be displayed.
     */
    public TimeThread(Label timeLabel) {
        this.timeLabel = timeLabel;
        setDaemon(true); // Ensures the thread dies when the application closes
    }

    /**
     * Safely stops the timer loop and interrupts the thread.
     */
    public void stopTimer() {
        running = false;
        this.interrupt();
    }

    @Override
    public void run() {
        while (running) {
            try {
                Thread.sleep(1000);
                secondsElapsed++;
                int mins = secondsElapsed / 60;
                int secs = secondsElapsed % 60;
                String timeStr = String.format("%02d:%02d", mins, secs);

                // Safe UI update on the JavaFX Thread
                Platform.runLater(() -> {
                    if (timeLabel != null) {
                        timeLabel.setText(timeStr);
                    }
                });
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break; // Exit the loop if interrupted
            }
        }
    }
}