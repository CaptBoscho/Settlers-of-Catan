package server.commands.game;

import server.commands.CommandExecutionResult;
import server.commands.ICommand;
import server.exceptions.AddAIException;
import server.exceptions.CommandExecutionFailedException;
import server.main.Config;
import shared.dto.AddAIDTO;
import shared.dto.CookieWrapperDTO;
import shared.dto.IDTO;
import shared.model.ai.AIType;

/**
 * A command object that adds an AI
 *
 * @author Joel Bradley
 */
public class AddAICommand implements ICommand {

    private int gameId;
    private AIType type;

    /**
     * Communicates with the ServerFacade to carry out the Add AI command
     * @return IDTO
     */
    @Override
    public CommandExecutionResult execute() throws CommandExecutionFailedException {
        try {
            return Config.facade.addAI(this.gameId, this.type);
        } catch (AddAIException e) {
            throw new CommandExecutionFailedException(e.getMessage());
        }
    }

    @Override
    public void setParams(IDTO dto) {
        final CookieWrapperDTO cookieDTO = (CookieWrapperDTO)dto;
        final AddAIDTO tmpDTO = (AddAIDTO) cookieDTO.getDto();
        this.gameId = cookieDTO.getGameId();
        this.type = tmpDTO.getAIType();
    }
}