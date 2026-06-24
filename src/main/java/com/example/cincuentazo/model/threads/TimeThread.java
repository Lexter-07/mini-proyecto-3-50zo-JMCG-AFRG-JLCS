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
    private volatile boolean paused = false;
    private final Object pauseLock = new Object();

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

                synchronized (pauseLock) {
                    while (paused) {
                        pauseLock.wait();
                    }
                }

                Thread.sleep(1000);

                secondsElapsed++;

                Platform.runLater(() ->
                        timeLabel.setText(formatTime(secondsElapsed)));

            } catch (InterruptedException e) {

                if (!running) {
                    break;
                }
            }
        }
    }

    private String formatTime(int totalSeconds) {

        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;

        return String.format("%02d:%02d", minutes, seconds);
    }

    public void pauseTimer() {
        paused = true;
        interrupt();
    }

    public void resumeTimer() {

        synchronized (pauseLock) {
            paused = false;
            pauseLock.notifyAll();
        }
    }

    public int getSecondsElapsed() {return secondsElapsed;}
    public String getFormattedTime() {return formatTime(secondsElapsed);}
}