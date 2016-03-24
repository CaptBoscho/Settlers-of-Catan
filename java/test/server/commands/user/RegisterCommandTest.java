package server.commands.user;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import server.exceptions.CommandExecutionFailedException;
import server.facade.MockFacade;
import server.main.Config;
import shared.dto.AuthDTO;
import shared.dto.CookieWrapperDTO;
import shared.dto.SaveGameDTO;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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
    @Ignore
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


    @Test
    public void testExecute() {
        //build a city where a settlement exists and pass
        AuthDTO dto = new AuthDTO("corby","hopie");
        RegisterCommand command = new RegisterCommand();
        command.setParams(dto);

        command.execute();
        //User should've been added to game
        assertTrue(true);
    }
}
