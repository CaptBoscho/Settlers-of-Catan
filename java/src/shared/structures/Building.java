package shared.structures;

import shared.definitions.ResourceType;
import shared.locations.VertexLocation;

/**
 * An abstract parent class for Cities and Settlements
 *
 * Created by Danny on 1/18/16.
 */
public abstract class Building {

    int playerID;
    /**
     * Adds resources to
     * @param resource
     */
    public void addResources(ResourceType resource) {}

    public int getPlayerID() {
        return playerID;
    }
}
