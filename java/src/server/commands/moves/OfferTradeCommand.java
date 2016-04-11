package server.commands.moves;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import server.commands.CommandExecutionResult;
import server.commands.ICommand;
import server.exceptions.CommandExecutionFailedException;
import server.exceptions.OfferTradeException;
import server.main.Config;
import shared.dto.CookieWrapperDTO;
import shared.dto.IDTO;
import shared.dto.OfferTradeDTO;

import java.io.Serializable;

/**
 * A command object that offers a trade
 *
 * @author Joel Bradley
 */
public final class OfferTradeCommand implements Serializable, ICommand {

    private boolean paramsSet = false;
    private int gameId;
    private OfferTradeDTO dto;

    /**
     * Communicates with the ServerFacade to carry out the Offer Trade command
     * @return IDTO
     */
    @Override
    public CommandExecutionResult execute() throws CommandExecutionFailedException {
        assert this.paramsSet;
        assert this.gameId >= 0;
        assert this.dto != null;

        try {
            // TODO - better API
            return Config.facade.offerTrade(this.gameId, this.dto);
        } catch(OfferTradeException e) {
            throw new CommandExecutionFailedException(e.getMessage());
        }
    }

    @Override
    public void setParams(final IDTO dto) {
        assert dto != null;

        this.paramsSet = true;
        final CookieWrapperDTO cookieDTO = (CookieWrapperDTO)dto;
        this.dto = (OfferTradeDTO)cookieDTO.getDto();
        this.gameId = cookieDTO.getGameId();
    }

    @Override
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("gameId", gameId);
        json.add("offer",dto.toJSON());
        return json;
    }

    @Override
    public void getFromJson(String json){
        final JsonObject obj = new JsonParser().parse(json).getAsJsonObject();
        gameId = obj.get("gameId").getAsInt();
        JsonObject offer = obj.getAsJsonObject("offer");
        dto = new OfferTradeDTO(offer.getAsString());
    }
}
