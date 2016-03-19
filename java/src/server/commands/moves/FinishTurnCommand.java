package server.commands.moves;

import server.commands.ICommand;
import server.exceptions.CommandExecutionFailedException;
import server.exceptions.FinishTurnException;
import server.facade.IFacade;
import shared.dto.FinishTurnDTO;
import shared.dto.GameModelDTO;
import shared.dto.IDTO;

/**
 * A command object that finishes a turn
 *
 * @author Joel Bradley
 */
public class FinishTurnCommand implements ICommand {
    private IFacade facade;
    private FinishTurnDTO dto;

    /**
     * Constructor
     */
    public FinishTurnCommand(IFacade facade, FinishTurnDTO dto) {
        this.facade = facade;
        this.dto = dto;
    }

    /**
     * Communicates with the ServerFacade to carry out the Finish Turn command
     * @return IDTO
     */
    @Override
    public IDTO execute() throws CommandExecutionFailedException {
        try {
            return facade.finishTurn(1, dto.getPlayerIndex());
        } catch (FinishTurnException e) {
            e.printStackTrace();
            throw new CommandExecutionFailedException("Error ending player turn!");
        }
    }

}
