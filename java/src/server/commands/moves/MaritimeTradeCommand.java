package server.commands.moves;

import server.commands.ICommand;
import server.exceptions.MaritimeTradeException;
import server.facade.IFacade;
import shared.definitions.ResourceType;
import shared.dto.GameModelDTO;

/**
 * A command object that maritime trades
 *
 * @author Joel Bradley
 */
public class MaritimeTradeCommand implements ICommand {

    private int player;
    private int ratio;
    private ResourceType give;
    private ResourceType get;
    IFacade facade;
    /**
     * Constructor
     */
    public MaritimeTradeCommand(int pindex, int rat, ResourceType send, ResourceType receive, IFacade fac) {
        player = pindex;
        ratio = rat;
        give = send;
        get = receive;
        facade = fac;
    }

    /**
     * Communicates with the ServerFacade to carry out the Maritime Trade command
     * @return IDTO
     */
    @Override
    public GameModelDTO execute() {
        try {
            facade.maritimeTrade(1, player, ratio, give, get);
        }catch(MaritimeTradeException e){

        }
        return null;
    }

}
