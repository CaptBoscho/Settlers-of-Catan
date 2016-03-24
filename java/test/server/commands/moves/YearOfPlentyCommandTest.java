package server.commands.moves;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import server.exceptions.CommandExecutionFailedException;
import server.facade.MockFacade;
import server.main.Config;
import shared.dto.SaveGameDTO;

/**
 * Unit Testing for the "Play Year of Plenty" command.
 *
 * @author Derek Argueta
 */
public class YearOfPlentyCommandTest {

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
        YearOfPlentyCommand yearOfPlentyCommand = new YearOfPlentyCommand();
        yearOfPlentyCommand.setParams(null);
        yearOfPlentyCommand.execute();
    }

    /**
     * Validates that the command checks that the parameters are set before
     * executing using the `assert` keyword.
     */
    @Test(expected = AssertionError.class)
    public void testExecuteWithMissingParams() throws CommandExecutionFailedException {
        new YearOfPlentyCommand().execute();
    }

    /**
     * Validate that you cannot pass in the wrong king of DTO
     */
    @Test(expected = AssertionError.class)
    public void testWrongDto() throws CommandExecutionFailedException {
        YearOfPlentyCommand registerCommand = new YearOfPlentyCommand();
        registerCommand.setParams(new SaveGameDTO(1, "Derek"));
        registerCommand.execute();
    }
}
