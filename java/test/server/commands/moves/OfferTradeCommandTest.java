package server.commands.moves;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import server.commands.CommandExecutionResult;
import server.exceptions.OfferTradeException;
import server.facade.MockFacade;
import server.main.Config;
import shared.definitions.ResourceType;
import shared.dto.OfferTradeDTO;
import shared.model.game.trade.Trade;
import shared.model.game.trade.TradePackage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joel on 3/20/16.
 */
public class OfferTradeCommandTest {

    private OfferTradeCommand command;

    @Before
    public void setUp() {
        Config.facade = new MockFacade();
    }

    @After
    public void tearDown() {

    }

    /**
     * checks to see if the command makes it to the facade and back
     */
    @Test
    public void testExecute() {
        List<ResourceType> send = new ArrayList<>();
        List<ResourceType> receive = new ArrayList<>();
        TradePackage package1 = new TradePackage(0,send);
        TradePackage package2 = new TradePackage(1,receive);
        Trade trade = new Trade(package1,package2);
        OfferTradeDTO dto = new OfferTradeDTO(0,trade,1);
        try {
            CommandExecutionResult answer = Config.facade.offerTrade(1,dto);
        } catch (OfferTradeException e) {
            e.printStackTrace();
        }
    }
}
