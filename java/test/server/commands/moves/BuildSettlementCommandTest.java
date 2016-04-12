package server.commands.moves;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import server.exceptions.CommandExecutionFailedException;
import server.facade.MockFacade;
import server.main.Config;
import shared.dto.BuildSettlementDTO;
import shared.dto.CookieWrapperDTO;
import shared.locations.HexLocation;
import shared.locations.VertexDirection;
import shared.locations.VertexLocation;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Unit Testing for the "Build Settlement" command.
 *
 * @author Derek Argueta
 */
public class BuildSettlementCommandTest {

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
        new BuildSettlementCommand().execute();
    }

    @Test
    @Ignore
    public void testExecute() {
        //build a settlement and pass
        CookieWrapperDTO dto = new CookieWrapperDTO(new BuildSettlementDTO(0, new VertexLocation(new HexLocation(0,2), VertexDirection.West), true));
        dto.setGameId(MockFacade.DEFAULT_GAME);
        BuildSettlementCommand command = new BuildSettlementCommand();
        command.setParams(dto);
        try {
            command.execute();
        } catch (CommandExecutionFailedException e) {
            fail(e.getMessage());
        }

        //try to build settlement where you shouldn't and fail
        dto = new CookieWrapperDTO(new BuildSettlementDTO(0, new VertexLocation(new HexLocation(0,3), VertexDirection.SouthWest), true));
        dto.setGameId(MockFacade.EMPTY_GAME);
        command.setParams(dto);
        try {
            command.execute();
            fail("Should not be able to build a settlement");
        } catch(CommandExecutionFailedException e) {
            //Exception should be thrown
            assertTrue(true);
        }
    }
}
