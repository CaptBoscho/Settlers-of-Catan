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
            "model.bank.IDevelopmentCardBankTest",
            "model.bank.ResourceCardBankTests",
            "model.bank.StructureBankTests",
            "model.game.trade.TradePackageTests",
            "model.game.trade.TradeTests",
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
