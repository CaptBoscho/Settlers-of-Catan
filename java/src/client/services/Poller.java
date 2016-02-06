package client.services;


import shared.dto.GetCurrentModelDTO;

import java.util.Timer;
import java.util.TimerTask;

/**
 * The poller keeps the client updated with the game information via long-polling.
 *
 * @author Derek Argueta
 */
public class Poller {

    private final static int DEFAULT_POLL_INTERVAL = 1000;
    private IServer server;
    private Timer poller;

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
        poller = new Timer(true);
        poller.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                GetCurrentModelDTO dto = new GetCurrentModelDTO(4);
                try {
                    server.getCurrentModel(dto);
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
}
