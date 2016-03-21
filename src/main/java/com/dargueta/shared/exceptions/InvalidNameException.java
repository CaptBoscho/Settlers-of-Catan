package com.dargueta.shared.exceptions;

/**
 * Exception for invalid player names
 */
public class InvalidNameException extends Exception {
    public InvalidNameException(String message) {
        super(message);
    }
}

