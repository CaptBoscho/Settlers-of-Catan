import client.networking.ServerTest;
import model.Player.PlayerManagerTest;
import model.Player.PlayerTest;
import model.game.GameTest;
import model.map.MapTest;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.junit.runner.notification.Failure;
import org.junit.runners.Suite;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Derek Argueta
 */
public class TestRunner {

    @Test
    public void test_1() {
        assertEquals("OK", "OK");
        assertTrue(true);
        assertFalse(false);
    }

    public static void main(String[] args) {

        String[] testClasses = new String[] {
                "client.networking.ServerTest",
                "model.game.GameTest",
                "model.map.MapTest",
                "model.Player.PlayerManagerTest",
                "model.Player.PlayerTest",
                "model.bank.IDevelopmentCardBankTest",
        };

        org.junit.runner.JUnitCore.main(testClasses);
    }

    /*public static void main(String[] args) {
        Result result = JUnitCore.runClasses(TestSuite.class);
        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }
        System.out.println(result.wasSuccessful());
    }*/
}



/*

@RunWith(Suite.class)
@Suite.SuiteClasses({
        MapTest.class,
        GameTest.class,
        ServerTest.class,
        PlayerTest.class,
        PlayerManagerTest.class
})
class TestSuite {}*/
