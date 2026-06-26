package com.example.cincuentazo.model.threads;

import javafx.application.Platform;
import javafx.scene.control.Label;

/**
 * Independent thread responsible for handling the game's timer.
 * Updates the JavaFX UI safely without blocking the main application thread.
 * @author Andrés Felipe Rodríguez García
 * @version 1.0
 */
public class TimeThread extends Thread {

    /** Flag controlling the main loop execution; volatile ensures cross-thread visibility. */
    private volatile boolean running = true;

    /** Accumulated time tracking counter in total seconds. */
    private int secondsElapsed = 0;

    /** Target UI JavaFX label where the formatted string MM:SS is rendered. */
    private final Label timeLabel;

    /** Flag denoting whether the timer loop is temporarily suspended. */
    private volatile boolean paused = false;

    /** Internal monitor lock object used to coordinate pause and resume state changes safely. */
    private final Object pauseLock = new Object();

    /**
     * Constructs the TimeThread linked to a specific UI Label.
     * @param timeLabel The JavaFX Label where the time will be displayed.
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

    /**
     * Main runtime execution loop for the background thread.
     * <p>
     * Continuously handles thread synchronization locks, increments elapsed time
     * every 1000 milliseconds, and invokes JavaFX updates safely.
     * Handles interrupts gracefully based on whether the game is pausing or fully exiting.
     */
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

    /**
     * Converts raw elapsed seconds into a standardized stopwatch time format.
     *
     * @param totalSeconds Total accumulated seconds to transform.
     * @return A formatted String representing time as {@code MM:SS}.
     */
    private String formatTime(int totalSeconds) {

        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;

        return String.format("%02d:%02d", minutes, seconds);
    }

    /**
     * Halts the timer increment logic temporarily by switching the paused status
     * and breaking the current sleep block.
     */
    public void pauseTimer() {
        paused = true;
        interrupt();
    }

    /**
     * Resumes the timer increment logic and notifies the waiting lock monitor
     * to continue the tracking execution loop.
     */
    public void resumeTimer() {

        synchronized (pauseLock) {
            paused = false;
            pauseLock.notifyAll();
        }
    }

    /**
     * Gets the current elapsed gameplay duration pre-formatted for text display.
     *
     * @return Formatted human-readable timestamp in an {@code MM:SS} structure.
     */
    public String getFormattedTime() {return formatTime(secondsElapsed);}
}