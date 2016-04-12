package server.commands.game;

import com.google.gson.JsonObject;
import server.commands.CommandExecutionResult;
import server.commands.ICommand;
import server.exceptions.CommandExecutionFailedException;
import server.exceptions.GetModelException;
import server.main.Config;
import shared.dto.CookieWrapperDTO;
import shared.dto.GameModelDTO;
import shared.dto.IDTO;

/**
 * A command object that gets the game model
 *
 * @author Joel Bradley
 */
public final class ModelCommand implements ICommand {

    private int gameId;
    private int requestedVersion;

    public ModelCommand() {
        this.gameId = -1;
        this.requestedVersion = -2;
    }

    /**
     * Communicates with the ServerFacade to carry out the Model command
     * @return IDTO
     */
    @Override
    public CommandExecutionResult execute() throws CommandExecutionFailedException {
        assert this.gameId >= 0;
        assert this.requestedVersion >= -1;

        try {
            return Config.facade.getModel(this.gameId, this.requestedVersion);
        } catch (GetModelException e) {
            throw new CommandExecutionFailedException(e.getMessage());
        }
    }

    @Override
    public void setParams(final IDTO dto) {
        final CookieWrapperDTO tmpDto = (CookieWrapperDTO)dto;
        this.gameId = tmpDto.getGameId();

        final GameModelDTO gameModelDTO = (GameModelDTO)tmpDto.getDto();
        this.requestedVersion = gameModelDTO.getVersion();
    }

    @Override
    public JsonObject toJson() {
        return null;
    }

    @Override
    public void getFromJson(String json) {

    }

}
