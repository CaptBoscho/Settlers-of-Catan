package shared.model.cards.devcards;

import shared.definitions.DevCardType;

import java.io.Serializable;

/**
 * @author Danny Harding
 */
public final class MonopolyCard extends DevelopmentCard implements Serializable {

    public MonopolyCard() {
        this.type = DevCardType.MONOPOLY;
    }

    @Override
    public void playCard() {
//                  DevCardController.playMonopolyCard();
    }
}
