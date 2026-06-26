package com.example.cincuentazo.exceptions;
/**
 * Checked exception thrown when a player tries to play a card that
 * causes the total table sum to exceed 50 points.
 * @author Andrés Felipe Rodríguez García
 * @version 1.0
 */
public class InvalidMoveException extends RuntimeException {
    public InvalidMoveException(String message) {
        super(message);
    }
}
