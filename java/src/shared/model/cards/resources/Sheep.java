package shared.model.cards.resources;

import shared.definitions.ResourceType;

import java.io.Serializable;

/**
 * Object representation of a Sheep resource.
 *
 * @author Danny Harding
 */
public final class Sheep extends ResourceCard implements Serializable {

    public Sheep() {
        this.type = ResourceType.SHEEP;
    }
}
