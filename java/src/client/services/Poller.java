package client.services;


import shared.model.game.Game;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;

/**
 * The poller keeps the client updated with the game information via long-polling.
 *
 * @author Derek Argueta
 */
public final class Poller {

    private final static int DEFAULT_POLL_INTERVAL = 200;
    private IServer server;
    private Timer poller;
    private int timeout;

    Callable<Void> pollingFunction;

    /**
     * Construct a poller instance using the given server
     * @param server An instance of IServer - could be a real networking server, or a mock server for testing.
     */
    public Poller(IServer server) {
        this.server = server;
        timeout = DEFAULT_POLL_INTERVAL;
        this.pollingFunction = null;
    }

    public void setPollingFunction(Callable<Void> pollingFunction) {
        this.pollingFunction = pollingFunction;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    /**
     * Indicates if the poller is running
     * @return boolean value indicating if the poller is running
     */
    public boolean isRunning() {
        return poller != null;
    }

    public void start() {
        poller = new Timer(true);
        poller.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if(pollingFunction == null) {
                    try {
                        server.getCurrentModel(Game.getInstance().getVersion());
                    } catch (MissingUserCookieException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        pollingFunction.call();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }, 0, this.timeout);
    }

    /**
     * Stops the poller and de-allocates it (sets to null)
     */
    public void stop() {
        poller.cancel();
        poller = null;
    }
}
