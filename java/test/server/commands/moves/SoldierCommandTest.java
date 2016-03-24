package server.commands.moves;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import server.exceptions.CommandExecutionFailedException;
import server.facade.MockFacade;
import server.main.Config;
import shared.dto.PlaySoldierCardDTO;
import shared.locations.HexLocation;

/**
 * Unit Testing for the "Play Soldier" command.
 *
 * @author Derek Argueta
 */
public class SoldierCommandTest {

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
        SoldierCommand soldierCommand = new SoldierCommand();
        soldierCommand.setParams(null);
        soldierCommand.execute();
    }

    /**
     * Validates that the command checks that the parameters are set before
     * executing using the `assert` keyword.
     */
    @Test(expected = AssertionError.class)
    public void testExecuteWithMissingParams() throws CommandExecutionFailedException {
        new SoldierCommand().execute();
    }


    public void testGood() throws CommandExecutionFailedException {
        SoldierCommand soldierCommand = new SoldierCommand();
        soldierCommand.setParams(new PlaySoldierCardDTO(0,1,new HexLocation(0,0)));
        soldierCommand.execute();
    }
}
