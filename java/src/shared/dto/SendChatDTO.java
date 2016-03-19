package shared.dto;

import com.google.gson.JsonObject;
import shared.model.JsonSerializable;

/**
 * @author Derek Argueta
 */
public final class SendChatDTO implements IDTO, JsonSerializable {

    // -- JSON keys
    private static final String kType = "type";
    private static final String kPlayerIndex = "playerIndex";
    private static final String kContent = "content";

    // -- class members
    private int playerId;
    private String content;

    /**
     *
     * @param playerId The ID of the player who is sending the message
     * @param content  The actual message
     */
    public SendChatDTO(final int playerId, final String content) {
        assert playerId >= 0;
        assert playerId <= 3;
        assert content != null;
        assert content.length() > 0;

        this.playerId = playerId;
        this.content = content;
    }

    /**
     * Converts the object to JSON
     *
     * @return The JSON representation of the object
     */
    @Override
    public JsonObject toJSON() {
        final JsonObject obj = new JsonObject();
        obj.addProperty(kType, "sendChat");
        obj.addProperty(kPlayerIndex, this.playerId);
        obj.addProperty(kContent, this.content);
        return obj;
    }
}
