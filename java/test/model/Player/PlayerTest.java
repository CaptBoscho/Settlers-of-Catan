package model.Player;

import client.facade.Facade;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import shared.definitions.CatanColor;
import shared.definitions.PortType;
import shared.exceptions.BadCallerException;
import shared.exceptions.InvalidNameException;
import shared.exceptions.InvalidPlayerException;
import shared.model.bank.InvalidTypeException;
import shared.model.cards.Card;
import shared.model.cards.devcards.*;
import shared.model.cards.resources.*;
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
    public void setUp() {
        final List<Player> players = new ArrayList<>();

        try {
            final Player one = new Player(0, CatanColor.BLUE, 0, 0, "Hope");
            final Player two = new Player(0, CatanColor.BROWN, 1, 1, "Corbin");
            final Player three = new Player(0, CatanColor.GREEN, 2, 2, "Hanna");
            final Player four = new Player(0, CatanColor.ORANGE, 3, 3, "Becca");

            players.add(one);
            players.add(two);
            players.add(three);
            players.add(four);
        } catch(Exception e) {
            fail();
        }

        pm = new PlayerManager(players);
        Facade.getInstance().getGame().initializeGame(players, false, false, false);
    }

    @After
    public void tearDown() throws Exception {
        pm = null;
    }

    @Test
    public void testCanDiscardCards() throws Exception {
        for(final Player p : pm.getPlayers()) {
            assertFalse(p.canDiscardCards());
        }

        for(final Player p : pm.getPlayers()) {
            for(int i = 0; i < 9; i++){
                p.addResourceCard(new Brick());
            }
            assertTrue(p.canDiscardCards());
        }
    }

    @Test
    public void testDiscardCards() throws Exception, InvalidTypeException {
        final List<Card> cards = new ArrayList<>();
        for(int j = 0; j < 4; j++){
            cards.add(new Brick());
        }

        for(final Player p : pm.getPlayers()) {
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
        for(final Player p : pm.getPlayers()) {
            assertFalse(p.canOfferTrade());
        }

        for(final Player p : pm.getPlayers()) {
            for(int i = 0; i < 8; i++){
                p.addResourceCard(new Brick());
            }
            assertTrue(p.canOfferTrade());
        }
    }

    @Test
    public void testCanMaritimeTrade() throws Exception {
        for(final Player p : pm.getPlayers()){
            assertFalse(p.canMaritimeTrade(PortType.BRICK));
        }

        for(final Player p : pm.getPlayers()){
            for(int i = 0; i < 8; i++){
                p.addResourceCard(new Brick());
            }
            assertTrue(p.canMaritimeTrade(PortType.BRICK));
            assertFalse(p.canMaritimeTrade(PortType.ORE));
        }
    }

    @Test
    public void testCanBuyDevCard() throws Exception {
        for(final Player p : pm.getPlayers()){
            assertFalse(p.canBuyDevCard());
        }

        for(final Player p : pm.getPlayers()){
            for(int i = 0; i < 8; i++){
                p.addResourceCard(new Sheep());
                p.addResourceCard(new Ore());
                p.addResourceCard(new Wheat());
            }
            assertTrue(p.canBuyDevCard());
        }
    }

    @Test
    public void testBuyDevCard() throws Exception {
        for(final Player p : pm.getPlayers()){
            p.addResourceCard(new Sheep());
            p.addResourceCard(new Ore());
            p.addResourceCard(new Wheat());

            assertTrue(p.canBuyDevCard());
            p.buyDevCard();
            assertFalse(p.canBuyDevCard());
        }
    }

    @Test
    public void testCanUseYearOfPlenty() throws Exception, BadCallerException {
        for(final Player p : pm.getPlayers()){
            assertFalse(p.canUseYearOfPlenty());
        }

        //Can't play if you've already played a dev card
        for(final Player p : pm.getPlayers()){
            p.addDevCard(new YearOfPlentyCard());
            p.setPlayedDevCard(true);
            assertFalse(p.canUseYearOfPlenty());
        }

        //Can't play until next turn
        for(final Player p : pm.getPlayers()){
            p.addDevCard(new YearOfPlentyCard());
            p.setPlayedDevCard(false);
            assertFalse(p.canUseYearOfPlenty());
        }

        //Can play now
        for(final Player p : pm.getPlayers()){
            p.getDevelopmentCardBank().moveNewToOld();
            assertTrue(p.canUseYearOfPlenty());
        }
    }

    @Test
    public void testUseYearOfPlenty() throws Exception, BadCallerException {
        for(final Player p : pm.getPlayers()){
            p.addDevCard(new YearOfPlentyCard());
            p.setPlayedDevCard(false);
            p.getDevelopmentCardBank().moveNewToOld();
            assertTrue(p.canUseYearOfPlenty());
            p.useYearOfPlenty();
            assertFalse(p.canUseYearOfPlenty());
        }
    }

    @Test
    public void testCanUseRoadBuilder() throws Exception, BadCallerException {
        for(final Player p : pm.getPlayers()){
            assertFalse(p.canUseRoadBuilder());
        }

        //Can't play if you've already played a dev card
        for(final Player p : pm.getPlayers()){
            p.addDevCard(new RoadBuildCard());
            p.setPlayedDevCard(true);
            assertFalse(p.canUseRoadBuilder());
        }

        //Can't play until next turn
        for(final Player p : pm.getPlayers()){
            p.addDevCard(new RoadBuildCard());
            p.setPlayedDevCard(false);
            assertFalse(p.canUseRoadBuilder());
        }

        //Can play now
        for(final Player p : pm.getPlayers()){
            p.getDevelopmentCardBank().moveNewToOld();
            assertTrue(p.canUseRoadBuilder());
        }
    }

    @Test
    public void testUseRoadBuilder() throws Exception, BadCallerException {
        for(final Player p : pm.getPlayers()){
            p.addDevCard(new RoadBuildCard());
            p.setPlayedDevCard(false);
            p.getDevelopmentCardBank().moveNewToOld();
            assertTrue(p.canUseRoadBuilder());
            p.useRoadBuilder();
            assertFalse(p.canUseRoadBuilder());
        }
    }

    @Test
    public void testCanUseSoldier() throws Exception, BadCallerException {
        for(final Player p : pm.getPlayers()){
            assertFalse(p.canUseSoldier());
        }

        //Can't play if you've already played a dev card
        for(final Player p : pm.getPlayers()){
            p.addDevCard(new SoldierCard());
            p.setPlayedDevCard(true);
            assertFalse(p.canUseSoldier());
        }

        //Can't play until next turn
        for(final Player p : pm.getPlayers()){
            p.addDevCard(new SoldierCard());
            p.setPlayedDevCard(false);
            assertFalse(p.canUseSoldier());
        }

        //Can play now
        for(final Player p : pm.getPlayers()){
            p.getDevelopmentCardBank().moveNewToOld();
            assertTrue(p.canUseSoldier());
        }
    }

    @Test
    public void testUseSoldier() throws Exception, BadCallerException {
        for(final Player p : pm.getPlayers()){
            p.addDevCard(new SoldierCard());
            p.setPlayedDevCard(false);
            p.getDevelopmentCardBank().moveNewToOld();
            assertTrue(p.canUseSoldier());
            p.useSoldier();
            assertFalse(p.canUseSoldier());
        }
    }

    @Test
    public void testCanUseMonopoly() throws Exception, BadCallerException {
        for(final Player p : pm.getPlayers()){
            assertFalse(p.canUseMonopoly());
        }

        //Can't play if you've already played a dev card
        for(final Player p : pm.getPlayers()){
            p.addDevCard(new MonopolyCard());
            p.setPlayedDevCard(true);
            assertFalse(p.canUseMonopoly());
        }

        //Can't play until next turn
        for(final Player p : pm.getPlayers()){
            p.addDevCard(new MonopolyCard());
            p.setPlayedDevCard(false);
            assertFalse(p.canUseMonopoly());
        }

        //Can play now
        for(final Player p : pm.getPlayers()){
            p.getDevelopmentCardBank().moveNewToOld();
            assertTrue(p.canUseMonopoly());
        }
    }

    @Test
    public void testUseMonopoly() throws Exception, BadCallerException, InvalidTypeException {
        for(final Player p : pm.getPlayers()){
            p.addDevCard(new MonopolyCard());
            p.setPlayedDevCard(false);
            p.getDevelopmentCardBank().moveNewToOld();
            assertTrue(p.canUseMonopoly());
            p.useMonopoly();
            assertFalse(p.canUseMonopoly());
        }
    }

    @Test
    public void testCanUseMonument() throws Exception, BadCallerException {
        for(final Player p : pm.getPlayers()){
            assertFalse(p.canUseMonument());
        }

        //Can't play if you've already played a dev card
        for(final Player p : pm.getPlayers()){
            p.addDevCard(new MonumentCard());
            p.setPlayedDevCard(true);
            assertFalse(p.canUseMonument());
        }

        //Can't play until player can win
        for(final Player p : pm.getPlayers()){
            p.addDevCard(new MonumentCard());
            p.setPlayedDevCard(false);
            assertFalse(p.canUseMonument());
        }

        //Can play now
        for(final Player p : pm.getPlayers()){
            p.incrementPoints(10);
            p.getDevelopmentCardBank().moveNewToOld();
            assertTrue(p.canUseMonument());
        }
    }

    @Test
    public void testUseMonument() throws Exception, BadCallerException {
        for(final Player p : pm.getPlayers()){
            p.addDevCard(new MonumentCard());
            p.setPlayedDevCard(false);
            p.getDevelopmentCardBank().moveNewToOld();
            p.incrementPoints(10);
            assertTrue(p.canUseMonument());
            p.useMonument();
            assertFalse(p.canUseMonument());
        }
    }

    @Test
    public void testCanPlaceRobber() throws Exception {
        for(final Player p : pm.getPlayers()){
            p.setMoveRobber(false);
            assertFalse(p.canPlaceRobber());
            p.setMoveRobber(true);
            assertTrue(p.canPlaceRobber());
        }
    }

    @Test
    public void testPlaceRobber() throws Exception, InvalidTypeException {
        for(final Player p : pm.getPlayers()){
            for(int i = 0; i < 4; i++){
                p.addResourceCard(new Brick());
            }
        }

        for(final Player p : pm.getPlayers()){
            p.setMoveRobber(true);
            assertTrue(p.canPlaceRobber());
            p.placeRobber();
            p.setMoveRobber(false);
            assertFalse(p.canPlaceRobber());
        }
    }

    @Test
    public void testCanBuildRoad() throws Exception {
        // no players should be able to build a road at first
        for(Player p : Facade.getInstance().getGame().getPlayerManager().getPlayers()) {
            assertFalse(p.canBuildRoad());
        }
        // give player 0 the resources to build a road
        Facade.getInstance().getGame().giveResource(new Brick(), 0);
        Facade.getInstance().getGame().giveResource(new Wood(), 0);

        // verify he can build a road
        assertTrue(Facade.getInstance().getGame().getPlayerManager().getPlayerByID(0).canBuildRoad());

        Facade.getInstance().getGame().getPlayerManager().getPlayerByID(0).buildRoad();

        // verify that the resources were consumed
        assertFalse(Facade.getInstance().getGame().getPlayerManager().getPlayerByID(0).canBuildRoad());
    }

    @Test
    public void testBuildRoad() throws Exception {
        for(final Player p : pm.getPlayers()){
            p.addResourceCard(new Brick());
            p.addResourceCard(new Wood());
            assertTrue(p.canBuildRoad());
            p.buildRoad();
            assertFalse(p.canBuildRoad());
        }
    }

    @Test
    public void testCanBuildSettlement() throws Exception {
        // no players should be able to build a settlement at first
        for(final Player p : Facade.getInstance().getGame().getPlayerManager().getPlayers()) {
            assertFalse(p.canBuildSettlement());
        }
        // give player 0 the resources to build a settlement
        Facade.getInstance().getGame().giveResource(new Brick(), 0);
        Facade.getInstance().getGame().giveResource(new Wood(), 0);
        Facade.getInstance().getGame().giveResource(new Sheep(), 0);
        Facade.getInstance().getGame().giveResource(new Wheat(), 0);

        // verify he can build a settlement
        assertTrue(Facade.getInstance().getGame().getPlayerManager().getPlayerByID(0).canBuildSettlement());

        Facade.getInstance().getGame().getPlayerManager().getPlayerByID(0).buildSettlement();

        // verify that the resources were consumed
        assertFalse(Facade.getInstance().getGame().getPlayerManager().getPlayerByID(0).canBuildSettlement());
    }

    @Test
    public void testBuildSettlement() throws Exception {
        for(final Player p : pm.getPlayers()){
            p.addResourceCard(new Brick());
            p.addResourceCard(new Wood());
            p.addResourceCard(new Wheat());
            p.addResourceCard(new Sheep());
            assertTrue(p.canBuildSettlement());
            p.buildSettlement();
            assertFalse(p.canBuildSettlement());
        }
    }

    @Test
    public void testCanBuildCity() throws Exception {
        for(final Player p : pm.getPlayers()){
            assertFalse(pm.canBuildCity(p.getId()));
        }

        for(final Player p : pm.getPlayers()){
            for(int i = 0; i < 3; i++) {
                p.addResourceCard(new Ore());
            }
            for(int i = 0; i < 2; i++) {
                p.addResourceCard(new Wheat());
            }
            if(p.getAvailableSettlements() < 5) {
                assertTrue(p.canBuildCity());
            } else {
                assertFalse(p.canBuildCity());
            }
        }
    }

    @Test
    public void testBuildCity() throws Exception {
        for(final Player p : pm.getPlayers()){
            for(int i = 0; i < 3; i++) {
                p.addResourceCard(new Ore());
            }
            for(int j = 0; j < 2; j++) {
                p.addResourceCard(new Wheat());
            }
            p.buildCity();
            assertFalse(p.canBuildCity());
        }
    }

    @Test
    public void testEquals() throws InvalidNameException, InvalidPlayerException {
        final Player playerOne = new Player(0, CatanColor.BROWN, 1, 0, "Derek");
        final Player playerTwo = new Player(0, CatanColor.BROWN, 1, 0, "Derek");
        final Player playerThree = new Player(0, CatanColor.BROWN, 1, 0, "Rick");
        final Player playerFour = new Player(0, CatanColor.BROWN, 2, 0, "Derek");
        final Player playerFive = new Player(0, CatanColor.RED, 1, 0, "Derek");
        final Player playerSix = new Player(100, CatanColor.BROWN, 1, 0, "Derek");
        final Player playerOneCopy = playerOne;
        final String notAPlayer = "";

        assertFalse(playerOne.equals(null));
        assertFalse(playerOne.equals(notAPlayer));

        assertTrue(playerOne.equals(playerOneCopy));
        assertTrue(playerOne.equals(playerTwo));

        assertFalse(playerOne.equals(playerThree));
        assertFalse(playerOne.equals(playerFour));
        assertFalse(playerOne.equals(playerFive));
        assertFalse(playerOne.equals(playerSix));
    }
}