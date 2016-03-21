package com.dargueta.shared.exceptions;

/**
 * Exception used for deserialization, to be thrown when the JSON has invalid values.
 *
 * Created by Danny on 2/4/16.
 */
public class BadJsonException extends Exception {
    public BadJsonException(String s) {
        super(s);
    }
}
