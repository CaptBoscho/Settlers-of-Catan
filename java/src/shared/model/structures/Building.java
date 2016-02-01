package shared.model.structures;

import shared.definitions.ResourceType;

/**
 * An abstract parent class for Cities and Settlements
 *
 * @author Danny Harding
 */
public abstract class Building {

    int playerID;

    public Building (int playerID) {
        this.playerID = playerID;
    }
    /**
     * Gives resources to the Player designated by playerID
     * @param resource
     */
    public void addResources(ResourceType resource) {}

    public int getPlayerID() {
        return playerID;
    }
}
