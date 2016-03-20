package server.commands.moves;

import server.commands.CommandExecutionResult;
import server.commands.ICommand;
import server.exceptions.CommandExecutionFailedException;
import server.exceptions.DiscardCardsException;
import server.facade.IFacade;
import shared.dto.DiscardCardsDTO;
import shared.dto.GameModelDTO;
import shared.dto.IDTO;

/**
 * A command object that discards cards
 *
 * @author Joel Bradley
 */
public class DiscardCardsCommand implements ICommand {

    DiscardCardsDTO dto;
    IFacade facade;

    /**
     * Communicates with the ServerFacade to carry out the Discard Cards command
     * @return IDTO
     */
    @Override
    public CommandExecutionResult execute() throws CommandExecutionFailedException {
//        try {
//            return facade.discardCards(1, dto);
//        }catch(DiscardCardsException e){
//            throw new CommandExecutionFailedException(e.getMessage());
//        }
        return null;
    }

    @Override
    public void setParams(IDTO dto) {

    }
}
