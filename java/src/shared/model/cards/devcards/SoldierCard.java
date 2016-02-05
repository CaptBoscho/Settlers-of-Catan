package shared.model.cards.devcards;

import shared.definitions.DevCardType;

/**
 * @author Danny Harding
 */
public class SoldierCard extends DevelopmentCard {

    public SoldierCard() {
        this.type = DevCardType.SOLDIER;
    }

    @Override
    public void playCard() {
//                  DevCardController.playSoldierCard();
    }
}
