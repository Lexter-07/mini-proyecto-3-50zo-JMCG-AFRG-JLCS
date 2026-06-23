package com.example.cincuentazo.exceptions;

/**
 * Unchecked exception thrown when a card is invalid or null.
 * @author AndresF395 aka Andrés Felipe Rodríguez García
 * @version 1.0
 */
public class InvalidCardException extends RuntimeException {
    public InvalidCardException(String message) {
        super(message);
    }
}
