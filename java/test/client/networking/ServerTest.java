package client.networking;

import client.data.GameInfo;
import client.services.IServer;
import client.services.MissingUserCookieException;
import client.services.ServerProxy;
import org.junit.Test;
import shared.definitions.CatanColor;
import shared.dto.*;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Derek Argueta
 */
public class ServerTest {

    private IServer server = new ServerProxy("localhost", 8081);

    @Test
    public void testCreateGame() {
        CreateGameDTO dto = new CreateGameDTO(false, false, false, "dummy test game");
        assertEquals(server.createNewGame(dto).toString(), "dummy test game");
    }

    @Test
    public void testLogin() {
        AuthDTO dto = new AuthDTO("totally", "fake");
        assertFalse(server.authenticateUser(dto));

        dto = new AuthDTO("Sam", "sam");
        assertTrue(server.authenticateUser(dto));
    }

    @Test
    public void testGetGames() {
        List<GameInfo> games = server.getAllGames();

        // the server is preloaded with at least 3 games
        assertTrue(games.size() >= 3);

        for(GameInfo info : games) {
            assertNotNull(info);
            assertTrue(!info.getTitle().equals(""));
        }
    }

    @Test
    public void testRegisterNewUser() {
        AuthDTO dto = new AuthDTO("user", "password");
        assertTrue(server.registerUser(dto));
    }

    @Test
    public void testJoinGame() {
        // test without having a user via cookies
        JoinGameDTO dto = new JoinGameDTO(4, CatanColor.BROWN);
        String result = server.joinGame(dto);
        assertEquals(result, "The catan.user HTTP cookie is missing.  You must login before calling this method.");
    }

    @Test
    public void testBadGetCurrentGameModel() {
        GetCurrentModelDTO dto = new GetCurrentModelDTO(2);
        try {
            server.getCurrentModel(dto);
            fail("MissingUserCookieException should be thrown");
        } catch(MissingUserCookieException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testBadSendChat() {
        SendChatDTO dto = new SendChatDTO(2, "hello world");
        try {
            server.sendChat(dto);
            fail("MissingUserCookieException should be thrown");
        } catch(MissingUserCookieException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testBadRollNumber() {
        RollNumberDTO dto = new RollNumberDTO(2, 2);
        try {
            server.rollNumber(dto);
            fail("MissingUserCookieException should be thrown");
        } catch(MissingUserCookieException e) {
            assertTrue(true);
        }
    }
}
