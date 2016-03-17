package shared.dto;

import com.google.gson.JsonObject;
import shared.model.JsonSerializable;

/**
 * @author Derek Argueta
 */
public final class SendChatDTO implements IDTO,JsonSerializable {

    private int playerId;
    private String content;

    /**
     *
     * @param playerId The ID of the player who is sending the message
     * @param content  The actual message
     */
    public SendChatDTO(int playerId, String content) {
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
        JsonObject obj = new JsonObject();
        obj.addProperty("type", "sendChat");
        obj.addProperty("playerIndex", this.playerId);
        obj.addProperty("content", this.content);
        return obj;
    }
}
