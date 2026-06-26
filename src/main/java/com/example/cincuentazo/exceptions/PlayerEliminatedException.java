package com.example.cincuentazo.exceptions;
/**
 * Unchecked exception thrown when an operation is performed on or by
 * a player who has already been eliminated from the game.
 * @author Andrés Felipe Rodríguez García
 * @version 1.0
 */
public class PlayerEliminatedException extends RuntimeException {
    public PlayerEliminatedException(String message) {
        super(message);
    }
}
