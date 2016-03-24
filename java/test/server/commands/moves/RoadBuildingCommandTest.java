package server.commands.moves;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import server.exceptions.CommandExecutionFailedException;
import server.facade.MockFacade;
import server.main.Config;
import shared.dto.CookieWrapperDTO;
import shared.dto.RoadBuildingDTO;
import shared.locations.EdgeDirection;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Unit Testing for the "Play Road-Building" command.
 *
 * @author Derek Argueta
 */
public class RoadBuildingCommandTest {

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
        RoadBuildingCommand roadBuildingCommand = new RoadBuildingCommand();
        roadBuildingCommand.setParams(null);
        roadBuildingCommand.execute();
    }

    /**
     * Validates that the command checks that the parameters are set before
     * executing using the `assert` keyword.
     */
    @Test(expected = AssertionError.class)
    public void testExecuteWithMissingParams() throws CommandExecutionFailedException {
        new RoadBuildingCommand().execute();
    }

    @Test
    public void testExecute() {
        //play a road building card
        CookieWrapperDTO dto = new CookieWrapperDTO(new RoadBuildingDTO(0,
                new EdgeLocation(new HexLocation(0,2), EdgeDirection.NorthWest),
                new EdgeLocation(new HexLocation(0,2), EdgeDirection.SouthWest)));
        dto.setGameId(MockFacade.DEFAULT_GAME);
        RoadBuildingCommand command = new RoadBuildingCommand();
        command.setParams(dto);
        try {
            command.execute();
            fail("Should not be able to play road building card");
        } catch(CommandExecutionFailedException e) {
            //Exception should be thrown
            assertTrue(true);
        }
    }
}
