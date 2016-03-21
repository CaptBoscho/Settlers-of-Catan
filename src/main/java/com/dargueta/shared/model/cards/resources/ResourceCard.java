package com.dargueta.shared.model.cards.resources;

import com.dargueta.shared.definitions.ResourceType;
import com.dargueta.shared.model.cards.Card;

/**
 * @author Danny Harding
 */
public abstract class ResourceCard extends Card {
    protected ResourceType type;

    public ResourceType getType() {
        return type;
    }
}
