package server.commands.moves;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import server.commands.CommandExecutionResult;
import server.commands.ICommand;
import server.exceptions.BuildRoadException;
import server.exceptions.CommandExecutionFailedException;
import server.main.Config;
import shared.dto.BuildRoadDTO;
import shared.dto.CookieWrapperDTO;
import shared.dto.IDTO;
import shared.locations.EdgeLocation;

import java.io.Serializable;

/**
 * A command object that builds a road
 *
 * @author Joel Bradley
 */
public final class BuildRoadCommand implements Serializable, ICommand {

    private int gameId;
    private int playerIndex;
    private EdgeLocation location;

    /**
     * Communicates with the ServerFacade to carry out the Build Road command
     * @return IDTO
     */
    @Override
    public CommandExecutionResult execute() throws CommandExecutionFailedException {
        assert this.gameId >= 0;
        assert this.playerIndex >= 0;
        assert this.playerIndex < 4;
        assert location != null;

        try {
            return Config.facade.buildRoad(this.gameId, this.playerIndex, this.location);
        } catch (BuildRoadException e) {
            throw new CommandExecutionFailedException(e.getMessage());
        }
    }

    @Override
    public void setParams(final IDTO dto) {
        final CookieWrapperDTO cookieDTO = (CookieWrapperDTO)dto;
        final BuildRoadDTO tmpDTO = (BuildRoadDTO)cookieDTO.getDto();
        this.gameId = cookieDTO.getGameId();
        this.playerIndex = tmpDTO.getPlayerIndex();
        this.location = tmpDTO.getRoadLocation();
    }

    @Override
    public JsonObject toJson(){
        JsonObject json = new JsonObject();
        json.addProperty("gameId", gameId);
        json.addProperty("playerIndex",playerIndex);
        json.add("edgeLocation",location.toJSON());
        return json;
    }

    @Override
    public void getFromJson(String json){
        final JsonObject obj = new JsonParser().parse(json).getAsJsonObject();
        gameId = obj.get("gameId").getAsInt();
        playerIndex = obj.get("playerIndex").getAsInt();
        location = new EdgeLocation(obj.getAsJsonObject("edgeLocation"));
    }


}
