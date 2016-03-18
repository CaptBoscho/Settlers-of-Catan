package server.commands.moves;

import com.google.gson.JsonObject;
import server.commands.ICommand;
import server.exceptions.OfferTradeException;
import server.facade.IFacade;
import shared.definitions.ResourceType;
import shared.dto.OfferTradeDTO;
import shared.model.game.trade.Trade;

import java.util.ArrayList;
import java.util.List;

/**
 * A command object that offers a trade
 *
 * @author Joel Bradley
 */
public class OfferTradeCommand implements ICommand {
    private OfferTradeDTO dot;
    private IFacade facade;
    private int game;
    /**
     * Constructor
     */
    public OfferTradeCommand(OfferTradeDTO dto, IFacade fac) {
        dot = dto;
        facade = fac;
        game = 0;
    }

    /**
     * Communicates with the ServerFacade to carry out the Offer Trade command
     * @return JsonObject
     */
    @Override
    public JsonObject execute() {
        try {
            facade.offerTrade(1, dot);
        }catch(OfferTradeException e){

        }
        return null;
    }

}
