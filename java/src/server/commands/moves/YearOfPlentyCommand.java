package server.commands.moves;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import server.commands.CommandExecutionResult;
import server.commands.ICommand;
import server.exceptions.CommandExecutionFailedException;
import server.exceptions.YearOfPlentyException;
import server.main.Config;
import shared.definitions.ResourceType;
import shared.dto.CookieWrapperDTO;
import shared.dto.IDTO;
import shared.dto.PlayYOPCardDTO;
import shared.locations.HexLocation;

import java.io.Serializable;

/**
 * A command object that plays a year of plenty card
 *
 * @author Joel Bradley
 */
public final class YearOfPlentyCommand implements Serializable, ICommand {

    private boolean paramsSet = false;
    private int gameId;
    private int playerIndex;
    private ResourceType resourceOne, resourceTwo;

    /**
     * Communicates with the ServerFacade to carry out the YearOfPlenty command
     * @return IDTO
     */
    @Override
    public CommandExecutionResult execute() throws CommandExecutionFailedException {
        assert this.paramsSet;
        assert this.gameId >= 0;
        assert this.playerIndex >= 0;
        assert this.playerIndex < 4;
        assert this.resourceOne != null;
        assert this.resourceTwo != null;

        try {
            return Config.facade.yearOfPlenty(this.gameId, this.playerIndex, this.resourceOne, this.resourceTwo);
        } catch (YearOfPlentyException e) {
            throw new CommandExecutionFailedException("YearOfPlentyCommand failed to execute properly");
        }
    }

    @Override
    public void setParams(final IDTO dto) {
        assert dto != null;
        assert dto instanceof CookieWrapperDTO;

        this.paramsSet = true;
        final CookieWrapperDTO cookieDTO = (CookieWrapperDTO)dto;
        final PlayYOPCardDTO tmpDTO = (PlayYOPCardDTO)cookieDTO.getDto();
        this.gameId = cookieDTO.getGameId();
        this.playerIndex = tmpDTO.getPlayerIndex();
        this.resourceOne = tmpDTO.getResource1();
        this.resourceTwo = tmpDTO.getResource2();
    }

    @Override
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("type","YearOfPlenty");
        json.addProperty("gameId", gameId);
        json.addProperty("playerIndex", playerIndex);
        json.addProperty("resourceOne",resourceOne.toString());
        json.addProperty("resourceTwo",resourceTwo.toString());
        return json;
    }

    @Override
    public void getFromJson(String json){
        final JsonObject obj = new JsonParser().parse(json).getAsJsonObject();
        gameId = obj.get("gameId").getAsInt();
        playerIndex = obj.get("playerIndex").getAsInt();
        resourceOne = ResourceType.translateFromString(obj.get("resourceOne").getAsString());
        resourceTwo = ResourceType.translateFromString(obj.get("resourceTWo").getAsString());
    }

}
