package shared.dto;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
    private int playerIndex;
    private String content;

    /**
     *
     * @param playerIndex The ID of the player who is sending the message
     * @param content  The actual message
     */
    public SendChatDTO(final int playerIndex, final String content) {
        assert playerIndex >= 0;
        assert playerIndex <= 3;
        assert content != null;
        assert content.length() > 0;

        this.playerIndex = playerIndex;
        this.content = content;
    }

    public SendChatDTO(final String json) {
        final JsonObject obj = new JsonParser().parse(json).getAsJsonObject();
        this.playerIndex = obj.get(kPlayerIndex).getAsInt();
        this.content = obj.get(kContent).getAsString();
    }

    public int getPlayerIndex(){
        return this.playerIndex;
    }

    public String getContent(){
        return this.content;
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
        obj.addProperty(kPlayerIndex, this.playerIndex);
        obj.addProperty(kContent, this.content);
        return obj;
    }

    public static boolean isValidRequestJson(String json) {
        final JsonObject obj = new JsonParser().parse(json).getAsJsonObject();
        final boolean hasType = obj.has(kType) && obj.get(kType).isJsonPrimitive();
        final boolean hasPlayerIndex = obj.has(kPlayerIndex) && obj.get(kPlayerIndex).isJsonPrimitive();
        final boolean hasContent = obj.has(kContent) && obj.get(kContent).isJsonPrimitive();

        return hasType && hasPlayerIndex && hasContent;
    }
}
