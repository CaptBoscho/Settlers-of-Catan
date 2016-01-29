package shared.model.devcards;

import shared.definitions.DevCardType;

/**
 * @author Danny Harding
 */
public class MonumentCard extends DevelopmentCard {

    MonumentCard() {
        this.type = DevCardType.MONUMENT;
    }

    @Override
    public void playCard() throws InvalidCardTypeException {
//                  DevCardController.playMonumentCard();
    }
}
