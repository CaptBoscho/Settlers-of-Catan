package shared.model.cards.devcards;

import shared.definitions.DevCardType;

/**
 * @author Danny Harding
 */
public class MonopolyCard extends DevelopmentCard {

    public MonopolyCard() {
        this.type = DevCardType.MONOPOLY;
    }

    @Override
    public void playCard() {
//                  DevCardController.playMonopolyCard();
    }
}
