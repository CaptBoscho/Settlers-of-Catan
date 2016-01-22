package shared.model.structures;

import shared.definitions.ResourceType;

/**
 * Created by Danny on 1/18/16.
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
