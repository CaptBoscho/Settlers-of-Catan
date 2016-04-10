package shared.model.cards.resources;

import shared.definitions.ResourceType;

import java.io.Serializable;

/**
 * Object representation of a Wheat resource.
 *
 * @author Danny Harding
 */
public final class Wheat extends ResourceCard implements Serializable {

    public Wheat() {
        this.type = ResourceType.WHEAT;
    }
}
