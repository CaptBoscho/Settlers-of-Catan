package server.commands.moves;

import server.commands.CommandExecutionResult;
import server.commands.ICommand;
import server.exceptions.CommandExecutionFailedException;
import server.exceptions.MonumentException;
import server.facade.IFacade;
import server.facade.ServerFacade;
import shared.dto.IDTO;
import shared.dto.PlayMonumentDTO;

/**
 * A command object that plays a monument card
 *
 * @author Joel Bradley
 */
public class MonumentCommand implements ICommand {
    PlayMonumentDTO dto;
    IFacade facade;

    /**
     * Constructor
     */
    public MonumentCommand() {
        this.dto = dto;
        this.facade = facade;
    }

    /**
     * Communicates with the ServerFacade to carry out the Monument command
     * @return IDTO
     */
    @Override
    public CommandExecutionResult execute() throws CommandExecutionFailedException {
//        try {
//            return ServerFacade.getInstance().monument(1, dto.getPlayerIndex());
//        } catch (MonumentException e) {
//            throw new CommandExecutionFailedException("MonumentCommand failed to execute properly");
//        }
        return null;
    }

    @Override
    public void setParams(IDTO dto) {

    }
}