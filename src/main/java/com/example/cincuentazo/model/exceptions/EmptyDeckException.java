package com.example.cincuentazo.model.exceptions;

/**
 * Thrown when attempting to draw a card
 * from an empty deck.
 */
public class EmptyDeckException extends Exception {

    public EmptyDeckException(String message) {
        super(message);
    }

}