package shared.model.game.trade;

import com.google.gson.JsonObject;
import shared.definitions.ResourceType;
import shared.model.player.Player;
import shared.model.resources.ResourceCard;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a trade done between two players
 *
 * @author Danny Harding
 */
public class Trade {
    TradePackage package1;
    TradePackage package2;

    public Trade(TradePackage package1, TradePackage package2) {
        this.package1 = package1;
        this.package2 = package2;
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
     * @pre package1 and package2 both have at least 1 resource
     *
     * @post The resource(s) from package1 are now in package2, and vice versa
     */
    public void switchResources() {
        List<ResourceType> ghost = package1.getResources();
        package1.setResources(package2.getResources());
        package2.setResources(ghost); //todo JUnit tests
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
