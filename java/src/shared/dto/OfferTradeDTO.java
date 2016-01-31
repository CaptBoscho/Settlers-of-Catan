package shared.dto;

import com.google.gson.JsonObject;
import shared.model.JsonSerializable;
import shared.model.game.trade.Trade;

/**
 * @author Derek Argueta
 */
public class OfferTradeDTO implements JsonSerializable {

    int playerIndex;
    Trade offer;
    int receiver;

    /**
     *
     * @param playerIndex Who's sending the offer
     * @param offer       What you get (+) and what you give (-)
     * @param receiver    Who you're offering the trade to (0-3)
     */
    public OfferTradeDTO(int playerIndex, Trade offer, int receiver) {
        assert playerIndex >= 0;
        assert receiver >= 0;
        assert playerIndex != receiver;
        assert offer != null;
        this.playerIndex = playerIndex;
        this.offer = offer;
        this.receiver = receiver;
    }

    /**
     * Converts the object to JSON
     *
     * @return The JSON representation of the object
     */
    @Override
    public JsonObject toJSON() {
        return null;
    }
}
