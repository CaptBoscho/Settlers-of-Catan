package server.commands.moves;

import server.commands.ICommand;
import server.exceptions.CommandExecutionFailedException;
import server.exceptions.RobPlayerException;
import server.facade.IFacade;
import shared.dto.IDTO;
import shared.dto.RobPlayerDTO;

/**
 * A command object that robs a player
 *
 * @author Joel Bradley
 */
public class RobPlayerCommand implements ICommand {

    private IFacade facade;
    private RobPlayerDTO dto;

    /**
     * Constructor
     */
    public RobPlayerCommand(IFacade facade, RobPlayerDTO dto) {
        this.facade = facade;
        this.dto = dto;
    }

    /**
     * Communicates with the ServerFacade to carry out the Rob Player command
     * @return IDTO
     */
    @Override
    public IDTO execute() throws CommandExecutionFailedException {
        try {
            return facade.robPlayer(1, dto.getPlayerIndex(), dto.getLocation(), dto.getVictimIndex());
        } catch (RobPlayerException e) {
            throw new CommandExecutionFailedException(e.getMessage());
        }
    }

}
