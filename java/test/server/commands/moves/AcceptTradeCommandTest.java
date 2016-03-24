package server.commands.moves;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import server.exceptions.CommandExecutionFailedException;
import server.facade.MockFacade;
import server.main.Config;
import shared.dto.CookieWrapperDTO;
import shared.dto.TradeOfferResponseDTO;

import static org.junit.Assert.fail;

/**
 * Unit Testing for the "Accept Trade" command.
 *
 * @author Derek Argueta
 */
public class AcceptTradeCommandTest {

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
        new AcceptTradeCommand().execute();
    }

    @Test
    public void testExecute() {
        AcceptTradeCommand acceptTradeCommand = new AcceptTradeCommand();
        CookieWrapperDTO wrapper = new CookieWrapperDTO(new TradeOfferResponseDTO(0, true));
        acceptTradeCommand.setParams(wrapper);
        try {
            acceptTradeCommand.execute();
            fail();
        } catch (CommandExecutionFailedException e) {
            e.printStackTrace();
        }
    }
}
