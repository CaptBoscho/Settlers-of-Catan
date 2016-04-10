package shared.dto;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import shared.model.JsonSerializable;
import shared.model.game.trade.Trade;

import java.io.Serializable;

/**
 * @author Derek Argueta
 */
public final class OfferTradeDTO implements Serializable, IDTO, JsonSerializable {

    // -- JSON keys
    private static final String kType = "type";
    private static final String kPlayerIndex = "playerIndex";
    private static final String kReceiver = "receiver";
    private static final String kOffer = "offer";

    // -- class members
    private int playerIndex;
    private Trade offer;
    private int receiver;

    /**
     *
     * @param playerIndex Who's sending the offer
     * @param offer       What you get (+) and what you give (-)
     * @param receiver    Who you're offering the trade to (0-3)
     */
    public OfferTradeDTO(final int playerIndex, final Trade offer, final int receiver) {
        assert playerIndex >= 0;
        assert playerIndex < 4;
        assert receiver >= 0;
        assert playerIndex != receiver;
        assert offer != null;

        this.playerIndex = playerIndex;
        this.offer = offer;
        this.receiver = receiver;
    }

    public OfferTradeDTO(final String json) {
        final JsonObject obj = new JsonParser().parse(json).getAsJsonObject();
        this.playerIndex = obj.get(kPlayerIndex).getAsInt();
        this.offer = new Trade(obj.get(kOffer).getAsJsonObject());
        this.receiver = obj.get(kReceiver).getAsInt();
        this.offer.setSender(playerIndex);
        this.offer.setReceiver(receiver);
    }

    /**
     * Converts the object to JSON
     *
     * @return The JSON representation of the object
     */
    @Override
    public JsonObject toJSON() {
        final JsonObject obj = new JsonObject();
        obj.addProperty(kType, "offerTrade");
        obj.addProperty(kPlayerIndex, this.playerIndex);
        obj.addProperty(kReceiver, this.receiver);
        obj.add(kOffer, this.offer.resourceListToJson());
        return obj;
    }

    public int getSender() {
        return playerIndex;
    }

    public int getReceiver() {
        return receiver;
    }

    public Trade getOffer() {
        return offer;
    }
}
