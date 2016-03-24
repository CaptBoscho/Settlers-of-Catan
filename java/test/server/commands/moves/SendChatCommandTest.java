package server.commands.moves;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import server.exceptions.CommandExecutionFailedException;
import server.facade.MockFacade;
import server.main.Config;
import shared.dto.CookieWrapperDTO;
import shared.dto.SendChatDTO;

import static org.junit.Assert.assertTrue;

/**
 * Unit Testing for the "Send Chat" command.
 *
 * @author Derek Argueta
 */
public class SendChatCommandTest {

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
        SendChatCommand sendChatCommand = new SendChatCommand();
        sendChatCommand.setParams(null);
        sendChatCommand.execute();
    }

    /**
     * Validates that the command checks that the parameters are set before
     * executing using the `assert` keyword.
     */
    @Test(expected = AssertionError.class)
    public void testExecuteWithMissingParams() throws CommandExecutionFailedException {
        new SendChatCommand().execute();
    }

    @Test
    public void testExecute() {
        //send a chat
        CookieWrapperDTO dto = new CookieWrapperDTO(new SendChatDTO(0, "These tests are stupid"));
        dto.setGameId(MockFacade.DEFAULT_GAME);
        SendChatCommand command = new SendChatCommand();
        command.setParams(dto);
        try {
            //Should send a chat just fine
            command.execute();
        } catch(CommandExecutionFailedException e) {
            //Send chat didn't work
            assertTrue(false);
        }
    }
}
