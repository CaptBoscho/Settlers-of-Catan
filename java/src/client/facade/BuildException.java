package client.facade;

/**
 * Throws an exception if there is a build error in
 * the Facade.
 */
public class BuildException extends Exception {
    public BuildException(string message) {super(message);}
}