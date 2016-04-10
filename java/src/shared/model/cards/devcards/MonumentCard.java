package shared.model.cards.devcards;

import shared.definitions.DevCardType;

import java.io.Serializable;

/**
 * @author Danny Harding
 */
public final class MonumentCard extends DevelopmentCard implements Serializable {

    public MonumentCard() {
        this.type = DevCardType.MONUMENT;
    }

    @Override
    public void playCard() {
//                  DevCardController.playMonumentCard();
    }
}
