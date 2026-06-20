package com.example.cincuentazo.model.exceptions;

/**
 * Thrown when a card cannot be played because
 * it would cause the total sum to exceed 50.
 */
public class InvalidCardPlayException extends Exception {

    public InvalidCardPlayException(String message) {
        super(message);
    }

}