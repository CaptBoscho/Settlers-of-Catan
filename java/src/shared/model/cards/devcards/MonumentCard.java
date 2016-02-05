package shared.model.cards.devcards;

import shared.definitions.DevCardType;

/**
 * @author Danny Harding
 */
public class MonumentCard extends DevelopmentCard {

    public MonumentCard() {
        this.type = DevCardType.MONUMENT;
    }

    @Override
    public void playCard() {
//                  DevCardController.playMonumentCard();
    }
}
