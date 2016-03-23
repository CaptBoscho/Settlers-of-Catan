package server.commands.game;


import server.commands.CommandExecutionResult;
import server.commands.ICommand;
import server.exceptions.ListAIException;
import server.main.Config;
import shared.dto.IDTO;
import shared.dto.ListAIDTO;

/**
 * A command object that list the AI
 *
 * @author Joel Bradley
 */
public class ListAICommand implements ICommand {

    private ListAIDTO dto;

    /**
     * Communicates with the ServerFacade to carry out the List AI command
     *
     * @return CommandExecutionResult
     */
    @Override
    public CommandExecutionResult execute() {
        try {
            int tmpId = 0;
            return Config.facade.listAI(tmpId);
        } catch (ListAIException e) {
            e.printStackTrace();
            CommandExecutionResult result = new CommandExecutionResult("Error listing AI types!");
            result.triggerError(500);
            return result;
        }
    }

    @Override
    public void setParams(IDTO dto) {
        //not needed
    }

}
