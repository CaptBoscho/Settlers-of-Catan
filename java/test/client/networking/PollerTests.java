package client.networking;

import client.services.MockServer;
import client.services.Poller;
import org.junit.Test;

/**
 * @author Derek Argueta
 */
public class PollerTests {

    @Test
    public void testPoller() {
        Poller poller = new Poller(new MockServer());
    }
}
