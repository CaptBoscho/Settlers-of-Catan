package model.game;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import shared.definitions.CatanColor;
import shared.exceptions.FailedToRandomizeException;
import shared.exceptions.InvalidNameException;
import shared.exceptions.InvalidPlayerException;
import shared.model.game.Game;
import shared.model.player.Name;
import shared.model.player.Player;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by corne on 2/5/2016.
 */
public class GameTest {
    private Game game = new Game();

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testCanFirstTurn() throws InvalidNameException, InvalidPlayerException, FailedToRandomizeException {
        List<Player> players = new ArrayList<>();

        Player one = new Player(0, CatanColor.BLUE, 0, new Name("Hope"));
        Player two = new Player(0, CatanColor.BROWN, 1, new Name("Corbin"));
        Player three = new Player(0, CatanColor.GREEN, 2, new Name("Hanna"));
        Player four = new Player(0, CatanColor.ORANGE, 3, new Name("Becca"));

        players.add(one);
        players.add(two);
        players.add(three);
        players.add(four);

        int first = game.initializeGame(players, false, false, false);

    }

    @Test
    public void testFirstTurn() throws Exception {

    }

    @Test
    public void testGetCurrentTurn() throws Exception {

    }

    @Test
    public void testCanDiscardCards() throws Exception {

    }

    @Test
    public void testDiscardCards() throws Exception {

    }

    @Test
    public void testCanRollNumber() throws Exception {

    }

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