package client.services;


import javax.swing.Timer;

/**
 * @author Derek Argueta
 */
public class Poller {

    private final static long DEFAULT_POLL_INTERVAL = 1000;
    private IServer server;
    private Timer poller;

    public Poller(IServer server) {
        this.server = server;
    }

    /**
     * Indicates if the poller is running
     * @return boolean value indicating if the poller is running
     */
    public boolean isRunning() {
        return poller.isRunning();
    }

    /**
     * Stops the poller and deallocates it (sets to null)
     */
    public void stop() {
        poller.stop();
        poller = null;
    }
}
