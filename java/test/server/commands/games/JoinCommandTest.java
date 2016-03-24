package server.commands.games;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import server.facade.MockFacade;
import server.main.Config;

/**
 * Unit Testing for the "Join Game" command.
 *
 * @author Derek Argueta
 */
public class JoinCommandTest {

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
    public void testExecuteWithMissingParams() {
        new JoinCommand().execute();
    }
}
