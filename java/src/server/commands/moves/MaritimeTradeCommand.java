package server.commands.moves;

import com.google.gson.JsonObject;
import server.commands.ICommand;
import server.exceptions.MaritimeTradeException;
import server.facade.IFacade;
import shared.definitions.ResourceType;
import shared.dto.MaritimeTradeDTO;

/**
 * A command object that maritime trades
 *
 * @author Joel Bradley
 */
public class MaritimeTradeCommand implements ICommand {

    private int gameID;
    private int player;
    private int ratio;
    private ResourceType give;
    private ResourceType get;
    IFacade facade;
    /**
     * Constructor
     */
    public MaritimeTradeCommand(MaritimeTradeDTO dto, IFacade fac) {
        player = dto.getPlayerIndex();
        ratio = dto.getRatio();
        give = convert(dto.getOutputResource());
        get = convert(dto.getInputResource());
        facade = fac;
        gameID = 0;
    }

    private ResourceType convert(String type){
        switch(type){
            case "brick":
                return ResourceType.BRICK;
            case "wood":
                return ResourceType.WOOD;
            case "wheat":
                return ResourceType.WHEAT;
            case "sheep":
                return ResourceType.SHEEP;
            case "ore":
                return ResourceType.ORE;
            default:
                return null;
        }
    }

    /**
     * Communicates with the ServerFacade to carry out the Maritime Trade command
     * @return JsonObject
     */
    @Override
    public JsonObject execute() {
        try {
            facade.maritimeTrade(1, player, ratio, give, get);
        }catch(MaritimeTradeException e){

        }
        return null;
    }

}
