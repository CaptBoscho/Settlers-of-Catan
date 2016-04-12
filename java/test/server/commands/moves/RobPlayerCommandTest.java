package server.commands.moves;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import server.exceptions.CommandExecutionFailedException;
import server.facade.MockFacade;
import server.main.Config;
import shared.dto.BuildCityDTO;
import shared.dto.CookieWrapperDTO;
import shared.dto.RobPlayerDTO;
import shared.locations.HexLocation;
import shared.locations.VertexDirection;
import shared.locations.VertexLocation;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Unit Testing for the "Rob Player" command.
 *
 * @author Derek Argueta
 */
public class RobPlayerCommandTest {

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
        RobPlayerCommand robPlayerCommand = new RobPlayerCommand();
        robPlayerCommand.setParams(null);
        robPlayerCommand.execute();
    }

    /**
     * Validates that the command checks that the parameters are set before
     * executing using the `assert` keyword.
     */
    @Test(expected = AssertionError.class)
    public void testExecuteWithMissingParams() throws CommandExecutionFailedException {
        new RobPlayerCommand().execute();
    }

    @Test
    @Ignore
    public void testExecute() {
        //build a city where a settlement exists and pass
        CookieWrapperDTO dto = new CookieWrapperDTO(new RobPlayerDTO(0, 1, new HexLocation(0, 0)));
        dto.setGameId(MockFacade.DEFAULT_GAME);
        RobPlayerCommand command = new RobPlayerCommand();
        command.setParams(dto);

        try {
            command.execute();
            fail("can't rob");
        } catch (CommandExecutionFailedException e) {
            //shouldn't be able to rob
            assertTrue(true);
        }
    }
}
