package shared.model.game.trade;

import shared.definitions.ResourceType;

import shared.model.cards.resources.ResourceCard;
import shared.definitions.ResourceType;

import java.util.ArrayList;
import java.util.List;

/**
 * A package of ResourceCards along with a userID
 *
 * @author Danny Harding
 */
public class TradePackage {
    private int playerID;
    private List<ResourceType> resources;

    /**
     * Creates a package for the Trade class to be able to trade.
     * @param playerID the ID of the player who is trading.  -1 for port.
     * @param resources An ArrayList of the ResourceCards to be traded
     */
    public TradePackage(int playerID, List<ResourceType> resources) {
        this.playerID = playerID;
        this.resources = resources;
    }

    /**
     * Gives ResourceCards in the TradePackage to the user indicated by the TradePackage's playerID
     */
    public void disburseResources() {

    }

    public void setResources(List<ResourceType> resources) {
        this.resources = resources;
    }

    public List<ResourceType> getResources() {
        return resources;
    }

    public int getUserID() {
        return playerID;
    }
}
