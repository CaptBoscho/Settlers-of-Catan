package shared.model.resources;

import shared.definitions.ResourceType;

/**
 * Object representation of a Brick resource.
 *
 * @author Danny Harding
 */
public class Brick extends ResourceCard{

    public Brick() {
        this.type = ResourceType.BRICK;
    }
}
