package shared.model.devcards;

import client.devcards.DevCardController;
import shared.definitions.DevCardType;

/**
 * @author Danny Harding
 */
public abstract class DevelopmentCard {

    protected DevCardType type;

    /**
     * Plays a Development Card based on its DevCardType
     */
    public void playCard() {}

    public DevCardType getType() {
        return type;
    }
}
