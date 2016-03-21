package server.commands.game;

import server.commands.CommandExecutionResult;
import server.commands.ICommand;
import shared.dto.GameModelDTO;
import shared.dto.IDTO;

/**
 * A command object that gets the game model
 *
 * @author Joel Bradley
 */
public class ModelCommand implements ICommand {

    private GameModelDTO dto;

    /**
     * Communicates with the ServerFacade to carry out the Model command
     * @return IDTO
     */
    @Override
    public CommandExecutionResult execute() {
        return null;
    }

    @Override
    public void setParams(IDTO dto) {
        this.dto = (GameModelDTO)dto;
    }

}
