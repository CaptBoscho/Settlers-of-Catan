package testrunner;
import org.junit.Test;

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
            "model.bank.IDevelopmentCardBankTest",
            "model.bank.StructureBankTests",
            "model.game.GameTest",
            "model.game.LargestArmyTests",
            "model.game.LongestRoadTests",
            "model.game.MessageLineTests",
            "model.game.MessageListTests",
            "model.game.TurnTrackerTests",
            "model.locations.HexLocationTests",
            "model.map.EdgeTests",
            "model.map.MapTest",
            "model.map.PortTests",
            "model.map.RobberTests",
            "model.map.VertexTests",
            "model.Player.PlayerManagerTest",
            "model.Player.PlayerTest",
        };

        org.junit.runner.JUnitCore.main(testClasses);
    }
}
