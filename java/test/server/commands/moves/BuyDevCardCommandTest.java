package server.commands.moves;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import server.exceptions.BuyDevCardException;
import server.exceptions.CommandExecutionFailedException;
import server.facade.MockFacade;
import server.main.Config;
import shared.definitions.ResourceType;
import shared.dto.BuyDevCardDTO;
import shared.dto.CookieWrapperDTO;
import shared.exceptions.PlayerExistsException;
import shared.model.cards.resources.Ore;
import shared.model.cards.resources.Sheep;
import shared.model.cards.resources.Wheat;
import shared.model.game.Game;
import shared.model.player.Player;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created by joel on 3/20/16.
 */
public class BuyDevCardCommandTest {

    private BuyDevCardCommand command;
    private MockFacade facade;

    @Before
    public void setUp() {
        facade = new MockFacade();
        facade.resetGame();
    }

    @After
    public void tearDown() {

    }

    @Test
    public void testExecute() {
        boolean tested = false;
        Game game = facade.getGame();

        for (Player player : game.getPlayers()) {
            if (player.canBuyDevCard()) {
                testCommand(player);
            }
        }

        try {
            Player player = game.getPlayerByIndex(0);
            player.addResourceCard(new Sheep());
            player.addResourceCard(new Wheat());
            player.addResourceCard(new Ore());
            assertTrue(player.canBuyDevCard());
            testCommand(player);

        } catch (PlayerExistsException e) {
            e.printStackTrace();
        }
    }

    private void testCommand(Player player) {
        int sheepBefore = player.getNumberOfType(ResourceType.SHEEP);
        int wheatBefore = player.getNumberOfType(ResourceType.WHEAT);
        int oreBefore = player.getNumberOfType(ResourceType.ORE);

        command = new BuyDevCardCommand();
        command.setParams(new CookieWrapperDTO(new BuyDevCardDTO(player.getPlayerIndex())));
        try {
            command.execute();

            int sheepAfter = player.getNumberOfType(ResourceType.SHEEP);
            int wheatAfter = player.getNumberOfType(ResourceType.WHEAT);
            int oreAfter = player.getNumberOfType(ResourceType.ORE);

            assertEquals(sheepBefore, sheepAfter + 1);
            assertEquals(wheatBefore, wheatAfter + 1);
            assertEquals(oreBefore, oreAfter + 1);

        } catch (CommandExecutionFailedException e) {
            fail("BuyDevCardCommand failed");
            e.printStackTrace();
        }
    }
}
