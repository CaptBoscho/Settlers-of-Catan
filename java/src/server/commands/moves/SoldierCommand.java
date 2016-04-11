package server.commands.moves;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import server.commands.CommandExecutionResult;
import server.commands.ICommand;
import server.exceptions.CommandExecutionFailedException;
import server.exceptions.SoldierException;
import server.main.Config;
import shared.dto.CookieWrapperDTO;
import shared.dto.IDTO;
import shared.dto.PlaySoldierCardDTO;
import shared.locations.HexLocation;

import java.io.Serializable;

/**
 * A command object that plays a soldier card
 *
 * @author Joel Bradley
 */
public final class SoldierCommand implements Serializable, ICommand {

    private boolean paramsSet = false;
    private int gameId;
    private int playerIndex;
    private HexLocation location;
    private int victimIndex;

    /**
     * Communicates with the ServerFacade to carry out the Soldier command
     * @return IDTO
     */
    @Override
    public CommandExecutionResult execute() throws CommandExecutionFailedException {
        assert this.paramsSet;
        assert this.gameId >= 0;
        assert this.playerIndex >= 0;
        assert this.playerIndex < 4;
        assert this.location != null;
        assert this.victimIndex >= 0;
        assert this.victimIndex < 4;

        try {
            return Config.facade.soldier(this.gameId, this.playerIndex, this.location, this.victimIndex);
        } catch (SoldierException e) {
            throw new CommandExecutionFailedException(e.getMessage());
        }
    }

    @Override
    public void setParams(final IDTO dto) {
        assert dto != null;

        this.paramsSet = true;
        final CookieWrapperDTO cookieDTO = (CookieWrapperDTO)dto;
        final PlaySoldierCardDTO tmpDTO = (PlaySoldierCardDTO)cookieDTO.getDto();
        this.gameId = cookieDTO.getGameId();
        this.playerIndex = tmpDTO.getPlayerIndex();
        this.location = tmpDTO.getLocation();
        this.victimIndex = tmpDTO.getVictimIndex();
    }

    @Override
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("type","Soldier");
        json.addProperty("gameId", gameId);
        json.addProperty("playerIndex", playerIndex);
        json.add("hexLocation",location.toJSON());
        json.addProperty("victim",victimIndex);
        return json;
    }

    @Override
    public void getFromJson(String json){
        final JsonObject obj = new JsonParser().parse(json).getAsJsonObject();
        gameId = obj.get("gameId").getAsInt();
        playerIndex = obj.get("playerIndex").getAsInt();
        location = new HexLocation(obj.getAsJsonObject("hexLocation"));
        victimIndex = obj.get("victim").getAsInt();
    }

}
