package com.example.cincuentazo.model.exceptions;

/**
 * Thrown when an eliminated player
 * attempts to perform an action.
 */
public class PlayerEliminatedException extends Exception {

    public PlayerEliminatedException(String message) {
        super(message);
    }

}