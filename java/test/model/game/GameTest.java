package model.game;

import org.apache.http.client.cache.Resource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import shared.definitions.CatanColor;

import shared.definitions.ResourceType;
import shared.exceptions.*;
import shared.locations.*;
import shared.model.bank.InvalidTypeException;
import shared.model.cards.resources.ResourceCard;
import shared.model.game.Game;
import shared.model.game.TurnTracker;
import shared.model.player.Name;
import shared.model.player.Player;

import javax.naming.InsufficientResourcesException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Corbin Byers
 */
public class GameTest {

    private Game game = new Game();

    @Before
    public void testInitializeGame() throws InvalidNameException, InvalidPlayerException, FailedToRandomizeException{
        List<Player> players = new ArrayList<Player>();


        Player one = new Player(0, CatanColor.BLUE, 1, new Name ("Hope"));
        Player two = new Player(0, CatanColor.BROWN, 2, new Name("Corbin"));
        Player three = new Player(0, CatanColor.GREEN, 3, new Name("Hanna"));
        Player four = new Player(0, CatanColor.ORANGE, 4, new Name("Becca"));

        players.add(one);
        players.add(two);
        players.add(three);
        players.add(four);

        int first = game.initializeGame(players, true, true, false);


        assertTrue(first > 0 && first <= 4);

    }

    @Test
    public void testInitialization() throws InvalidPlayerException, InvalidLocationException, Exception{
        int current_turn = game.getCurrentTurn();
        HexLocation hloc = new HexLocation(0,0);
        VertexLocation vloc = new VertexLocation(hloc, VertexDirection.East);
        EdgeLocation eloc = new EdgeLocation(hloc, EdgeDirection.NorthEast);


        assertTrue(game.canInitiateSettlement(current_turn,vloc));
        game.initiateSettlement(current_turn,vloc);
        assertTrue(game.canInitiateRoad(current_turn,vloc,eloc));
        assertFalse(game.canInitiateRoad(current_turn,vloc,new EdgeLocation(hloc, EdgeDirection.SouthWest)));
        game.initiateRoad(current_turn,vloc,eloc);

        HexLocation hloc2 = new HexLocation(8,8);
        VertexLocation vloc2 = new VertexLocation(hloc2, VertexDirection.East);

        int next = game.getTurnTracker().nextTurn();

        assertFalse(game.canInitiateSettlement(next, vloc));
    }

    @Test
    public void testGetCurrentTurnStartUp() throws Exception{
        int first = game.getCurrentTurn();
        assertTrue(first == 1);
        int second = game.getTurnTracker().nextTurn();
        assertTrue(second == 2);
        int third = game.getTurnTracker().nextTurn();
        System.out.println(third);
        assertTrue(third == 3);
        int fourth = game.getTurnTracker().nextTurn();
        assertTrue(fourth == 4);
        int fifth = game.getTurnTracker().nextTurn();
        assertTrue(fifth == 3);

        game.getTurnTracker().setSetupPhase(false);
        int sixth = game.getTurnTracker().nextTurn();
        assertTrue(sixth == 4);
        int seventh = game.getTurnTracker().nextTurn();
        assertTrue(seventh == 1);
    }

    @Test
    public void testCanRollNumber() {
        int turn = game.getCurrentTurn();
        assertTrue(game.canRollNumber(turn));
        game.getTurnTracker().nextPhase();
        assertFalse(game.canRollNumber(turn));
    }

    @Test
    public void testRollNumber() throws InvalidDiceRollException{
        int turn = game.getCurrentTurn();
        game.getTurnTracker().nextPhase();
        game.getTurnTracker().nextPhase();
        int roll = game.rollNumber(turn);
        assertTrue(roll > 1);
        assertTrue(roll <= 12);
    }

    @Test
    public void testCanOfferTrade() {
        int guy = game.getCurrentTurn();
        game.getTurnTracker().nextPhase();
        assertTrue(game.canOfferTrade(guy));
    }

    @Test
    public void testOfferTrade() throws InsufficientResourcesException, InvalidTypeException, PlayerExistsException {
        int guy = game.getCurrentTurn();
        game.getTurnTracker().nextPhase();
        System.out.println(game.getTurnTracker().getPhase());
        int friend = 3;
        if(guy == 3){friend = 4;}
        ResourceCard one = game.getResourceCard(ResourceType.BRICK);
        ResourceCard two = game.getResourceCard(ResourceType.ORE);
        ResourceCard three = game.getResourceCard(ResourceType.SHEEP);

        game.giveResource(one, guy);
        game.giveResource(two, guy);
        game.giveResource(three, friend);

        List<ResourceType> ones = new ArrayList<ResourceType>();
        List<ResourceType> twos = new ArrayList<ResourceType>();
        ones.add(ResourceType.BRICK);
        ones.add(ResourceType.ORE);
        twos.add(ResourceType.SHEEP);

        assertTrue(game.amountOwnedResource(guy, ResourceType.BRICK) == 1);
        assertTrue(game.amountOwnedResource(guy, ResourceType.ORE) == 1);
        assertTrue(game.amountOwnedResource(friend, ResourceType.SHEEP) == 1);

        game.offerTrade(guy,friend,ones,twos);

        assertTrue(game.amountOwnedResource(friend, ResourceType.BRICK) == 1);
        assertTrue(game.amountOwnedResource(friend, ResourceType.ORE) == 1);
        assertTrue(game.amountOwnedResource(guy, ResourceType.SHEEP) == 1);
        assertTrue(game.amountOwnedResource(guy, ResourceType.BRICK) == 0);
    }

    @Test
    public void testCanFinishTurn() {
        int guy = game.getCurrentTurn();
        game.getTurnTracker().nextPhase();
        game.getTurnTracker().nextPhase();
        System.out.println(game.getTurnTracker().getPhase());
        assertTrue(game.canFinishTurn(guy));
    }

    @Test
    public void testFinishTurn() {
        int guy = game.getCurrentTurn();
        game.nextPhase();
        TurnTracker.Phase p = game.getCurrentPhase();

        System.out.println(p);
        if(p == TurnTracker.Phase.DISCARDING){
            assertTrue(game.canFinishTurn(guy));
        }
        else{
            assertFalse(game.canFinishTurn(guy));
        }

        assertFalse(game.canFinishTurn(guy-1));
    }

    @Test
    public void testCanBuyDevCard() throws InsufficientResourcesException, InvalidTypeException, PlayerExistsException{
        int guy = game.getCurrentTurn();
        game.setPhase(TurnTracker.Phase.DISCARDING);
        assertFalse(game.canBuyDevCard(guy));

        ResourceCard one = game.getResourceCard(ResourceType.WHEAT);
        ResourceCard two = game.getResourceCard(ResourceType.ORE);
        ResourceCard three = game.getResourceCard(ResourceType.SHEEP);

        game.giveResource(one, guy);
        game.giveResource(two, guy);
        game.giveResource(three, guy);

        assertTrue(game.canBuyDevCard(guy));
    }

    @Test
    public void testBuyDevCard() throws InsufficientResourcesException, InvalidTypeException, PlayerExistsException{
        int guy = game.getCurrentTurn();
        game.setPhase(TurnTracker.Phase.DISCARDING);

        ResourceCard one = game.getResourceCard(ResourceType.WHEAT);
        ResourceCard two = game.getResourceCard(ResourceType.ORE);
        ResourceCard three = game.getResourceCard(ResourceType.SHEEP);

        game.giveResource(one, guy);
        game.giveResource(two, guy);
        game.giveResource(three, guy);

        game.buyDevCard(guy);

        Player p = game.getPlayerManager().getPlayerByID(guy);
        


    }

    void testCanUseYearOfPlenty() {

    }

    void testUseYearOfPlenty() {

    }

    void testCanUseRoadBuilder() {

    }

    void testUseRoadBuilder() {

    }

    void testCanUseSoldier() {

    }

    void testUseSoldier() {

    }

    void testCanUseMonopoly() {

    }

    void testUseMonopoly() {

    }

    void testCanUseMonument() {

    }

    void testUseMonument() {

    }

    void testCanPlaceRobber() {

    }

    void testPlaceRobber() {

    }

    void testRob() {

    }

    void testCanBuildRoad() {

    }

    void testBuildRoad() {

    }

    void testCanBuildSettlement() {

    }

    void testBuildSettlement() {

    }

    void testCanBuildCity() {

    }

    void testBuildCity() {

    }

    void testCurrentLongestRoadSize() {

    }

    void testCurrentLongestRoadPlayer() {

    }

    void testNewLongestRoad() {

    }

    void testCanBuyDevelopmentCard() {

    }

    void testBuyDevelopmentCard() {

    }

    void testCanTrade() {

    }

    void testCanMaritimeTrade() {

    }

    void testMaritimeTrade() {

    }

    void testGetPortTypes() {

    }
}
