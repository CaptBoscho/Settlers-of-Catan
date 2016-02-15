package shared.model.game.trade;

import shared.definitions.ResourceType;

import java.util.List;

/**
 * A package of ResourceCards along with a userID
 *
 * @author Danny Harding
 */
public final class TradePackage {
    private int playerID;
    private List<ResourceType> resources;

    /**
     * Creates a package for the Trade class to be able to trade.
     * @param playerID the ID of the player who is trading.  -1 for port.
     * @param resources An ArrayList of the ResourceCards to be traded
     */
    public TradePackage(int playerID, List<ResourceType> resources) {
        assert playerID >= -1;
        assert resources != null;
        assert resources.size() > 0;

        this.playerID = playerID;
        this.resources = resources;
    }

    /**
     * Gives ResourceCards in the TradePackage to the user indicated by the TradePackage's playerID
     */
    public void disperseResources() {

    }

    @Override
    public boolean equals(Object o) {
        if(o == null) return false;
        if(!TradePackage.class.isAssignableFrom(o.getClass())) return false;
        final TradePackage other = (TradePackage)o;
        if(playerID != other.getUserID()) return false;
        if(resources.size() != other.getResources().size()) return false;
        for(int i = 0; i < resources.size(); i++) {
            if(resources.get(i) != other.getResources().get(i)) {
                return false;
            }
        }
        return true;
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
