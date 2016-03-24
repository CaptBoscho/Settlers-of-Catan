package server.commands.user;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import server.exceptions.CommandExecutionFailedException;
import server.facade.MockFacade;
import server.main.Config;
import shared.dto.AuthDTO;
import shared.dto.SaveGameDTO;

import static org.junit.Assert.assertFalse;

/**
 * Unit Testing for the "Login" command.
 *
 * @author Derek Argueta
 */
public class LoginCommandTest {

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
        LoginCommand loginCommand = new LoginCommand();
        loginCommand.setParams(null);
        loginCommand.execute();
    }

    /**
     * Validate that you cannot pass a null username
     */
    @Test(expected = AssertionError.class)
    public void testNullUsername() throws CommandExecutionFailedException {
        AuthDTO authDTO = new AuthDTO(null, "password");
        LoginCommand registerCommand = new LoginCommand();
        registerCommand.setParams(authDTO);
        registerCommand.execute();
    }

    /**
     * Validate that you cannot pass a null password
     */
    @Test(expected = AssertionError.class)
    public void testNullPassword() throws CommandExecutionFailedException {
        AuthDTO authDTO = new AuthDTO("username", null);
        LoginCommand registerCommand = new LoginCommand();
        registerCommand.setParams(authDTO);
        registerCommand.execute();
    }

    /**
     * Validate that you cannot pass in the wrong king of DTO
     */
    @Test(expected = AssertionError.class)
    public void testWrongDto() throws CommandExecutionFailedException {
        LoginCommand registerCommand = new LoginCommand();
        registerCommand.setParams(new SaveGameDTO(1, "Derek"));
        registerCommand.execute();
    }

    /**
     * Validates that the command checks that the parameters are set before
     * executing using the `assert` keyword.
     */
    @Test(expected = AssertionError.class)
    public void testExecuteWithMissingParams() {
        new LoginCommand().execute();
    }

    @Test
    public void testExecute() {
        //login
        AuthDTO dto = new AuthDTO("pedro", "pedro");
        LoginCommand command = new LoginCommand();
        command.setParams(dto);
        assertFalse(command.execute().errorOccurred());
    }
}
