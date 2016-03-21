package com.dargueta.client.facade;

/**
 * Throws an exception if there is a build error in
 * the Facade.
 */
public class BuildException extends Exception {
    public BuildException(String message) {super(message);}
}