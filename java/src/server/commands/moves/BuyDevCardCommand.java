package server.commands.moves;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import server.commands.CommandExecutionResult;
import server.commands.ICommand;
import server.exceptions.BuyDevCardException;
import server.exceptions.CommandExecutionFailedException;
import server.main.Config;
import shared.dto.BuyDevCardDTO;
import shared.dto.CookieWrapperDTO;
import shared.dto.IDTO;
import shared.locations.VertexLocation;

import java.io.Serializable;

/**
 * A command object that buys a development card
 *
 * @author Joel Bradley
 */
public final class BuyDevCardCommand implements Serializable, ICommand {

    private boolean paramsSet = false;
    private int gameId;
    private int playerIndex;

    /**
     * Communicates with the ServerFacade to carry out the Buy Development Card command
     * @return IDTO
     */
    @Override
    public CommandExecutionResult execute() throws CommandExecutionFailedException {
        assert this.paramsSet;
        assert this.gameId >= 0;
        assert this.playerIndex >= 0;
        assert this.playerIndex < 4;

        try {
            return Config.facade.buyDevCard(this.gameId, this.playerIndex);
        } catch (BuyDevCardException e) {
            throw new CommandExecutionFailedException(e.getMessage());
        }
    }

    @Override
    public void setParams(final IDTO dto) {
        this.paramsSet = true;
        final CookieWrapperDTO cookieDTO = (CookieWrapperDTO)dto;
        final BuyDevCardDTO tmpDTO = (BuyDevCardDTO)cookieDTO.getDto();
        this.gameId = cookieDTO.getGameId();
        this.playerIndex = tmpDTO.getPlayerIndex();
    }

    @Override
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("gameId", gameId);
        json.addProperty("playerIndex", playerIndex);
        return json;
    }

    @Override
    public void getFromJson(String json){
        final JsonObject obj = new JsonParser().parse(json).getAsJsonObject();
        gameId = obj.get("gameId").getAsInt();
        playerIndex = obj.get("playerIndex").getAsInt();
    }
}
