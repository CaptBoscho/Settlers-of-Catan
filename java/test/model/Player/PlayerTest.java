package model.Player;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import shared.definitions.CatanColor;
import shared.exceptions.FailedToRandomizeException;
import shared.model.bank.InvalidTypeException;
import shared.model.cards.Card;
import shared.model.cards.resources.Brick;
import shared.model.cards.resources.ResourceCard;
import shared.model.cards.resources.Wood;
import shared.model.game.Game;
import shared.model.player.Name;
import shared.model.player.Player;
import shared.model.player.PlayerManager;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Kyle Cornelison
 */
public class PlayerTest {
    private PlayerManager pm;

    @Before
    public void setUp() throws FailedToRandomizeException {
        List<Player> players = new ArrayList<>();

        try {
            Player one = new Player(0, CatanColor.BLUE, 0, new Name("Hope"));
            Player two = new Player(0, CatanColor.BROWN, 1, new Name("Corbin"));
            Player three = new Player(0, CatanColor.GREEN, 2, new Name("Hanna"));
            Player four = new Player(0, CatanColor.ORANGE, 3, new Name("Becca"));

            players.add(one);
            players.add(two);
            players.add(three);
            players.add(four);
        }catch(Exception e){
            fail();
        }

        pm = new PlayerManager(players);
        Game.getInstance().initializeGame(players, false, false, false);
    }

    @After
    public void tearDown() throws Exception {
        pm = null;
    }

    @Test
    public void testCanDiscardCards() throws Exception {
        for(Player p : pm.getPlayers()){
            assertFalse(p.canDiscardCards());
        }

        for(Player p : pm.getPlayers()){
            for(int i = 0; i < 9; i++){
                p.addResourceCard(new Brick());
            }
            assertTrue(p.canDiscardCards());
        }
    }

    @Test
    public void testDiscardCards() throws Exception, InvalidTypeException {
        List<Card> cards = new ArrayList<>();
        for(int j = 0; j < 4; j++){
            cards.add(new Brick());
        }

        for(Player p : pm.getPlayers()){
            for(int i = 0; i < 8; i++){
                p.addResourceCard(new Brick());
            }
            p.discardCards(cards);
            assertTrue(p.hasDiscarded());
            assertFalse(p.canDiscardCards());
            assertTrue(p.countResources() ==  4);
        }
    }

    @Test
    public void testCanOfferTrade() throws Exception {
        for(Player p : pm.getPlayers()) {
            assertFalse(p.canOfferTrade());
        }

        for(Player p : pm.getPlayers()){
            for(int i = 0; i < 8; i++){
                p.addResourceCard(new Brick());
            }
            assertTrue(p.canOfferTrade());
        }
    }

    @Test
    public void testCanMaritimeTrade() throws Exception {

    }

    @Test
    public void testCanBuyDevCard() throws Exception {

    }

    @Test
    public void testBuyDevCard() throws Exception {

    }

    @Test
    public void testCanUseYearOfPlenty() throws Exception {
        for(Player p : pm.getPlayers()) {
            assertFalse(p.canUseYearOfPlenty());
        }

        // TODO -- give YOP to a player and assert true
    }

    @Test
    public void testUseYearOfPlenty() throws Exception {

    }

    @Test
    public void testCanUseRoadBuilder() throws Exception {

    }

    @Test
    public void testUseRoadBuilder() throws Exception {

    }

    @Test
    public void testCanUseSoldier() throws Exception {
        for(Player p : pm.getPlayers()) {
            assertFalse(p.canUseSoldier());
        }

        // TODO -- give soldier card to a player and assert true
    }

    @Test
    public void testUseSoldier() throws Exception {

    }

    @Test
    public void testCanUseMonopoly() throws Exception {
        for(Player p : pm.getPlayers()) {
            assertFalse(p.canUseMonopoly());
        }

        // TODO -- give monopoly card to a player and assert true
    }

    @Test
    public void testUseMonopoly() throws Exception {

    }

    @Test
    public void testCanUseMonument() throws Exception {
        for(Player p : Game.getInstance().getPlayerManager().getPlayers()) {
            assertFalse(p.canUseMonument());
        }



        // TODO -- give monument card to a player and assert true
    }

    @Test
    public void testUseMonument() throws Exception {

    }

    @Test
    public void testCanPlaceRobber() throws Exception {

    }

    @Test
    public void testPlaceRobber() throws Exception {

    }

    @Test
    public void testCanBuildRoad() throws Exception {
        // no players should be able to build a road at first
        for(Player p : Game.getInstance().getPlayerManager().getPlayers()) {
            assertFalse(p.canBuildRoad());
        }
        // give player 0 the resources to build a road
        Game.getInstance().giveResource(new Brick(), 0);
        Game.getInstance().giveResource(new Wood(), 0);

        // verify he can build a road
        assertTrue(Game.getInstance().getPlayerManager().getPlayerByID(0).canBuildRoad());

        Game.getInstance().getPlayerManager().getPlayerByID(0).buildRoad();

        // verify that the resources were consumed
        assertFalse(Game.getInstance().getPlayerManager().getPlayerByID(0).canBuildRoad());
    }

    @Test
    public void testBuildRoad() throws Exception {

    }

    @Test
    public void testCanBuildSettlement() throws Exception {

    }

    @Test
    public void testBuildSettlement() throws Exception {

    }

    @Test
    public void testCanBuildCity() throws Exception {

    }

    @Test
    public void testBuildCity() throws Exception {

    }
}