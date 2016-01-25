package shared.model.resources;

import shared.definitions.ResourceType;

/**
 * @author Danny Harding
 */
public abstract class ResourceCard {
    protected ResourceType type;

    public ResourceType getType() {
        return type;
    }
}
