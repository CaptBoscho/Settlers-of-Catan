package server.commands.games;

import org.junit.After;
import org.junit.Before;
import server.facade.MockFacade;
import server.main.Config;

/**
 * Unit Testing for the "List Games" command.
 *
 * @author Derek Argueta
 */
public class ListCommandTest {

    @Before
    public void setUp() {
        Config.facade = new MockFacade();
    }

    @After
    public void tearDown() {

    }
}
