package server.commands.moves;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import server.exceptions.BuyDevCardException;
import server.facade.MockFacade;
import server.main.Config;

/**
 * Created by joel on 3/20/16.
 */
public class BuyDevCardCommandTest {

    private BuyDevCardCommand command;

    @Before
    public void setUp() {
        Config.facade = new MockFacade();
    }

    @After
    public void tearDown() {

    }

    @Test
    public void testExecute() {
        try {
            System.out.println(Config.facade.buyDevCard(1, 3));
        } catch (BuyDevCardException e) {
            e.printStackTrace();
        }
    }
}
