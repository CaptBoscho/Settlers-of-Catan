package server.commands.moves;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import server.exceptions.CommandExecutionFailedException;
import server.facade.MockFacade;
import server.main.Config;
import shared.dto.CookieWrapperDTO;
import shared.dto.PlayMonopolyDTO;

import static org.junit.Assert.fail;

/**
 * Unit Testing for the "Play Monopoly" command.
 *
 * @author Derek Argueta
 */
public class MonopolyCommandTest {

    @Before
    public void setUp() {
        Config.facade = new MockFacade();
    }

    @After
    public void tearDown() {

    }

    /**
     * Validate that you cannot pass a null dto
     */
    @Test(expected = AssertionError.class)
    public void testNullParameters() throws CommandExecutionFailedException {
        MonopolyCommand monopolyCommand = new MonopolyCommand();
        monopolyCommand.setParams(null);
        monopolyCommand.execute();
    }

    /**
     * Validates that the command checks that the parameters are set before
     * executing using the `assert` keyword.
     */
    @Test(expected = AssertionError.class)
    public void testExecuteWithMissingParams() throws CommandExecutionFailedException {
        new MonopolyCommand().execute();
    }

    /**
     * Validates that the command works with valid parameters
     */
    @Test
    @Ignore
    public void testExecuteWithValidParams() {
        MonopolyCommand monopolyCommand = new MonopolyCommand();
        CookieWrapperDTO wrapperDTO = new CookieWrapperDTO(new PlayMonopolyDTO(0, "wood"));
        wrapperDTO.setGameId(MockFacade.DEFAULT_GAME);
        monopolyCommand.setParams(wrapperDTO);
        try {
            monopolyCommand.execute();
        } catch (CommandExecutionFailedException e) {
            fail();
            e.printStackTrace();
        }
    }
}