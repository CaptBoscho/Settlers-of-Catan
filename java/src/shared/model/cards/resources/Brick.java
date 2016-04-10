package shared.model.cards.resources;

import shared.definitions.ResourceType;

import java.io.Serializable;

/**
 * Object representation of a Brick resource.
 *
 * @author Danny Harding
 */
public final class Brick extends ResourceCard implements Serializable {

    public Brick() {
        this.type = ResourceType.BRICK;
    }
}
