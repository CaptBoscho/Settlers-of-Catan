package server.commands.moves;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import server.exceptions.CommandExecutionFailedException;
import server.facade.MockFacade;
import server.main.Config;
import shared.dto.CookieWrapperDTO;
import shared.dto.DiscardCardsDTO;

import static org.junit.Assert.fail;

/**
 * @author Derek Argueta
 */
public class DiscardCardsCommandTest {

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
        new DiscardCardsCommand().execute();
    }

    /**
     * Validates that the command checks that the parameters are valid before
     * executing using the `assert` keyword.
     */
    @Test(expected = AssertionError.class)
    public void testExecuteWithBadParams() throws CommandExecutionFailedException {
        DiscardCardsCommand command = new DiscardCardsCommand();
        CookieWrapperDTO wrapperDTO = new CookieWrapperDTO(new DiscardCardsDTO(0,-1,0,0,0,0));
        command.setParams(wrapperDTO);
        command.execute();
    }

    /**
     * Validates that the command works with valid parameters
     */
    @Test
    public void testExecuteWithValidParams() {
        DiscardCardsCommand command = new DiscardCardsCommand();
        CookieWrapperDTO wrapperDTO = new CookieWrapperDTO(new DiscardCardsDTO(0,1,0,0,0,0));
        wrapperDTO.setGameId(MockFacade.DEFAULT_GAME);
        command.setParams(wrapperDTO);
        try {
            command.execute();
            //execution worked
        } catch (CommandExecutionFailedException e) {
            fail();
            e.printStackTrace();
        }
    }
}
