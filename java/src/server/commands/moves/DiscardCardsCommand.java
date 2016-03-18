package server.commands.moves;

import com.google.gson.JsonObject;
import server.commands.ICommand;
import server.exceptions.DiscardCardsException;
import server.facade.IFacade;
import shared.dto.DiscardCardsDTO;
import shared.dto.GameModelDTO;

/**
 * A command object that discards cards
 *
 * @author Joel Bradley
 */
public class DiscardCardsCommand implements ICommand {

    DiscardCardsDTO dto;
    IFacade facade;
    /**
     * Constructor
     */
    public DiscardCardsCommand(DiscardCardsDTO discard, IFacade fac) {
        dto = discard;
        facade = fac;
    }

    /**
     * Communicates with the ServerFacade to carry out the Discard Cards command
     * @return JsonObject
     */
    @Override
    public GameModelDTO execute() {
        try {
            facade.discardCards(1, dto);
        }catch(DiscardCardsException e){

        }
        return null;
    }

}
