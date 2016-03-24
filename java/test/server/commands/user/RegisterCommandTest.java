package server.commands.user;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import server.exceptions.CommandExecutionFailedException;
import server.facade.MockFacade;
import server.main.Config;
import shared.dto.AuthDTO;
import shared.dto.SaveGameDTO;

/**
 * @author Derek Argueta
 */
public class RegisterCommandTest {

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
        RegisterCommand registerCommand = new RegisterCommand();
        registerCommand.setParams(null);
        registerCommand.execute();
    }

    /**
     * Validate that you cannot pass a null username
     */
    @Test(expected = AssertionError.class)
    public void testNullUsername() throws CommandExecutionFailedException {
        AuthDTO authDTO = new AuthDTO(null, "password");
        RegisterCommand registerCommand = new RegisterCommand();
        registerCommand.setParams(authDTO);
        registerCommand.execute();
    }

    /**
     * Validate that you cannot pass a null password
     */
    @Test(expected = AssertionError.class)
    public void testNullPassword() throws CommandExecutionFailedException {
        AuthDTO authDTO = new AuthDTO("username", null);
        RegisterCommand registerCommand = new RegisterCommand();
        registerCommand.setParams(authDTO);
        registerCommand.execute();
    }

    /**
     * Validate that you cannot pass in the wrong king of DTO
     */
    @Test(expected = AssertionError.class)
    public void testWrongDto() throws CommandExecutionFailedException {
        RegisterCommand registerCommand = new RegisterCommand();
        registerCommand.setParams(new SaveGameDTO(1, "Derek"));
        registerCommand.execute();
    }

    /**
     * Validates that the command checks that the parameters are set before
     * executing using the `assert` keyword.
     */
    @Test(expected = AssertionError.class)
    public void testExecuteWithMissingParams() {
        new RegisterCommand().execute();
    }
}
