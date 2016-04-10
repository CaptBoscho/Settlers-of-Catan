package shared.model.cards.devcards;

import shared.definitions.DevCardType;

import java.io.Serializable;

/**
 * @author Danny Harding
 */
public final class SoldierCard extends DevelopmentCard implements Serializable {

    public SoldierCard() {
        this.type = DevCardType.SOLDIER;
    }

    @Override
    public void playCard() {
//                  DevCardController.playSoldierCard();
    }
}
