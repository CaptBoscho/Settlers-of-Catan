package shared.model.game.trade;

import com.google.gson.JsonObject;
import shared.model.player.Player;

/**
 * Represents a trade done between two players
 *
 * @author Danny Harding
 */
public class Trade {
    TradePackage package1;
    TradePackage package2;

    public Trade(TradePackage package1, TradePackage package2) {

    }

    /**
     * Constructs a Trade object from a JSON blob
     *
     * @param json the JSON representation of the object
     */
    public Trade(JsonObject json) {

    }

    /**
     * Gives resources from package1 to Player from package2 and vice versa.
     */
    public void switchResources() {

    }

    /**
     * Converts the object to JSON
     *
     * @return a JSON representation of the object
     */
    public JsonObject toJSON() {
        return null;
    }
}
