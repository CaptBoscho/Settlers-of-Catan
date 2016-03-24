package server.commands.game;


import server.commands.CommandExecutionResult;
import server.commands.ICommand;
import server.exceptions.ListAIException;
import server.main.Config;
import shared.dto.CookieWrapperDTO;
import shared.dto.IDTO;

/**
 * A command object that list the AI
 *
 * @author Joel Bradley
 */
public final class ListAICommand implements ICommand {

    private boolean paramsSet = false;
    private int gameId;

    /**
     * Communicates with the ServerFacade to carry out the List AI command
     *
     * @return CommandExecutionResult
     */
    @Override
    public CommandExecutionResult execute() {
        assert this.paramsSet;
        assert this.gameId >= 0;
        try {
            return Config.facade.listAI(gameId);
        } catch (ListAIException e) {
            e.printStackTrace();
            CommandExecutionResult result = new CommandExecutionResult("Error listing AI types!");
            result.triggerError(500);
            return result;
        }
    }

    @Override
    public void setParams(final IDTO dto) {
        assert dto != null;

        this.paramsSet = true;
        final CookieWrapperDTO cookieDTO = (CookieWrapperDTO)dto;
        this.gameId = cookieDTO.getGameId();
    }
}
