package model.game;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import shared.definitions.CatanColor;

import shared.exceptions.*;
import shared.model.game.Game;
import shared.model.player.Name;
import shared.model.player.Player;
import shared.locations.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * @author Corbin Byers
 */
public class GameTest {

    private Game game = new Game();

    @Before
    void testInitializeGame() throws InvalidNameException, InvalidPlayerException, FailedToRandomizeException{
        List<Player> players = new ArrayList<Player>();


        Player one = new Player(0, CatanColor.BLUE, 0, new Name ("Hope"));
        Player two = new Player(0, CatanColor.BROWN, 1, new Name("Corbin"));
        Player three = new Player(0, CatanColor.GREEN, 2, new Name("Hanna"));
        Player four = new Player(0, CatanColor.ORANGE, 3, new Name("Becca"));

        players.add(one);
        players.add(two);
        players.add(three);
        players.add(four);

        int first = game.initializeGame(players, true, true, false);

        assertTrue(first >= 0 && first <= 4);
    }

    @Test
    void testCanFirstTurn() throws InvalidPlayerException, InvalidLocationException, Exception{
        int current_turn = game.getCurrentTurn();
        HexLocation hloc = new HexLocation(0,0);
        VertexLocation vloc = new VertexLocation(hloc, VertexDirection.East);
        EdgeLocation eloc = new EdgeLocation(hloc, EdgeDirection.NorthEast);
        assert(game.canFirstTurn(current_turn,vloc,eloc));

        HexLocation hloc2 = new HexLocation(8,8);
        VertexLocation vloc2 = new VertexLocation(hloc2, VertexDirection.East);

        assertTrue(!game.canFirstTurn(current_turn, vloc2,eloc));

        game.firstTurn(current_turn,vloc,eloc);

        int next = game.getTurnTracker().nextTurn();

        assertTrue(!game.canFirstTurn(next, vloc, eloc));



    }

    @Test
    void testFirstTurn() throws InvalidPlayerException, InvalidLocationException, StructureException {
        int current_turn = game.getCurrentTurn();
        HexLocation hloc = new HexLocation(0,0);
        VertexLocation vloc = new VertexLocation(hloc, VertexDirection.East);
        EdgeLocation eloc = new EdgeLocation(hloc, EdgeDirection.NorthEast);

        game.firstTurn(current_turn,vloc,eloc);
    }

    void testGetCurrentTurn() {

    }

    void testCanDiscardCards() {

    }

    void testDiscardCards() {

    }

    void testCanRollNumber() {

    }

    void testRollNumber() {

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
