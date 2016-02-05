package shared.model.cards.resources;

import shared.definitions.ResourceType;

/**
 * Object representation of an Ore resource.
 *
 * @author Danny Harding
 */
public class Ore extends ResourceCard {

    public Ore() {
        this.type = ResourceType.ORE;
    }
}
