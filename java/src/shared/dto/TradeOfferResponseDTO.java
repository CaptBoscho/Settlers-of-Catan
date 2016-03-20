package shared.dto;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import shared.model.JsonSerializable;

/**
 * @author Derek Argueta
 */
public final class TradeOfferResponseDTO implements IDTO,JsonSerializable {

    // -- JSON keys
    private static final String kType = "type";
    private static final String kPlayerIndex = "playerIndex";
    private static final String kWillAccept = "willAccept";

    // -- class members
    private int playerIndex;

    public boolean willAccept() {
        return willAccept;
    }

    public int getPlayerIndex() {
        return playerIndex;
    }

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

    public TradeOfferResponseDTO(final String json) {
        final JsonObject obj = new JsonParser().parse(json).getAsJsonObject();
        this.playerIndex = obj.get(kPlayerIndex).getAsInt();
        this.willAccept = obj.get(kWillAccept).getAsBoolean();
    }

    /**
     * Converts the object to JSON
     *
     * @return The JSON representation of the object
     */
    @Override
    public JsonObject toJSON() {
        JsonObject obj = new JsonObject();
        obj.addProperty(kType, "acceptTrade");
        obj.addProperty(kPlayerIndex, this.playerIndex);
        obj.addProperty(kWillAccept, this.willAccept);
        return obj;
    }
}
