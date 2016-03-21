package com.dargueta.shared.model.cards.devcards;

import com.dargueta.shared.definitions.DevCardType;
import com.dargueta.shared.model.cards.Card;

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
