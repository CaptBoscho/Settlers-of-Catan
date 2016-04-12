package server.commands.moves;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import server.commands.CommandExecutionResult;
import server.commands.ICommand;
import server.exceptions.BuildCityException;
import server.exceptions.CommandExecutionFailedException;
import server.main.Config;
import shared.dto.BuildCityDTO;
import shared.dto.CookieWrapperDTO;
import shared.dto.IDTO;
import shared.locations.VertexLocation;

import java.io.Serializable;

/**
 * A command object that builds a city
 *
 * @author Joel Bradley
 */
public final class BuildCityCommand implements Serializable, ICommand {

    private int gameId;
    private int playerIndex;
    private VertexLocation location;

    /**
     * Communicates with the ServerFacade to carry out the Build City command
     * @return IDTO
     */
    @Override
    public CommandExecutionResult execute() throws CommandExecutionFailedException {
        assert this.gameId >= 0;
        assert this.playerIndex >= 0;
        assert this.playerIndex < 4;
        assert this.location != null;

        try {
            return Config.facade.buildCity(this.gameId, this.playerIndex, this.location);
        } catch (BuildCityException e) {
            throw new CommandExecutionFailedException(e.getMessage());
        }
    }

    @Override
    public void setParams(final IDTO dto) {
        final CookieWrapperDTO cookieDTO = (CookieWrapperDTO)dto;
        final BuildCityDTO tmpDTO = (BuildCityDTO)cookieDTO.getDto();
        this.gameId = cookieDTO.getGameId();
        this.playerIndex = tmpDTO.getPlayerIndex();
        this.location = tmpDTO.getLocation();
    }

    @Override
    public JsonObject toJson(){
        JsonObject json = new JsonObject();
        json.addProperty("type","BuildCity");
        json.addProperty("gameId", gameId);
        json.addProperty("playerIndex",playerIndex);
        json.add("vertexLocation", location.toJSON());
        return json;
    }

    @Override
    public void getFromJson(String json){
        final JsonObject obj = new JsonParser().parse(json).getAsJsonObject();
        gameId = obj.get("gameId").getAsInt();
        playerIndex = obj.get("playerIndex").getAsInt();
        location = new VertexLocation(obj.getAsJsonObject("vertexLocation"));
    }

}
