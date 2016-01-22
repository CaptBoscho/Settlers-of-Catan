package shared.model.game.trade;

import shared.model.resources.ResourceCard;

import java.util.ArrayList;

/**
 * A package of ResourceCards along with a userID
 * Created by Danny on 1/22/16.
 */
public class TradePackage {
    private int playerID;
    private ArrayList<ResourceCard> resources;

    /**
     * Creates a package for the Trade class to be able to trade.
     * @param playerID the ID of the player who is trading.  -1 for port.
     * @param resources An ArrayList of the ResourceCards to be traded
     */
    public TradePackage(int playerID, ArrayList<ResourceCard> resources) {

    }

    public ArrayList<ResourceCard> getResources() {
        return resources;
    }

    public int getUserID() {
        return playerID;
    }
}
