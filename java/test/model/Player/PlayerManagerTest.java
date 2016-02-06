package model.Player;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import shared.definitions.CatanColor;
import shared.definitions.PortType;
import shared.definitions.ResourceType;
import shared.exceptions.BadCallerException;
import shared.exceptions.PlayerExistsException;
import shared.model.bank.InvalidTypeException;
import shared.model.cards.Card;
import shared.model.cards.devcards.*;
import shared.model.cards.resources.*;
import shared.model.player.Name;
import shared.model.player.Player;
import shared.model.player.PlayerManager;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by corne on 2/5/2016.
 */
public class PlayerManagerTest {
    private PlayerManager pm;

    @Before
    public void setUp() {
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
    }

    @After
    public void tearDown() throws Exception {
        pm = null;
    }

    @Test
    public void testRandomizePlayers() throws Exception {
        List<Player> players = new ArrayList<Player>(pm.getPlayers());

        int counter = 0;
        while(!wasShuffled(players)){
            pm.randomizePlayers();
            counter++;

            //Just making sure we don't loop forever
            if(counter == 9)
                break;
        }

        //Make sure we didn't just exit because it wasn't shuffling
        assertTrue(counter != 9);
    }

    @Test
    public void testGetPlayerByID() throws Exception {
        Player p1 = pm.getPlayerByIndex(0);
        assertEquals(p1,pm.getPlayerByID(p1.get_id()));

        Player p2 = pm.getPlayerByIndex(1);
        assertEquals(p2,pm.getPlayerByID(p2.get_id()));

        Player p3 = pm.getPlayerByIndex(2);
        assertEquals(p3,pm.getPlayerByID(p3.get_id()));

        Player p4 = pm.getPlayerByIndex(3);
        assertEquals(p4,pm.getPlayerByID(p4.get_id()));
    }

    @Test
    public void testCanDiscardCards() throws Exception {
        for(Player p : pm.getPlayers()){
            assertFalse(pm.canDiscardCards(p.get_id()));
        }

        for(Player p : pm.getPlayers()){
            for(int i = 0; i < 9; i++){
                p.addResourceCard(new Brick());
            }
            assertTrue(pm.canDiscardCards(p.get_id()));
        }
    }

    @Test
    public void testDiscardCards() throws Exception, InvalidTypeException {
        List<Card> cards = new ArrayList<Card>();
        for(int j = 0; j < 4; j++){
            cards.add(new Brick());
        }

        for(Player p : pm.getPlayers()){
            for(int i = 0; i < 8; i++){
                p.addResourceCard(new Brick());
            }
            pm.discardCards(p.get_id(), cards);
            assertFalse(pm.canDiscardCards(p.get_id()));
        }
    }

    @Test
    public void testCanOfferTrade() throws Exception {
        for(Player p : pm.getPlayers()){
            assertFalse(pm.canOfferTrade(p.get_id()));
        }

        for(Player p : pm.getPlayers()){
            for(int i = 0; i < 8; i++){
                p.addResourceCard(new Brick());
            }
            assertTrue(pm.canOfferTrade(p.get_id()));
        }
    }

    @Test
    public void testCanMaritimeTrade() throws Exception {
        for(Player p : pm.getPlayers()){
            assertFalse(pm.canMaritimeTrade(p.get_id(), PortType.BRICK));
        }

        for(Player p : pm.getPlayers()){
            for(int i = 0; i < 8; i++){
                p.addResourceCard(new Brick());
            }
            assertTrue(pm.canMaritimeTrade(p.get_id(), PortType.BRICK));
            assertFalse(pm.canMaritimeTrade(p.get_id(), PortType.ORE));
        }
    }

    @Test
    public void testCanBuyDevCard() throws Exception {
        for(Player p : pm.getPlayers()){
            assertFalse(pm.canBuyDevCard(p.get_id()));
        }

        for(Player p : pm.getPlayers()){
            for(int i = 0; i < 8; i++){
                p.addResourceCard(new Sheep());
                p.addResourceCard(new Ore());
                p.addResourceCard(new Wheat());
            }
            assertTrue(pm.canBuyDevCard(p.get_id()));
        }
    }

    @Test
    public void testBuyDevCard() throws Exception {
        for(Player p : pm.getPlayers()){
            p.addResourceCard(new Sheep());
            p.addResourceCard(new Ore());
            p.addResourceCard(new Wheat());

            assertTrue(pm.canBuyDevCard(p.get_id()));
            pm.buyDevCard(p.get_id());
            assertFalse(pm.canBuyDevCard(p.get_id()));
        }
    }

    @Test
    public void testCanUseYearOfPlenty() throws Exception, BadCallerException {
        for(Player p : pm.getPlayers()){
            assertFalse(pm.canUseYearOfPlenty(p.get_id()));
        }

        //Can't play if you've already played a dev card
        for(Player p : pm.getPlayers()){
            p.addDevCard(new YearOfPlentyCard());
            p.setPlayedDevCard(true);
            assertFalse(pm.canUseYearOfPlenty(p.get_id()));
        }

        //Can't play until next turn
        for(Player p : pm.getPlayers()){
            p.addDevCard(new YearOfPlentyCard());
            p.setPlayedDevCard(false);
            assertFalse(pm.canUseYearOfPlenty(p.get_id()));
        }

        //Can play now
        for(Player p : pm.getPlayers()){
            p.getDevelopmentCardBank().moveNewToOld();
            assertTrue(pm.canUseYearOfPlenty(p.get_id()));
        }
    }

    @Test
    public void testUseYearOfPlenty() throws Exception, BadCallerException {
        for(Player p : pm.getPlayers()){
            p.addDevCard(new YearOfPlentyCard());
            p.setPlayedDevCard(false);
            p.getDevelopmentCardBank().moveNewToOld();
            pm.useYearOfPlenty(p.get_id());
            assertFalse(pm.canUseYearOfPlenty(p.get_id()));
        }
    }

    @Test
    public void testCanUseRoadBuilder() throws Exception, BadCallerException {
        for(Player p : pm.getPlayers()){
            assertFalse(pm.canUseRoadBuilder(p.get_id()));
        }

        //Can't play if you've already played a dev card
        for(Player p : pm.getPlayers()){
            p.addDevCard(new RoadBuildCard());
            p.setPlayedDevCard(true);
            assertFalse(pm.canUseRoadBuilder(p.get_id()));
        }

        //Can't play until next turn
        for(Player p : pm.getPlayers()){
            p.addDevCard(new RoadBuildCard());
            p.setPlayedDevCard(false);
            assertFalse(pm.canUseRoadBuilder(p.get_id()));
        }

        //Can play now
        for(Player p : pm.getPlayers()){
            p.getDevelopmentCardBank().moveNewToOld();
            assertTrue(pm.canUseRoadBuilder(p.get_id()));
        }
    }

    @Test
    public void testUseRoadBuilder() throws Exception, BadCallerException {
        for(Player p : pm.getPlayers()){
            p.addDevCard(new RoadBuildCard());
            p.setPlayedDevCard(false);
            p.getDevelopmentCardBank().moveNewToOld();
            pm.useRoadBuilder(p.get_id());
            assertFalse(pm.canUseRoadBuilder(p.get_id()));
        }
    }

    @Test
    public void testCanUseSoldier() throws Exception, BadCallerException {
        for(Player p : pm.getPlayers()){
            assertFalse(pm.canUseSoldier(p.get_id()));
        }

        //Can't play if you've already played a dev card
        for(Player p : pm.getPlayers()){
            p.addDevCard(new SoldierCard());
            p.setPlayedDevCard(true);
            assertFalse(pm.canUseSoldier(p.get_id()));
        }

        //Can't play until next turn
        for(Player p : pm.getPlayers()){
            p.addDevCard(new SoldierCard());
            p.setPlayedDevCard(false);
            assertFalse(pm.canUseSoldier(p.get_id()));
        }

        //Can play now
        for(Player p : pm.getPlayers()){
            p.getDevelopmentCardBank().moveNewToOld();
            assertTrue(pm.canUseSoldier(p.get_id()));
        }
    }

    @Test
    public void testUseSoldier() throws Exception, BadCallerException {
        for(Player p : pm.getPlayers()){
            p.addDevCard(new SoldierCard());
            p.setPlayedDevCard(false);
            p.getDevelopmentCardBank().moveNewToOld();
            pm.useSoldier(p.get_id());
            assertFalse(pm.canUseSoldier(p.get_id()));
        }
    }

    @Test
    public void testCanUseMonopoly() throws Exception, BadCallerException {
        for(Player p : pm.getPlayers()){
            assertFalse(pm.canUseMonopoly(p.get_id()));
        }

        //Can't play if you've already played a dev card
        for(Player p : pm.getPlayers()){
            p.addDevCard(new MonopolyCard());
            p.setPlayedDevCard(true);
            assertFalse(pm.canUseMonopoly(p.get_id()));
        }

        //Can't play until next turn
        for(Player p : pm.getPlayers()){
            p.addDevCard(new MonopolyCard());
            p.setPlayedDevCard(false);
            assertFalse(pm.canUseMonopoly(p.get_id()));
        }

        //Can play now
        for(Player p : pm.getPlayers()){
            p.getDevelopmentCardBank().moveNewToOld();
            assertTrue(pm.canUseMonopoly(p.get_id()));
        }
    }

    @Test
    public void testUseMonopoly() throws Exception, BadCallerException, InvalidTypeException {
        for(Player p : pm.getPlayers()){
            p.addDevCard(new MonopolyCard());
            p.setPlayedDevCard(false);
            p.getDevelopmentCardBank().moveNewToOld();
            pm.useMonopoly(p.get_id(), 3, ResourceType.BRICK);
            assertFalse(pm.canUseMonopoly(p.get_id()));
        }
    }

    @Test
    public void testCanUseMonument() throws Exception, BadCallerException {
        for(Player p : pm.getPlayers()){
            assertFalse(pm.canUseMonument(p.get_id()));
        }

        //Can't play if you've already played a dev card
        for(Player p : pm.getPlayers()){
            p.addDevCard(new MonumentCard());
            p.setPlayedDevCard(true);
            assertFalse(pm.canUseMonument(p.get_id()));
        }

        //Can't play until next turn
        for(Player p : pm.getPlayers()){
            p.addDevCard(new MonumentCard());
            p.setPlayedDevCard(false);
            assertFalse(pm.canUseMonument(p.get_id()));
        }

        //Can play now
        for(Player p : pm.getPlayers()){
            p.getDevelopmentCardBank().moveNewToOld();
            assertTrue(pm.canUseMonument(p.get_id()));
        }
    }

    @Test
    public void testUseMonument() throws Exception, BadCallerException {
        for(Player p : pm.getPlayers()){
            p.addDevCard(new MonumentCard());
            p.setPlayedDevCard(false);
            p.getDevelopmentCardBank().moveNewToOld();
            pm.useMonument(p.get_id());
            assertFalse(pm.canUseMonument(p.get_id()));
        }
    }

    @Test
    public void testCanPlaceRobber() throws Exception {
        for(Player p : pm.getPlayers()){
            p.setMoveRobber(false);
            assertFalse(pm.canPlaceRobber(p.get_id()));
            p.setMoveRobber(true);
            assertTrue(pm.canPlaceRobber(p.get_id()));
        }
    }

    @Test
    public void testPlaceRobber() throws Exception, InvalidTypeException {
        for(Player p : pm.getPlayers()){
            for(int i = 0; i < 4; i++){
                p.addResourceCard(new Brick());
            }
        }

        for(Player p : pm.getPlayers()){
            p.setMoveRobber(true);
            pm.placeRobber(p.get_id(), 1);
            assertFalse(pm.canPlaceRobber(p.get_id()));
        }
    }

    @Test
    public void testCanBuildRoad() throws Exception {
        for(Player p : pm.getPlayers()){
            assertFalse(pm.canBuildRoad(p.get_id()));
        }

        for(Player p : pm.getPlayers()){
            p.addResourceCard(new Brick());
            p.addResourceCard(new Wood());
            assertTrue(pm.canBuildRoad(p.get_id()));
        }
    }

    @Test
    public void testBuildRoad() throws Exception {
        for(Player p : pm.getPlayers()){
            p.addResourceCard(new Brick());
            p.addResourceCard(new Wood());
            pm.buildRoad(p.get_id());
            assertFalse(pm.canBuildRoad(p.get_id()));
        }
    }

    @Test
    public void testCanBuildSettlement() throws Exception {
        for(Player p : pm.getPlayers()){
            assertFalse(pm.canBuildSettlement(p.get_id()));
        }

        for(Player p : pm.getPlayers()){
            p.addResourceCard(new Brick());
            p.addResourceCard(new Wood());
            p.addResourceCard(new Wheat());
            p.addResourceCard(new Sheep());
            assertTrue(pm.canBuildSettlement(p.get_id()));
        }
    }

    @Test
    public void testBuildSettlement() throws Exception {
        for(Player p : pm.getPlayers()){
            p.addResourceCard(new Brick());
            p.addResourceCard(new Wood());
            p.addResourceCard(new Wheat());
            p.addResourceCard(new Sheep());
            pm.buildSettlement(p.get_id());
            assertFalse(pm.canBuildSettlement(p.get_id()));
        }
    }

    @Test
    public void testCanBuildCity() throws Exception {
        for(Player p : pm.getPlayers()){
            assertFalse(pm.canBuildCity(p.get_id()));
        }

        for(Player p : pm.getPlayers()){
            for(int i = 0; i < 3; i++) {
                p.addResourceCard(new Ore());
            }
            for(int i = 0; i < 2; i++) {
                p.addResourceCard(new Wheat());
            }
            assertTrue(pm.canBuildCity(p.get_id()));
        }
    }

    @Test
    public void testBuildCity() throws Exception {
        for(Player p : pm.getPlayers()){
            for(int i = 0; i < 3; i++) {
                p.addResourceCard(new Ore());
            }
            for(int j = 0; j < 2; j++) {
                p.addResourceCard(new Wheat());
            }
            pm.buildCity(p.get_id());
            assertFalse(pm.canBuildCity(p.get_id()));
        }
    }

    //Test Helper Methods
    private boolean wasShuffled(List<Player> players){
        try{
            return (players.get(0) != pm.getPlayerByIndex(0) || players.get(1) != pm.getPlayerByIndex(1)
                    || players.get(2) != pm.getPlayerByIndex(2) || players.get(3) != pm.getPlayerByIndex(3));
        }catch(PlayerExistsException e){
            return false;
        }
    }
}