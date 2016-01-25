package shared.model.structures;

import shared.definitions.ResourceType;

/**
 * A settlement is a first-level structure for a player, and provides the player with one victory point. Settlements
 * can be placed on Edge pieces.
 *
 * @author Danny Harding
 */
public class Settlement extends Building {

    /**
     * Adds one of the given resource type to a Player's ResourceCardBank
     * @param resource
     */
    @Override
    public void addResources(ResourceType resource) {
        super.addResources(resource);
    }
}
