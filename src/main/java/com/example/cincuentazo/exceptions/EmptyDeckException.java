package com.example.cincuentazo.exceptions;
/**
 * Checked exception thrown when an attempt is made to draw from an empty deck
 * before triggering the recycling process.
 * @author AndresF395 aka Andrés Felipe Rodríguez García
 * @version 1.0
 */
public class EmptyDeckException extends RuntimeException {
    public EmptyDeckException(String message) {
        super(message);
    }
}
