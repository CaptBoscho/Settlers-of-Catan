package server.commands.moves;

import org.junit.After;
import org.junit.Before;
import server.facade.MockFacade;
import server.main.Config;

/**
 * Unit Testing for the "Finish Turn" command.
 *
 * @author Derek Argueta
 */
public class FinishTurnCommandTest {

    @Before
    public void setUp() {
        Config.facade = new MockFacade();
    }

    @After
    public void tearDown() {

    }
}
