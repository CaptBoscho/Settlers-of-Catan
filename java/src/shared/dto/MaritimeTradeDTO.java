package shared.dto;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import server.utils.JSONUtils;
import shared.model.JsonSerializable;

/**
 * @author Derek Argueta
 */
public final class MaritimeTradeDTO implements IDTO, JsonSerializable {

    // -- JSON keys
    private static final String kType = "type";
    private static final String kPlayerIndex = "playerIndex";
    private static final String kRatio = "ratio";
    private static final String kInputResource = "inputResource";
    private static final String kOutputResource = "outputResource";

    // -- class members
    private int playerIndex;
    private int ratio;
    private String inputResource;
    private String outputResource;

    /**
     *
     * @param playerIndex    Who's doing the trading
     * @param ratio          (<i>optional</i>) The ratio of the trade your doing as an integer (ie. put 3 for a 3:1 trade)
     * @param inputResource  (<i>optional</i>) What type of resource you're giving
     * @param outputResource (<i>optional</i>) What type of resource you're getting
     */
    public MaritimeTradeDTO(final int playerIndex, final int ratio, final String inputResource, final String outputResource) {
        assert playerIndex >= 0;
        assert inputResource != null;
        assert outputResource != null;

        this.playerIndex = playerIndex;
        this.ratio = ratio;
        this.inputResource = inputResource;
        this.outputResource = outputResource;
    }

    public MaritimeTradeDTO(final String json) {
        final JsonObject obj = new JsonParser().parse(json).getAsJsonObject();
        this.playerIndex = obj.get(kPlayerIndex).getAsInt();
        this.ratio = obj.get(kRatio).getAsInt();
        this.inputResource = obj.get(kInputResource).getAsString();
        this.outputResource = obj.get(kOutputResource).getAsString();
    }

    public String getInputResource() {
        return inputResource;
    }

    public int getPlayerIndex() {
        return playerIndex;
    }

    public int getRatio() {
        return ratio;
    }

    public String getOutputResource() {
        return outputResource;
    }

    /**
     * Converts the object to JSON
     *
     * @return The JSON representation of the object
     */
    @Override
    public JsonObject toJSON() {
        final JsonObject obj = new JsonObject();
        obj.addProperty(kType, "maritimeTrade");
        obj.addProperty(kPlayerIndex, this.playerIndex);
        obj.addProperty(kRatio, this.ratio);
        obj.addProperty(kInputResource, this.inputResource);
        obj.addProperty(kOutputResource, this.outputResource);
        return obj;
    }

    public static boolean isValidRequestJson(final String json) {
        if(!JSONUtils.isJSONValid(json)) {
            return false;
        }
        final JsonObject obj = new JsonParser().parse(json).getAsJsonObject();
        final boolean hasType = obj.has(kType) && obj.get(kType).isJsonPrimitive();
        final boolean hasPlayerIndex = obj.has(kPlayerIndex) && obj.get(kPlayerIndex).isJsonPrimitive();
        final boolean hasRatio = obj.has(kRatio) && obj.get(kRatio).isJsonPrimitive();
        final boolean hasInputResource = obj.has(kInputResource) && obj.get(kInputResource).isJsonPrimitive();
        final boolean hasOutputResource = obj.has(kOutputResource) && obj.get(kOutputResource).isJsonPrimitive();

        return hasType && hasPlayerIndex && hasRatio && hasInputResource && hasOutputResource;
    }
}
