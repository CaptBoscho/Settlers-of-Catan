package model.game;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import shared.definitions.CatanColor;

import shared.exceptions.*;
import shared.locations.*;
import shared.model.game.Game;
import shared.model.game.TurnTracker;
import shared.model.player.Name;
import shared.model.player.Player;

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

    void testCanDiscardCards() {
        int turn = game.getCurrentTurn();
    }

    void testDiscardCards() {

    }





    void testCanOfferTrade() {

    }

    void testOfferTrade() {

    }

    void testCanFinishTurn() {

    }

    void testFinishTurn() {

    }

    void testCanBuyDevCard() {

    }

    void testBuyDevCard() {

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
