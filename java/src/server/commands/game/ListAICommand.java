package server.commands.game;


import server.commands.CommandExecutionResult;
import server.commands.ICommand;
import shared.dto.CookieWrapperDTO;
import shared.dto.IDTO;

/**
 * A command object that list the AI
 *
 * @author Joel Bradley
 */
public final class ListAICommand implements ICommand {

    private CookieWrapperDTO dto;

    /**
     * Communicates with the ServerFacade to carry out the List AI command
     *
     * @return CommandExecutionResult
     */
    @Override
    public CommandExecutionResult execute() {
        assert this.dto != null;
        return null;
    }

    @Override
    public void setParams(IDTO dto) {
        this.dto = (CookieWrapperDTO)dto;
    }

}
