package server.commands.moves;

import server.commands.CommandExecutionResult;
import server.commands.ICommand;
import server.exceptions.CommandExecutionFailedException;
import server.exceptions.RobPlayerException;
import server.exceptions.SoldierException;
import server.facade.IFacade;
import shared.dto.IDTO;
import shared.dto.PlaySoldierCardDTO;

/**
 * A command object that plays a soldier card
 *
 * @author Joel Bradley
 */
public class SoldierCommand implements ICommand {

    private IFacade facade;
    private PlaySoldierCardDTO dto;

    /**
     * Communicates with the ServerFacade to carry out the Soldier command
     * @return IDTO
     */
    @Override
    public CommandExecutionResult execute() throws CommandExecutionFailedException {
//        try {
//            return facade.soldier(1, dto.getPlayerIndex(), dto.getLocation(), dto.getVictimIndex());
//        } catch (SoldierException e) {
//            throw new CommandExecutionFailedException(e.getMessage());
//        }
        return null;
    }

    @Override
    public void setParams(IDTO dto) {

    }

}
