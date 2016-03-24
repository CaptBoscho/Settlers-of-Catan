package server.commands.moves;

import com.google.gson.JsonObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import server.exceptions.CommandExecutionFailedException;
import server.facade.MockFacade;
import server.facade.ServerFacade;
import server.main.Config;
import shared.dto.BuildRoadDTO;
import shared.dto.CookieWrapperDTO;
import shared.dto.IDTO;
import shared.locations.EdgeDirection;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Unit Testing for the "Build Road" command.
 *
 * @author Derek Argueta
 */
public class BuildRoadCommandTest {

    private BuildRoadCommand command;

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
        new BuildRoadCommand().execute();
    }

    /**
     * Validates that the command checks that the parameters are valid before
     * executing using the `assert` keyword.
     */
    @Test(expected = AssertionError.class)
    public void testExecuteWithBadParams() throws CommandExecutionFailedException {
        command = new BuildRoadCommand();
        IDTO wrapperDTO = new CookieWrapperDTO(new BuildRoadDTO(-1, null, false));
        command.setParams(wrapperDTO);
        command.execute();
    }

    /**
     * Validates that the command works with valid parameters
     */
    @Test
    public void testExecuteWithValidParams() {
        command = new BuildRoadCommand();
        CookieWrapperDTO wrapperDTO = new CookieWrapperDTO(new BuildRoadDTO(1, new EdgeLocation(new HexLocation(1,1), EdgeDirection.South), false));
        wrapperDTO.setGameId(MockFacade.EMPTY_GAME);
        command.setParams(wrapperDTO);
        try {
            command.execute();
            fail();
        } catch (CommandExecutionFailedException e) {
            assertTrue(true); // exception should be thrown.
        }
    }
}
