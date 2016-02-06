import client.networking.ServerTest;
import model.Player.PlayerManagerTest;
import model.Player.PlayerTest;
import model.game.GameTest;
import model.map.MapTest;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.junit.runner.notification.Failure;
import org.junit.runners.Suite;

/**
 * @author Derek Argueta
 */
public class TestRunner {

    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(TestSuite.class);
        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }
        System.out.println(result.wasSuccessful());
    }
}


@RunWith(Suite.class)
@Suite.SuiteClasses({
        MapTest.class,
        GameTest.class,
        ServerTest.class,
        PlayerTest.class,
        PlayerManagerTest.class
})
class TestSuite {}
