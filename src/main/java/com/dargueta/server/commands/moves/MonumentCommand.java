package server.commands.moves;

import server.commands.CommandExecutionResult;
import server.commands.ICommand;
import server.exceptions.CommandExecutionFailedException;
import server.exceptions.MonumentException;
import server.main.Config;
import shared.dto.IDTO;
import shared.dto.PlayMonumentDTO;

/**
 * A command object that plays a monument card
 *
 * @author Joel Bradley
 */
public class MonumentCommand implements ICommand {

    PlayMonumentDTO dto;

    /**
     * Communicates with the ServerFacade to carry out the Monument command
     * @return IDTO
     */
    @Override
    public CommandExecutionResult execute() throws CommandExecutionFailedException {
        try {
            return Config.facade.monument(1, dto.getPlayerIndex());
        } catch (MonumentException e) {
            throw new CommandExecutionFailedException("MonumentCommand failed to execute properly");
        }
    }

    @Override
    public void setParams(IDTO dto) {
        this.dto = (PlayMonumentDTO)dto;
    }
}