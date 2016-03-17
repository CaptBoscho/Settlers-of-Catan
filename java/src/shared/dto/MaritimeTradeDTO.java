package shared.dto;

import com.google.gson.JsonObject;
import shared.model.JsonSerializable;

/**
 * @author Derek Argueta
 */
public final class MaritimeTradeDTO implements IDTO,JsonSerializable {

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
    public MaritimeTradeDTO(int playerIndex, int ratio, String inputResource, String outputResource) {
        assert playerIndex >= 0;
        assert inputResource != null;
        assert outputResource != null;

        this.playerIndex = playerIndex;
        this.ratio = ratio;
        this.inputResource = inputResource;
        this.outputResource = outputResource;
    }

    /**
     * Converts the object to JSON
     *
     * @return The JSON representation of the object
     */
    @Override
    public JsonObject toJSON() {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", "maritimeTrade");
        obj.addProperty("playerIndex", this.playerIndex);
        obj.addProperty("ratio", this.ratio);
        obj.addProperty("inputResource", this.inputResource);
        obj.addProperty("outputResource", this.outputResource);
        return obj;
    }
}
