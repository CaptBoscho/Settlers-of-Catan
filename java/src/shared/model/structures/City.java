package shared.model.structures;

import shared.definitions.ResourceType;

/**
 * A City is a second-level structure for players, and can only be built from a previously existing settlement.
 *
 * @author Danny Harding
 */
public class City extends Building {

    /**
     * Adds two of the given resource type to a Player's ResourceCardBank
     * @param resource
     */
    @Override
    public void addResources(ResourceType resource) {
        super.addResources(resource);
    }
}