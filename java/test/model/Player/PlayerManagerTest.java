package model.Player;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import shared.definitions.CatanColor;
import shared.definitions.ResourceType;
import shared.exceptions.PlayerExistsException;
import shared.model.cards.Card;
import shared.model.cards.resources.Brick;
import shared.model.cards.resources.ResourceCard;
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
    public void testDiscardCards() throws Exception {
        List<Card> cards = new ArrayList<ResourceCard>();

        for(Player p : pm.getPlayers()){
            for(int i = 0; i < 9; i++){
                p.addResourceCard(new Brick());
            }
            p.discardCards();
        }
    }

    @Test
    public void testCanOfferTrade() throws Exception {

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

    }

    @Test
    public void testUseSoldier() throws Exception {

    }

    @Test
    public void testCanUseMonopoly() throws Exception {

    }

    @Test
    public void testUseMonopoly() throws Exception {

    }

    @Test
    public void testCanUseMonument() throws Exception {

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