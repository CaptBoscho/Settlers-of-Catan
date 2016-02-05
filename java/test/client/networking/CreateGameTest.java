package client.networking;

import static org.junit.Assert.assertEquals;

import client.services.IServer;
import client.services.ServerProxy;
import org.junit.Test;
import shared.dto.CreateGameDTO;

/**
 * @author Derek Argueta
 */
public class CreateGameTest {

    @Test
    public void testCreateGame() {
        IServer server = new ServerProxy("localhost", 8081);
        CreateGameDTO dto = new CreateGameDTO(false, false, false, "dummy test game");
        assertEquals(server.createNewGame(dto).toString(), "dummy test game");
    }
}
