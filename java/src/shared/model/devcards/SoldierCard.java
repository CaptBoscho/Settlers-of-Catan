package shared.model.devcards;

import shared.definitions.DevCardType;

/**
 * @author Danny Harding
 */
public class SoldierCard extends DevelopmentCard {

    SoldierCard() {
        this.type = DevCardType.SOLDIER;
    }

    @Override
    public void playCard() throws InvalidCardTypeException {
//                  DevCardController.playSoldierCard();
    }
}
