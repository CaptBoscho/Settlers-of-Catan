package server.commands.moves;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import server.facade.MockFacade;
import server.main.Config;

/**
 * Created by joel on 3/20/16.
 */
public class RoadBuildingCommandTest {

    private RoadBuildingCommand command;

    @Before
    public void setUp() {
        Config.facade = new MockFacade();
    }

    @After
    public void tearDown() {

    }

    @Test
    public void testExecute() {

    }
}
