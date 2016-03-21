package server.commands.game;

import server.commands.CommandExecutionResult;
import server.commands.ICommand;
import server.exceptions.CommandExecutionFailedException;
import server.exceptions.GetModelException;
import server.main.Config;
import shared.dto.CookieWrapperDTO;
import shared.dto.IDTO;

/**
 * A command object that gets the game model
 *
 * @author Joel Bradley
 */
public class ModelCommand implements ICommand {

    private int gameId;

    /**
     * Communicates with the ServerFacade to carry out the Model command
     * @return IDTO
     */
    @Override
    public CommandExecutionResult execute() throws CommandExecutionFailedException {
        try {
            return Config.facade.getModel(this.gameId);
        } catch (GetModelException e) {
            throw new CommandExecutionFailedException(e.getMessage());
        }
    }

    @Override
    public void setParams(final IDTO dto) {
        this.gameId = ((CookieWrapperDTO) dto).getGameId();
    }

}
