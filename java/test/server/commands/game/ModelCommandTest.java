package server.commands.game;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import server.exceptions.CommandExecutionFailedException;
import server.facade.MockFacade;
import server.main.Config;
import shared.dto.CookieWrapperDTO;
import shared.dto.GameModelDTO;

import static org.junit.Assert.fail;

/**
 * Unit Testing for the "Model" command.
 *
 * @author Derek Argueta
 */
public class ModelCommandTest {

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
        new ModelCommand().execute();
    }

    /**
     * Validates that the command checks that the parameters are valid before
     * executing using the `assert` keyword.
     */
    @Test(expected = AssertionError.class)
    public void testExecuteWithBadParams() throws CommandExecutionFailedException {
        ModelCommand command = new ModelCommand();
        CookieWrapperDTO wrapperDTO = new CookieWrapperDTO(new GameModelDTO(null));
        command.setParams(wrapperDTO);
        command.execute();
    }

    /**
     * Validates that the command works with valid parameters
     */
    @Test
    public void testExecuteWithValidParams() {
        ModelCommand command = new ModelCommand();
        CookieWrapperDTO wrapperDTO = new CookieWrapperDTO(new GameModelDTO(0));
        wrapperDTO.setGameId(MockFacade.DEFAULT_GAME);
        command.setParams(wrapperDTO);
        try {
            command.execute();
        } catch (CommandExecutionFailedException e) {
            fail();
            e.printStackTrace();
        }
    }
}
