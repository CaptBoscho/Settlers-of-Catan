package shared.dto;

import com.google.gson.JsonObject;
import shared.model.JsonSerializable;

/**
 * @author Derek Argueta
 */
public final class TradeOfferResponseDTO implements JsonSerializable {

    private int playerIndex;
    private boolean willAccept;

    /**
     *
     * @param playerIndex Who's accepting / rejecting this trade
     * @param willAccept  Whether the player accepted the trade or not
     */
    public TradeOfferResponseDTO(int playerIndex, boolean willAccept) {
        assert playerIndex >= 0;

        this.playerIndex = playerIndex;
        this.willAccept = willAccept;
    }

    /**
     * Converts the object to JSON
     *
     * @return The JSON representation of the object
     */
    @Override
    public JsonObject toJSON() {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", "acceptTrade");
        obj.addProperty("playerIndex", this.playerIndex);
        obj.addProperty("willAccept", this.willAccept);
        return obj;
    }
}
