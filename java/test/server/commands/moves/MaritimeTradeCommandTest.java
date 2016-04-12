package server.commands.moves;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import server.exceptions.CommandExecutionFailedException;
import server.facade.MockFacade;
import server.main.Config;
import shared.definitions.ResourceType;
import shared.dto.CookieWrapperDTO;
import shared.dto.MaritimeTradeDTO;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Unit Testing for the "Maritime Trade" command.
 *
 * @author Derek Argueta
 */
public class MaritimeTradeCommandTest {

    private MaritimeTradeCommand command;

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
        new MaritimeTradeCommand().execute();
    }

    @Test
    @Ignore
    public void testExecute() {
        //build a city where a settlement exists and pass
        CookieWrapperDTO dto = new CookieWrapperDTO(new MaritimeTradeDTO(0,4, "ore","wheat"));
        dto.setGameId(MockFacade.DEFAULT_GAME);
        MaritimeTradeCommand command = new MaritimeTradeCommand();
        command.setParams(dto);

        try {
            command.execute();
            fail("Shouldn't be able to maritime trade");
        } catch (CommandExecutionFailedException e) {
            //Should throw an error
            assertTrue(true);

        }
    }
}
