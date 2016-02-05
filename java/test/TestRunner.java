import model.Player.PlayerManagerTest;
import model.Player.PlayerTest;
import model.game.DiceTest;
import model.game.GameTest;
import model.game.TurnTrackerTest;
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
        // server tests
        client.networking.CreateGameTest.class,
        client.networking.AuthenticationTest.class,

        // model tests
        MapTest.class,
        GameTest.class,
        DiceTest.class,
        PlayerTest.class,
        TurnTrackerTest.class,
        PlayerManagerTest.class
})
class TestSuite {}
