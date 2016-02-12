package client.networking;

import client.data.GameInfo;
import client.services.*;
import org.junit.Before;
import org.junit.Test;
import shared.definitions.CatanColor;
import shared.definitions.ResourceType;
import shared.dto.*;
import shared.locations.*;
import shared.model.game.Game;
import shared.model.game.trade.Trade;
import shared.model.game.trade.TradePackage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

/**
 * @author Derek Argueta
 */
public class ServerTest {

    private IServer server = new ServerProxy("localhost", 8081);

    private String generateString(Random rng, String characters, int length) {
        char[] text = new char[length];
        for (int i = 0; i < length; i++) {
            text[i] = characters.charAt(rng.nextInt(characters.length()));
        }
        return new String(text);
    }

    @Before
    public void setup() {
        UserCookie.getInstance().clearCookies();
    }

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
        String fakeUsername = generateString(new Random(), "qwertyuiopasdfghjklzxcvbnm", 10);
        String fakePassword = generateString(new Random(), "qwertyuiopasdfghjklzxcvbnm", 10);
        AuthDTO dto = new AuthDTO(fakeUsername, fakePassword);
        assertTrue(server.registerUser(dto));

        // registration should now fail if we try the same credentials
        assertFalse(server.registerUser(dto));
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
        try {
            server.getCurrentModel(3);
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
        } catch(MissingUserCookieException | CommandExecutionFailed e) {
            assertTrue(true);
        }
    }

    @Test
    public void testBadRobPlayer() {
        RobPlayerDTO dto = new RobPlayerDTO(1, 2, new HexLocation(4, 5));
        try {
            server.robPlayer(dto);
            fail("MissingUserCookieException should be thrown");
        } catch(MissingUserCookieException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testBadFinishTurn() {
        FinishTurnDTO dto = new FinishTurnDTO(2);
        try {
            server.finishTurn(dto);
            fail("MissingUserCookieException should be thrown");
        } catch(MissingUserCookieException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testBadBuyDevCard() {
        BuyDevCardDTO dto = new BuyDevCardDTO(2);
        try {
            server.buyDevCard(dto);
            fail("MissingUserCookieException should be thrown");
        } catch(MissingUserCookieException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testBadPlayYOPCard() {
        PlayYOPCardDTO dto = new PlayYOPCardDTO(2, ResourceType.BRICK, ResourceType.ORE);
        try {
            server.playYearOfPlentyCard(dto);
            fail("MissingUserCookieException should be thrown");
        } catch(MissingUserCookieException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testBadPlayRoadBuildingCard() {
        BuildRoadDTO dto = new BuildRoadDTO(2, new EdgeLocation(new HexLocation(3, 4), EdgeDirection.South), true);
        try {
            server.playRoadBuildingCard(dto);
            fail("MissingUserCookieException should be thrown");
        } catch(MissingUserCookieException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testBadPlaySoldierCard() {
        PlaySoldierCardDTO dto = new PlaySoldierCardDTO(2, 3, new HexLocation(4, 3));
        try {
            server.playSoldierCard(dto);
            fail("MissingUserCookieException should be thrown");
        } catch(MissingUserCookieException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testBadPlayMonopolyCard() {
        PlayMonopolyDTO dto = new PlayMonopolyDTO(2, "ore");
        try {
            server.playMonopolyCard(dto);
            fail("MissingUserCookieException should be thrown");
        } catch(MissingUserCookieException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testBadPlayMonumentCard() {
        PlayMonumentDTO dto = new PlayMonumentDTO(2);
        try {
            server.playMonumentCard(dto);
            fail("MissingUserCookieException should be thrown");
        } catch(MissingUserCookieException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testBadBuildRoad() {
        BuildRoadDTO dto = new BuildRoadDTO(2, new EdgeLocation(new HexLocation(3, 4), EdgeDirection.South), true);
        try {
            server.buildRoad(dto);
            fail("MissingUserCookieException should be thrown");
        } catch(MissingUserCookieException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testBadBuildSettlement() {
        BuildSettlementDTO dto = new BuildSettlementDTO(2, new VertexLocation(new HexLocation(2, 3), VertexDirection.SouthEast), false);
        try {
            server.buildSettlement(dto);
            fail("MissingUserCookieException should be thrown");
        } catch(MissingUserCookieException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testBadBuildCity() {
        BuildCityDTO dto = new BuildCityDTO(2, new VertexLocation(new HexLocation(2, 3), VertexDirection.SouthEast));
        try {
            server.buildCity(dto);
            fail("MissingUserCookieException should be thrown");
        } catch(MissingUserCookieException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testBadOfferTrade() {
        ArrayList<ResourceType> list1 = new ArrayList<>();
        ArrayList<ResourceType> list2 = new ArrayList<>();
        list1.add(ResourceType.BRICK);
        list2.add(ResourceType.WOOD);
        TradePackage tp1 = new TradePackage(2, list1);
        TradePackage tp2 = new TradePackage(3, list2);
        OfferTradeDTO dto = new OfferTradeDTO(2, new Trade(tp1, tp2), 3);
        try {
            server.offerTrade(dto);
            fail("MissingUserCookieException should be thrown");
        } catch(MissingUserCookieException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testBadTradeResponse() {
        TradeOfferResponseDTO dto = new TradeOfferResponseDTO(2, false);
        try {
            server.respondToTradeOffer(dto);
            fail("MissingUserCookieException should be thrown");
        } catch(MissingUserCookieException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testBadMaritimeTrade() {
        MaritimeTradeDTO dto = new MaritimeTradeDTO(2, 4, "wood", "ore");
        try {
            server.maritimeTrade(dto);
            fail("MissingUserCookieException should be thrown");
        } catch(MissingUserCookieException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testBadDiscardCards() {
        DiscardCardsDTO dto = new DiscardCardsDTO(2, 1, 1, 1, 1, 1);
        try {
            server.discardCards(dto);
            fail("MissingUserCookieException should be thrown");
        } catch(MissingUserCookieException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testActualUserInteraction() {

        // Sam signs in
        AuthDTO dto = new AuthDTO("Sam", "sam");
        assertTrue(server.authenticateUser(dto));

        // Sam creates a new game
        CreateGameDTO cdto = new CreateGameDTO(false, false, false, "my test game yooo");
        assertNotNull(server.createNewGame(cdto));

        // Sam sees all available games
        List<GameInfo> games = server.getAllGames();
        assertTrue(games.size() >= 4);

        // Sam joins the game he created
        JoinGameDTO jdto = new JoinGameDTO(3, CatanColor.WHITE);
        assertEquals(server.joinGame(jdto), "Success");

        // Sam sends a chat in the game
        SendChatDTO sdto = new SendChatDTO(0, "hello world");
        try {
            server.sendChat(sdto);
            assertTrue(true);
        } catch (MissingUserCookieException e) {
            fail("Should be able to send message perfectly fine");
        }

        // Sam rolled a 4
//        RollNumberDTO rdto = new RollNumberDTO(0, 4);
//        try {
//            server.rollNumber(rdto);
//            assertTrue(true);
//        } catch (MissingUserCookieException | CommandExecutionFailed e) {
//            fail();
//        }

    }

    @Test
    public void testPoller() {

        AuthDTO dto = new AuthDTO("Sam", "sam");
        assertTrue(server.authenticateUser(dto));
        // Sam joins the game he created
        JoinGameDTO jdto = new JoinGameDTO(3, CatanColor.WHITE);
        assertEquals(server.joinGame(jdto), "Success");

        int initialVersion = Game.getInstance().getVersion();

        Poller poller = new Poller(server);
        poller.start();

        long t= System.currentTimeMillis();
        long end = t+5000;
        while(System.currentTimeMillis() < end) {
            // do something
            // pause to avoid churning
//            assertTrue(Game.getInstance().getVersion() > initialVersion);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
