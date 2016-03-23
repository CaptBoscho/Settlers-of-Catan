package server.commands.games;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import server.facade.MockFacade;
import server.main.Config;

/**
 * Unit Testing for the "Create Game" command.
 *
 * @author Derek Argueta
 */
public class CreateCommandTest {

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
        new CreateCommand().execute();
    }
}
