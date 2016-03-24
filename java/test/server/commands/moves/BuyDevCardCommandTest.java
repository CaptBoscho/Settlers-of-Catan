package server.commands.moves;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import server.exceptions.CommandExecutionFailedException;
import server.facade.MockFacade;
import server.main.Config;
import shared.dto.BuyDevCardDTO;
import shared.dto.CookieWrapperDTO;
import shared.model.game.Game;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Unit Testing for the "Buy Dev Card" command.
 *
 * @author Derek Argueta
 */
public class BuyDevCardCommandTest {

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
        new BuyDevCardCommand().execute();
    }

    @Test
    public void testExecute() {
        //build a city where a settlement exists and pass
        CookieWrapperDTO dto = new CookieWrapperDTO(new BuyDevCardDTO(0));
        dto.setGameId(MockFacade.DEFAULT_GAME);
        BuyDevCardCommand command = new BuyDevCardCommand();
        command.setParams(dto);

        try {
            command.execute();
            fail("Insufficient Cards to buy a development card");
        } catch (CommandExecutionFailedException e) {
            //Should throw this exception
            assertTrue(true);
        }
    }
}
