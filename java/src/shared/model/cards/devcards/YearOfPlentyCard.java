package shared.model.cards.devcards;

import shared.definitions.DevCardType;

import java.io.Serializable;

/**
 * @author Danny Harding
 */
public final class YearOfPlentyCard extends DevelopmentCard implements Serializable {

    public YearOfPlentyCard() {
        this.type = DevCardType.YEAR_OF_PLENTY;
    }

    @Override
    public void playCard() {
//                  DevCardController.playYearOfPlentyCard();
    }
}
