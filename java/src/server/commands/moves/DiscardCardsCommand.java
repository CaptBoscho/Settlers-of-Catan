package server.commands.moves;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import server.commands.CommandExecutionResult;
import server.commands.ICommand;
import server.exceptions.CommandExecutionFailedException;
import server.exceptions.DiscardCardsException;
import server.main.Config;
import shared.dto.CookieWrapperDTO;
import shared.dto.DiscardCardsDTO;
import shared.dto.IDTO;

import java.io.Serializable;

/**
 * A command object that discards cards
 *
 * @author Joel Bradley
 */
public final class DiscardCardsCommand implements Serializable, ICommand {

    private boolean paramsSet;
    private int gameId;
    private DiscardCardsDTO dto;

    /**
     * Communicates with the ServerFacade to carry out the Discard Cards command
     * @return IDTO
     */
    @Override
    public CommandExecutionResult execute() throws CommandExecutionFailedException {
        assert this.paramsSet;
        assert this.gameId >= 0;
        assert this.dto != null;

        try {
            // TODO - better API
            return Config.facade.discardCards(gameId, dto);
        }catch(DiscardCardsException e){
            throw new CommandExecutionFailedException(e.getMessage());
        }
    }

    @Override
    public void setParams(final IDTO dto) {
        this.paramsSet = true;
        final CookieWrapperDTO cookieDTO = (CookieWrapperDTO)dto;
        this.dto = (DiscardCardsDTO)cookieDTO.getDto();
        this.gameId = cookieDTO.getGameId();
    }

    @Override
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("type","DiscardCards");
        json.addProperty("gameId", gameId);
        json.add("discardDto", dto.toJSON());
        return json;
    }

    @Override
    public void getFromJson(String json){
        final JsonObject obj = new JsonParser().parse(json).getAsJsonObject();
        gameId = obj.get("gameId").getAsInt();
        JsonObject discard = obj.getAsJsonObject("discardDto");
        dto = new DiscardCardsDTO(discard.getAsString());
    }
}
