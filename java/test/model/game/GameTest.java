package model.game;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import shared.definitions.CatanColor;
<<<<<<< HEAD
import shared.exceptions.FailedToRandomizeException;
import shared.exceptions.InvalidNameException;
import shared.exceptions.InvalidPlayerException;
=======

import shared.exceptions.*;
import shared.locations.*;
>>>>>>> refs/remotes/origin/master
import shared.model.game.Game;
import shared.model.game.TurnTracker;
import shared.model.player.Name;
import shared.model.player.Player;

import java.util.ArrayList;
import java.util.List;

<<<<<<< HEAD
import static org.junit.Assert.*;
=======
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
>>>>>>> refs/remotes/origin/master

/**
 * Created by corne on 2/5/2016.
 */
public class GameTest {
    private Game game = new Game();

    @Before
<<<<<<< HEAD
    public void setUp() throws Exception {
=======
    public void testInitializeGame() throws InvalidNameException, InvalidPlayerException, FailedToRandomizeException{
        List<Player> players = new ArrayList<Player>();
>>>>>>> refs/remotes/origin/master

    }

    @After
    public void tearDown() throws Exception {

<<<<<<< HEAD
    }

    @Test
    public void testCanFirstTurn() throws InvalidNameException, InvalidPlayerException, FailedToRandomizeException {
        List<Player> players = new ArrayList<>();

        Player one = new Player(0, CatanColor.BLUE, 0, new Name("Hope"));
        Player two = new Player(0, CatanColor.BROWN, 1, new Name("Corbin"));
        Player three = new Player(0, CatanColor.GREEN, 2, new Name("Hanna"));
        Player four = new Player(0, CatanColor.ORANGE, 3, new Name("Becca"));
=======
        Player one = new Player(0, CatanColor.BLUE, 1, new Name ("Hope"));
        Player two = new Player(0, CatanColor.BROWN, 2, new Name("Corbin"));
        Player three = new Player(0, CatanColor.GREEN, 3, new Name("Hanna"));
        Player four = new Player(0, CatanColor.ORANGE, 4, new Name("Becca"));
>>>>>>> refs/remotes/origin/master

        players.add(one);
        players.add(two);
        players.add(three);
        players.add(four);

<<<<<<< HEAD
        int first = game.initializeGame(players, false, false, false);

=======
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
<<<<<<< HEAD
    public void testFirstTurn() throws InvalidPlayerException, InvalidLocationException, StructureException {
        int current_turn = game.getCurrentTurn();
        HexLocation hloc = new HexLocation(0,0);
        VertexLocation vloc = new VertexLocation(hloc, VertexDirection.East);
        EdgeLocation eloc = new EdgeLocation(hloc, EdgeDirection.NorthEast);

>>>>>>> refs/remotes/origin/master
    }

    @Test
    public void testFirstTurn() throws Exception {
=======
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
>>>>>>> refs/remotes/origin/master

    @Test
    public void testRollNumber() throws InvalidDiceRollException{
        int turn = game.getCurrentTurn();
        game.getTurnTracker().nextPhase();
        game.getTurnTracker().nextPhase();
        int roll = game.rollNumber(turn);
        assertTrue(roll > 1);
        assertTrue(roll <= 12);
    }

<<<<<<< HEAD
    @Test
    public void testGetCurrentTurn() throws Exception {

=======
    void testCanDiscardCards() {
        int turn = game.getCurrentTurn();
>>>>>>> refs/remotes/origin/master
    }

    @Test
    public void testCanDiscardCards() throws Exception {

    }

<<<<<<< HEAD
    @Test
    public void testDiscardCards() throws Exception {
=======
>>>>>>> refs/remotes/origin/master


<<<<<<< HEAD
    @Test
    public void testCanRollNumber() throws Exception {
=======
>>>>>>> refs/remotes/origin/master


    @Test
    public void testRollNumber() throws Exception {

    }

    @Test
    public void testCanOfferTrade() throws Exception {

    }

    @Test
    public void testOfferTrade() throws Exception {

    }

    @Test
    public void testCanFinishTurn() throws Exception {

    }

    @Test
    public void testFinishTurn() throws Exception {

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
    public void testRob() throws Exception {

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

    @Test
    public void testCurrentLongestRoadSize() throws Exception {

    }

    @Test
    public void testCurrentLongestRoadPlayer() throws Exception {

    }

    @Test
    public void testNewLongestRoad() throws Exception {

    }

    @Test
    public void testCanBuyDevelopmentCard() throws Exception {

    }

    @Test
    public void testBuyDevelopmentCard() throws Exception {

    }

    @Test
    public void testCanTrade() throws Exception {

    }

    @Test
    public void testCanMaritimeTrade() throws Exception {

    }

    @Test
    public void testMaritimeTrade() throws Exception {

    }
}