package server.commands.moves;

import com.google.gson.JsonParser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import server.commands.CommandExecutionResult;
import server.exceptions.CommandExecutionFailedException;
import server.exceptions.OfferTradeException;
import server.facade.MockFacade;
import server.main.Config;
import shared.definitions.ResourceType;
import shared.dto.OfferTradeDTO;
import shared.model.game.Game;
import shared.model.game.trade.Trade;
import shared.model.game.trade.TradePackage;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertNull;
import static junit.framework.TestCase.fail;

/**
 * Unit Testing for the "Offer Trade" command.
 *
 * @author Joel Bradley
 */
public class OfferTradeCommandTest {

    @Before
    public void setUp() {
        Config.facade = new MockFacade();
    }

    @After
    public void tearDown() {

    }

    /**
     * Validates that the command checks that the parameters are set before
     * executing using the `assert` keyword.
     */
    @Test(expected = AssertionError.class)
    public void testExecuteWithMissingParams() throws CommandExecutionFailedException {
        new OfferTradeCommand().execute();
    }

    /**
     * checks to see if the command makes it to the facade and back
     */
    @Test
    public void testExecute() {
        List<ResourceType> send = new ArrayList<>();
        send.add(ResourceType.BRICK);
        List<ResourceType> receive = new ArrayList<>();
        receive.add(ResourceType.WHEAT);
        TradePackage package1 = new TradePackage(0, send);
        TradePackage package2 = new TradePackage(1, receive);
        Trade trade = new Trade(package1, package2);
        OfferTradeDTO dto = new OfferTradeDTO(0, trade, 1);
        CommandExecutionResult answer;
        try {
             answer = Config.facade.offerTrade(1,dto);
        } catch (OfferTradeException e) {
            e.printStackTrace();
            fail("Execption shouldn't get triggered");
            return;
        }

        Game resultGame = new Game(new JsonParser().parse(answer.getBody()).getAsJsonObject());
        assertNull(resultGame.getWinner());
    }
}
