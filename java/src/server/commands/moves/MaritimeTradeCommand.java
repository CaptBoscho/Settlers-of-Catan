package server.commands.moves;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import server.commands.CommandExecutionResult;
import server.commands.ICommand;
import server.exceptions.CommandExecutionFailedException;
import server.exceptions.MaritimeTradeException;
import server.main.Config;
import shared.dto.CookieWrapperDTO;
import shared.dto.IDTO;
import shared.dto.MaritimeTradeDTO;

import java.io.Serializable;

/**
 * A command object that maritime trades
 *
 * @author Joel Bradley
 */
public final class MaritimeTradeCommand implements Serializable, ICommand {

    private boolean paramsSet = false;
    private int gameId;
    private MaritimeTradeDTO dto;

    /**
     * Communicates with the ServerFacade to carry out the Maritime Trade command
     * @return IDTO
     */
    @Override
    public CommandExecutionResult execute() throws CommandExecutionFailedException {
        assert this.paramsSet;
        assert this.gameId >= 0;
        assert this.dto != null;

        try {
            return Config.facade.maritimeTrade(gameId, dto);
        } catch(MaritimeTradeException e) {
            throw new CommandExecutionFailedException(e.getMessage());
        }
    }

    @Override
    public void setParams(final IDTO dto) {
        assert dto != null;

        this.paramsSet = true;
        final CookieWrapperDTO cookieDTO = (CookieWrapperDTO) dto;
        this.dto = (MaritimeTradeDTO) cookieDTO.getDto();
        this.gameId = cookieDTO.getGameId();
    }

    @Override
    public JsonObject toJson(){
        JsonObject jj = dto.toJSON();
        jj.addProperty("type","MaritimeTrade");
        jj.addProperty("gameId",gameId);
        jj.add("maritime",dto.toJSON());
        return jj;
    }

    @Override
    public void getFromJson(String json){
        final JsonObject obj = new JsonParser().parse(json).getAsJsonObject();
        obj.get("gameId").getAsInt();
        JsonObject maritime = obj.getAsJsonObject("maritime");
        dto = new MaritimeTradeDTO(maritime.getAsString());
    }
}
