package com.dargueta.shared.exceptions;

/**
 * Created by corne on 1/30/2016.
 * Exception for moving the Robber
 */
public class MoveRobberException extends Exception {
    /**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public MoveRobberException(String message) {
        super(message);
    }
}
