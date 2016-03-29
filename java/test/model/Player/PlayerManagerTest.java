package model.Player;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import shared.definitions.CatanColor;
import shared.definitions.PortType;
import shared.definitions.ResourceType;
import shared.exceptions.BadCallerException;
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
public class PlayerManagerTest {
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
        }catch(Exception e){
            fail();
        }

        pm = new PlayerManager(players);
    }

    @After
    public void tearDown() throws Exception {
        pm = null;
    }

    @Test
    public void testGetPlayerByID() throws Exception {
        final Player p1 = pm.getPlayerByIndex(0);
        assertEquals(p1, pm.getPlayerByID(p1.getId()));

        final Player p2 = pm.getPlayerByIndex(1);
        assertEquals(p2, pm.getPlayerByID(p2.getId()));

        final Player p3 = pm.getPlayerByIndex(2);
        assertEquals(p3, pm.getPlayerByID(p3.getId()));

        final Player p4 = pm.getPlayerByIndex(3);
        assertEquals(p4, pm.getPlayerByID(p4.getId()));
    }

    @Test
    public void testCanDiscardCards() throws Exception {
        for(final Player p : pm.getPlayers()){
            assertFalse(pm.canDiscardCards(p.getPlayerIndex()));
        }

        for(final Player p : pm.getPlayers()){
            for(int i = 0; i < 9; i++){
                p.addResourceCard(new Brick());
            }
            assertTrue(pm.canDiscardCards(p.getPlayerIndex()));
        }
    }

    @Test
    public void testDiscardCards() throws Exception, InvalidTypeException {
        final List<Card> cards = new ArrayList<>();
        for(int j = 0; j < 4; j++){
            cards.add(new Brick());
        }

        for(final Player p : pm.getPlayers()){
            for(int i = 0; i < 8; i++){
                p.addResourceCard(new Brick());
            }
            pm.discardCards(p.getPlayerIndex(), cards);
            assertFalse(pm.canDiscardCards(p.getPlayerIndex()));
        }
    }

    @Test
    public void testCanOfferTrade() throws Exception {
        for(final Player p : pm.getPlayers()){
            assertFalse(pm.canOfferTrade(p.getPlayerIndex()));
        }

        for(final Player p : pm.getPlayers()){
            for(int i = 0; i < 8; i++){
                p.addResourceCard(new Brick());
            }
            assertTrue(pm.canOfferTrade(p.getPlayerIndex()));
        }
    }

    @Test
    public void testCanMaritimeTrade() throws Exception {
        for(final Player p : pm.getPlayers()){
            assertFalse(pm.canMaritimeTrade(p.getPlayerIndex(), PortType.BRICK));
        }

        for(final Player p : pm.getPlayers()){
            for(int i = 0; i < 8; i++){
                p.addResourceCard(new Brick());
            }
            assertTrue(pm.canMaritimeTrade(p.getPlayerIndex(), PortType.BRICK));
            assertFalse(pm.canMaritimeTrade(p.getPlayerIndex(), PortType.ORE));
        }
    }

    @Test
    public void testCanBuyDevCard() throws Exception {
        for(final Player p : pm.getPlayers()){
            assertFalse(pm.canBuyDevCard(p.getPlayerIndex()));
        }

        for(final Player p : pm.getPlayers()){
            for(int i = 0; i < 8; i++){
                p.addResourceCard(new Sheep());
                p.addResourceCard(new Ore());
                p.addResourceCard(new Wheat());
            }
            assertTrue(pm.canBuyDevCard(p.getPlayerIndex()));
        }
    }

    @Test
    public void testBuyDevCard() throws Exception {
        for(final Player p : pm.getPlayers()){
            p.addResourceCard(new Sheep());
            p.addResourceCard(new Ore());
            p.addResourceCard(new Wheat());

            assertTrue(pm.canBuyDevCard(p.getPlayerIndex()));
            pm.buyDevCard(p.getPlayerIndex());
            assertFalse(pm.canBuyDevCard(p.getPlayerIndex()));
        }
    }

    @Test
    public void testCanUseYearOfPlenty() throws Exception, BadCallerException {
        for(final Player p : pm.getPlayers()){
            assertFalse(pm.canUseYearOfPlenty(p.getPlayerIndex()));
        }

        //Can't play if you've already played a dev card
        for(final Player p : pm.getPlayers()){
            p.addDevCard(new YearOfPlentyCard());
            p.setPlayedDevCard(true);
            assertFalse(pm.canUseYearOfPlenty(p.getPlayerIndex()));
        }

        //Can't play until next turn
        for(final Player p : pm.getPlayers()){
            p.addDevCard(new YearOfPlentyCard());
            p.setPlayedDevCard(false);
            assertFalse(pm.canUseYearOfPlenty(p.getPlayerIndex()));
        }

        //Can play now
        for(final Player p : pm.getPlayers()){
            p.getDevelopmentCardBank().moveNewToOld();
            assertTrue(pm.canUseYearOfPlenty(p.getPlayerIndex()));
        }
    }

    @Test
    public void testUseYearOfPlenty() throws Exception, BadCallerException {
        for(final Player p : pm.getPlayers()){
            p.addDevCard(new YearOfPlentyCard());
            p.setPlayedDevCard(false);
            p.getDevelopmentCardBank().moveNewToOld();
            pm.useYearOfPlenty(p.getPlayerIndex());
            assertFalse(pm.canUseYearOfPlenty(p.getPlayerIndex()));
        }
    }

    @Test
    public void testCanUseRoadBuilder() throws Exception, BadCallerException {
        for(final Player p : pm.getPlayers()){
            assertFalse(pm.canUseRoadBuilder(p.getPlayerIndex()));
        }

        //Can't play if you've already played a dev card
        for(final Player p : pm.getPlayers()){
            p.addDevCard(new RoadBuildCard());
            p.setPlayedDevCard(true);
            assertFalse(pm.canUseRoadBuilder(p.getPlayerIndex()));
        }

        //Can't play until next turn
        for(final Player p : pm.getPlayers()){
            p.addDevCard(new RoadBuildCard());
            p.setPlayedDevCard(false);
            assertFalse(pm.canUseRoadBuilder(p.getPlayerIndex()));
        }

        //Can play now
        for(final Player p : pm.getPlayers()){
            p.getDevelopmentCardBank().moveNewToOld();
            assertTrue(pm.canUseRoadBuilder(p.getPlayerIndex()));
        }
    }

    @Test
    public void testUseRoadBuilder() throws Exception, BadCallerException {
        for(final Player p : pm.getPlayers()){
            p.addDevCard(new RoadBuildCard());
            p.setPlayedDevCard(false);
            p.getDevelopmentCardBank().moveNewToOld();
            pm.useRoadBuilder(p.getPlayerIndex());
            assertFalse(pm.canUseRoadBuilder(p.getPlayerIndex()));
        }
    }

    @Test
    public void testCanUseSoldier() throws Exception, BadCallerException {
        for(final Player p : pm.getPlayers()){
            assertFalse(pm.canUseSoldier(p.getPlayerIndex()));
        }

        //Can't play if you've already played a dev card
        for(final Player p : pm.getPlayers()){
            p.addDevCard(new SoldierCard());
            p.setPlayedDevCard(true);
            assertFalse(pm.canUseSoldier(p.getPlayerIndex()));
        }

        //Can't play until next turn
        for(final Player p : pm.getPlayers()){
            p.addDevCard(new SoldierCard());
            p.setPlayedDevCard(false);
            assertFalse(pm.canUseSoldier(p.getPlayerIndex()));
        }

        //Can play now
        for(final Player p : pm.getPlayers()){
            p.getDevelopmentCardBank().moveNewToOld();
            assertTrue(pm.canUseSoldier(p.getPlayerIndex()));
        }
    }

    @Test
    public void testUseSoldier() throws Exception, BadCallerException {
        for(final Player p : pm.getPlayers()){
            p.addDevCard(new SoldierCard());
            p.setPlayedDevCard(false);
            p.getDevelopmentCardBank().moveNewToOld();
            pm.useSoldier(p.getPlayerIndex());
            assertFalse(pm.canUseSoldier(p.getPlayerIndex()));
        }
    }

    @Test
    public void testCanUseMonopoly() throws Exception, BadCallerException {
        for(final Player p : pm.getPlayers()){
            assertFalse(pm.canUseMonopoly(p.getPlayerIndex()));
        }

        //Can't play if you've already played a dev card
        for(final Player p : pm.getPlayers()){
            p.addDevCard(new MonopolyCard());
            p.setPlayedDevCard(true);
            assertFalse(pm.canUseMonopoly(p.getPlayerIndex()));
        }

        //Can't play until next turn
        for(final Player p : pm.getPlayers()){
            p.addDevCard(new MonopolyCard());
            p.setPlayedDevCard(false);
            assertFalse(pm.canUseMonopoly(p.getPlayerIndex()));
        }

        //Can play now
        for(final Player p : pm.getPlayers()){
            p.getDevelopmentCardBank().moveNewToOld();
            assertTrue(pm.canUseMonopoly(p.getPlayerIndex()));
        }
    }

    @Test
    public void testUseMonopoly() throws Exception, BadCallerException, InvalidTypeException {
        for(final Player p : pm.getPlayers()){
            p.addDevCard(new MonopolyCard());
            p.setPlayedDevCard(false);
            p.getDevelopmentCardBank().moveNewToOld();
            pm.useMonopoly(p.getPlayerIndex(), ResourceType.BRICK);
            assertFalse(pm.canUseMonopoly(p.getPlayerIndex()));
        }
    }

    @Test
    public void testCanUseMonument() throws Exception, BadCallerException {
        for(final Player p : pm.getPlayers()){
            assertFalse(pm.canUseMonument(p.getPlayerIndex()));
        }

        //Can't play if you've already played a dev card
        for(final Player p : pm.getPlayers()){
            p.addDevCard(new MonumentCard());
            p.setPlayedDevCard(true);
            assertFalse(pm.canUseMonument(p.getPlayerIndex()));
        }

        //Can't play until player can win
        for(final Player p : pm.getPlayers()){
            p.addDevCard(new MonumentCard());
            p.setPlayedDevCard(false);
            assertFalse(pm.canUseMonument(p.getPlayerIndex()));
        }

        //Can play now
        for(final Player p : pm.getPlayers()){
            p.incrementPoints(10);
            p.getDevelopmentCardBank().moveNewToOld();
            assertTrue(pm.canUseMonument(p.getPlayerIndex()));
        }
    }

    @Test
    public void testUseMonument() throws Exception, BadCallerException {
        for(final Player p : pm.getPlayers()){
            p.addDevCard(new MonumentCard());
            p.setPlayedDevCard(false);
            p.getDevelopmentCardBank().moveNewToOld();
            p.incrementPoints(10);
            pm.useMonument(p.getPlayerIndex());
            assertFalse(pm.canUseMonument(p.getPlayerIndex()));
        }
    }

    @Test
    public void testCanPlaceRobber() throws Exception {
        for(final Player p : pm.getPlayers()){
            p.setMoveRobber(false);
            assertFalse(pm.canPlaceRobber(p.getPlayerIndex()));
            p.setMoveRobber(true);
            assertTrue(pm.canPlaceRobber(p.getPlayerIndex()));
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
            pm.placeRobber(p.getPlayerIndex(), 1);
            p.setMoveRobber(false);
            assertFalse(pm.canPlaceRobber(p.getPlayerIndex()));
        }
    }

    @Test
    public void testCanBuildRoad() throws Exception {
        for(final Player p : pm.getPlayers()){
            assertFalse(pm.canBuildRoad(p.getPlayerIndex()));
        }

        for(final Player p : pm.getPlayers()){
            p.addResourceCard(new Brick());
            p.addResourceCard(new Wood());
            assertTrue(pm.canBuildRoad(p.getPlayerIndex()));
        }
    }

    @Test
    public void testBuildRoad() throws Exception {
        for(final Player p : pm.getPlayers()){
            p.addResourceCard(new Brick());
            p.addResourceCard(new Wood());
            pm.buildRoad(p.getPlayerIndex());
            assertFalse(pm.canBuildRoad(p.getPlayerIndex()));
        }
    }

    @Test
    public void testCanBuildSettlement() throws Exception {
        for(final Player p : pm.getPlayers()){
            assertFalse(pm.canBuildSettlement(p.getPlayerIndex()));
        }

        for(final Player p : pm.getPlayers()){
            p.addResourceCard(new Brick());
            p.addResourceCard(new Wood());
            p.addResourceCard(new Wheat());
            p.addResourceCard(new Sheep());
            assertTrue(pm.canBuildSettlement(p.getPlayerIndex()));
        }
    }

    @Test
    public void testBuildSettlement() throws Exception {
        for(final Player p : pm.getPlayers()){
            p.addResourceCard(new Brick());
            p.addResourceCard(new Wood());
            p.addResourceCard(new Wheat());
            p.addResourceCard(new Sheep());
            pm.buildSettlement(p.getPlayerIndex());
            assertFalse(pm.canBuildSettlement(p.getPlayerIndex()));
        }
    }

    @Test
    public void testCanBuildCity() throws Exception {
        for(final Player p : pm.getPlayers()){
            assertFalse(pm.canBuildCity(p.getPlayerIndex()));
        }

        for(final Player p : pm.getPlayers()){
            for(int i = 0; i < 3; i++) {
                p.addResourceCard(new Ore());
            }
            for(int i = 0; i < 2; i++) {
                p.addResourceCard(new Wheat());
            }
            if(pm.getAvailableSettlements(p.getPlayerIndex()) < 5) {
                assertTrue(pm.canBuildCity(p.getPlayerIndex()));
            } else {
                assertFalse(pm.canBuildCity(p.getPlayerIndex()));
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
            pm.buildCity(p.getPlayerIndex());
            assertFalse(pm.canBuildCity(p.getPlayerIndex()));
        }
    }
}