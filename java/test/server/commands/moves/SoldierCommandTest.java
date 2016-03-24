package server.commands.moves;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import server.exceptions.CommandExecutionFailedException;
import server.facade.MockFacade;
import server.main.Config;
import shared.dto.CookieWrapperDTO;
import shared.dto.PlaySoldierCardDTO;
import shared.locations.HexLocation;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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

    @Test
    public void testExecute() throws CommandExecutionFailedException {
        CookieWrapperDTO dto = new CookieWrapperDTO(new PlaySoldierCardDTO(0,1,new HexLocation(0,0)));
        dto.setGameId(MockFacade.DEFAULT_GAME);
        SoldierCommand soldierCommand = new SoldierCommand();
        soldierCommand.setParams(dto);
        try {
            soldierCommand.execute();
            fail("can't rob if you don't have the card");
        } catch (CommandExecutionFailedException e) {
            //Should enter here
            assertTrue(true);
        }
    }
}
