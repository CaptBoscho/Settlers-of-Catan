package shared.model.game;

import org.junit.Assert;
import shared.definitions.CatanColor;
import shared.exceptions.FailedToRandomizeException;
import shared.exceptions.InvalidNameException;
import shared.exceptions.InvalidPlayerException;
import shared.model.player.Name;
import shared.model.player.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Corbin Byers
 */
class GameTest {

    private Game game = new Game();

    /*void testInitializeGame() {

    }*/

    void testCanFirstTurn() throws InvalidNameException, InvalidPlayerException, FailedToRandomizeException{
        List<Player> players = new ArrayList<>();

        Player one = new Player(0, CatanColor.BLUE, 0, new Name ("Hope"));
        Player two = new Player(0, CatanColor.BROWN, 1, new Name("Corbin"));
        Player three = new Player(0, CatanColor.GREEN, 2, new Name("Hanna"));
        Player four = new Player(0, CatanColor.ORANGE, 3, new Name("Becca"));

        players.add(one);
        players.add(two);
        players.add(three);
        players.add(four);

        int first = game.initializeGame(players, false, false, false);


    }

    void testFirstTurn() {

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
