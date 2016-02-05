package client.networking;

import client.services.IServer;
import client.services.ServerProxy;
import org.junit.Test;
import shared.dto.AuthDTO;

import static org.junit.Assert.assertEquals;

/**
 * @author Derek Argueta
 */
public class AuthenticationTest {
    @Test
    public void testCreateGame() {
        IServer server = new ServerProxy("localhost", 8081);

        AuthDTO dto = new AuthDTO("totally", "fake");
        assertEquals(server.authenticateUser(dto), false);

        dto = new AuthDTO("Sam", "sam");
        assertEquals(server.authenticateUser(dto), true);
    }
}
