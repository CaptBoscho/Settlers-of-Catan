package shared.model.cards.resources;

import shared.definitions.ResourceType;
import shared.model.cards.Card;

import java.io.Serializable;

/**
 * @author Danny Harding
 */
public abstract class ResourceCard extends Card implements Serializable {
    protected ResourceType type;

    public ResourceType getType() {
        return type;
    }
}
