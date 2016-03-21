package com.dargueta.shared.exceptions;

/**
 * Created by Kyle 'TMD' Cornelison on 2/24/2016.
 *
 * Exception when a user tries to perform an invalid action for the current game state
 */
public class InvalidStateActionException extends Exception {
    public InvalidStateActionException(String message) {
        super(message);
    }
}
