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
            return Config.facade.listAI(0);
        } catch (ListAIException e) {
            e.printStackTrace();
            return new CommandExecutionResult("Error listing AI types!");
        }
    }

    @Override
    public void setParams(IDTO dto) {
        //not needed
    }

}
