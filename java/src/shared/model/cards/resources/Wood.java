package shared.model.cards.resources;

import shared.definitions.ResourceType;

import java.io.Serializable;

/**
 * Object representation of a Wood resource.
 *
 * @author Danny Harding
 */
public final class Wood extends ResourceCard implements Serializable {

    public Wood() {
        this.type = ResourceType.WOOD;
    }
}
