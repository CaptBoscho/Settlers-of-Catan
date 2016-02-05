package shared.model.cards.devcards;

import shared.definitions.DevCardType;
import shared.model.cards.Card;

/**
 * @author Danny Harding
 */
public abstract class DevelopmentCard extends Card {

    protected DevCardType type;

    /**
     * Plays a Development Card based on its DevCardType
     */
    public void playCard() {}

    public DevCardType getType() {
        return type;
    }
}
