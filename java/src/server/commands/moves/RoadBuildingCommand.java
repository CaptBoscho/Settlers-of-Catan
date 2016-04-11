package server.commands.moves;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import server.commands.CommandExecutionResult;
import server.commands.ICommand;
import server.exceptions.CommandExecutionFailedException;
import server.exceptions.RoadBuildingException;
import server.main.Config;
import shared.dto.CookieWrapperDTO;
import shared.dto.IDTO;
import shared.dto.RoadBuildingDTO;
import shared.locations.EdgeLocation;

import java.io.Serializable;

/**
 * A command object that plays a road building card
 *
 * @author Joel Bradley
 */
public final class RoadBuildingCommand implements Serializable, ICommand {

    private boolean paramsSet = false;
    private int gameId;
    private int playerIndex;
    private EdgeLocation locationOne;
    private EdgeLocation locationTwo;

    /**
     * Communicates with the ServerFacade to carry out the Road Building command
     * @return IDTO
     */
    @Override
    public CommandExecutionResult execute() throws CommandExecutionFailedException {
        assert this.paramsSet;
        assert this.gameId >= 0;
        assert this.playerIndex >= 0;
        assert this.playerIndex < 4;
        assert locationOne != null;
        assert locationTwo != null;

        try {
            return Config.facade.roadBuilding(this.gameId, this.playerIndex, this.locationOne, this.locationTwo);
        } catch (RoadBuildingException e) {
            throw new CommandExecutionFailedException(e.getMessage());
        }
    }

    @Override
    public void setParams(final IDTO dto) {
        assert dto != null;

        this.paramsSet = true;
        final CookieWrapperDTO cookieDTO = (CookieWrapperDTO)dto;
        final RoadBuildingDTO tmpDTO = (RoadBuildingDTO)cookieDTO.getDto();
        this.gameId = cookieDTO.getGameId();
        this.playerIndex = tmpDTO.getPlayerIndex();
        this.locationOne = tmpDTO.getRoadLocationOne();
        this.locationTwo = tmpDTO.getRoadLocationTwo();
    }

    @Override
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("gameId", gameId);
        json.addProperty("playerIndex", playerIndex);
        json.add("edgeLocationOne",locationOne.toJSON());
        json.add("edgeLocationTwo",locationTwo.toJSON());
        return json;
    }

    @Override
    public void getFromJson(String json){
        final JsonObject obj = new JsonParser().parse(json).getAsJsonObject();
        gameId = obj.get("gameId").getAsInt();
        playerIndex = obj.get("playerIndex").getAsInt();
        locationOne = new EdgeLocation(obj.getAsJsonObject("edgeLocationOne"));
        locationTwo = new EdgeLocation(obj.getAsJsonObject("edgeLocationTwo"));
    }

}
