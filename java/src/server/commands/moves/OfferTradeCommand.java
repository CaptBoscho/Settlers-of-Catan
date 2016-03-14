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
    private int sender;
    private int receiver;
    private List<ResourceType> send;
    private List<ResourceType> receive;
    private IFacade facade;
    /**
     * Constructor
     */
    public OfferTradeCommand(OfferTradeDTO dto, IFacade fac) {
        sender = dto.getSender();
        receiver = dto.getReceiver();
        Trade offer = dto.getOffer();
        send = offer.getPackage1().getResources();
        receive = offer.getPackage2().getResources();
        facade = fac;
    }

    /**
     * Communicates with the ServerFacade to carry out the Offer Trade command
     * @return JsonObject
     */
    @Override
    public JsonObject execute() {
        try {
            facade.offerTrade(sender, receiver, send, receive);
        }catch(OfferTradeException e){

        }
        return null;
    }

}
