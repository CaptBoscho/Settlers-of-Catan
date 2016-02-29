package client.services;


import shared.model.game.Game;

import java.util.Timer;
import java.util.TimerTask;

/**
 * The poller keeps the client updated with the game information via long-polling.
 *
 * @author Derek Argueta
 */
public final class Poller {

    private final static int DEFAULT_POLL_INTERVAL = 200;
    private IServer server;
    private Timer poller;
    private int state = -1;

    private static Poller instance;

    public static Poller getInstance() {
        if(instance == null) {
            instance = new Poller(ServerProxy.getInstance());
        }
        return instance;
    }

    /**
     * Construct a poller instance using the given server
     * @param server An instance of IServer - could be a real networking server, or a mock server for testing.
     */
    public Poller(IServer server) {
        this.server = server;
    }

    /**
     * Indicates if the poller is running
     * @return boolean value indicating if the poller is running
     */
    public boolean isRunning() {
        return poller != null;
    }

    public void start() {
        assert server != null;

        // don't do anything if the poller is already started
        if(poller != null) return;

        poller = new Timer(true);
        poller.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    switch(state) {
                        case 1:
                            server.getCurrentModel(Game.getInstance().getVersion());
                            break;
                        case 2:
                            server.getLatestPlayers();
                            break;
                    }
                } catch (MissingUserCookieException e) {
                    e.printStackTrace();
                }
            }
        }, 0, DEFAULT_POLL_INTERVAL);
    }

    /**
     * Stops the poller and de-allocates it (sets to null)
     */
    public void stop() {
        poller.cancel();
        poller = null;
    }

    public void setModelPolling() {
        this.state = 1;
    }

    public void setPlayerWaitingPolling() {
        this.state = 2;
    }

    public void setListGamePolling() {
        this.state = 3;
    }
}
