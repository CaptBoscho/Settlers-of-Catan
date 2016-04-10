package shared.model.cards.resources;

import shared.definitions.ResourceType;

import java.io.Serializable;

/**
 * Object representation of an Ore resource.
 *
 * @author Danny Harding
 */
public final class Ore extends ResourceCard implements Serializable {

    public Ore() {
        this.type = ResourceType.ORE;
    }
}
