package server.commands.game;

import server.commands.CommandExecutionResult;
import server.commands.ICommand;
import server.exceptions.AddAIException;
import server.main.Config;
import shared.dto.AddAIDTO;
import shared.dto.CookieWrapperDTO;
import shared.dto.IDTO;
import shared.model.ai.aimodel.AIType;

/**
 * A command object that adds an AI
 *
 * @author Joel Bradley
 */
public final class AddAICommand implements ICommand {

    private boolean paramsSet = false;
    private int gameId;
    private AIType type;

    /**
     * Communicates with the ServerFacade to carry out the Add AI command
     * @return IDTO
     */
    @Override
    public CommandExecutionResult execute() {
        assert this.paramsSet;
        assert this.gameId >= 0;
        assert this.type != null;

        try {
            return Config.facade.addAI(this.gameId, this.type);
        } catch (AddAIException e) {
            // this is so dumb. ugh.
            e.printStackTrace();
        }

        // TODO - SO DUMB
        return null;
    }

    @Override
    public void setParams(IDTO dto) {
        assert dto != null;

        this.paramsSet = true;
        final CookieWrapperDTO cookieDTO = (CookieWrapperDTO)dto;
        final AddAIDTO tmpDTO = (AddAIDTO) cookieDTO.getDto();
        this.gameId = cookieDTO.getGameId();
        this.type = tmpDTO.getAIType();
    }
}