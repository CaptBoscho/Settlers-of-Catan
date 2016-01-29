package shared.model.devcards;

import shared.definitions.DevCardType;

/**
 * @author Danny Harding
 */
public class MonopolyCard extends DevelopmentCard {

    MonopolyCard() {
        this.type = DevCardType.MONOPOLY;
    }

    @Override
    public void playCard() throws InvalidCardTypeException {
//                  DevCardController.playMonopolyCard();
    }
}
