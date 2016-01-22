package shared.model.resources;

import shared.definitions.ResourceType;

/**
 * Created by Danny on 1/18/16.
 */
public abstract class ResourceCard {
    protected ResourceType type;

    public ResourceType getType() {
        return type;
    }
}
