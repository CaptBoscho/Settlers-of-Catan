package server.commands.moves;

import client.services.CommandExecutionFailed;
import com.google.gson.JsonObject;
import server.commands.CommandExecutionResult;
import server.commands.ICommand;
import server.exceptions.CommandExecutionFailedException;
import server.exceptions.OfferTradeException;
import server.facade.IFacade;
import shared.dto.IDTO;
import shared.dto.OfferTradeDTO;

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
     * Communicates with the ServerFacade to carry out the Offer Trade command
     * @return IDTO
     */
    @Override
    public CommandExecutionResult execute() throws CommandExecutionFailedException {

//        try {
//            return facade.offerTrade(1, dot);
//        }catch(OfferTradeException e){
//            throw new CommandExecutionFailedException(e.getMessage());
//        }
        return null;
    }

    @Override
    public void setParams(IDTO dto) {

    }

}
