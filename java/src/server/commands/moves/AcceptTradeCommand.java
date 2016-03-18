package server.commands.moves;

import com.google.gson.JsonObject;
import server.commands.ICommand;
import server.exceptions.AcceptTradeException;
import server.facade.IFacade;
import shared.dto.TradeOfferResponseDTO;
import shared.exceptions.PlayerExistsException;
import shared.model.bank.InvalidTypeException;

import javax.naming.InsufficientResourcesException;

/**
 * A command object that accepts a trade
 *
 * @author Joel Bradley
 */
public class AcceptTradeCommand implements ICommand {
    private IFacade facade;
    private int playerIndex;
    private boolean answer;
    /**
     * Constructor
     */
    public AcceptTradeCommand(TradeOfferResponseDTO dto, IFacade fac) {
        facade = fac;
        playerIndex = dto.getPlayerIndex();
        answer = dto.willAccept();
    }

    /**
     * Communicates with the ServerFacade to carry out the Accept Trade command
     * @return JsonObject
     */
    @Override
    public JsonObject execute() {
        try {
            facade.acceptTrade(1, playerIndex, answer);
        }catch(AcceptTradeException e){}
        catch(InvalidTypeException e){}
        catch(InsufficientResourcesException e){}
        catch(PlayerExistsException e){}
        return null;
    }

}