package shared.model.cards.resources;

import shared.definitions.ResourceType;
import shared.model.cards.Card;

/**
 * @author Danny Harding
 */
public abstract class ResourceCard extends Card {
    protected ResourceType type;

    public ResourceType getType() {
        return type;
    }
}
