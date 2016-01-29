package shared.model.devcards;

import shared.definitions.DevCardType;

/**
 * @author Danny Harding
 */
public class YearOfPlentyCard extends DevelopmentCard {

    YearOfPlentyCard() {
        this.type = DevCardType.YEAR_OF_PLENTY;
    }

    @Override
    public void playCard() throws InvalidCardTypeException {
//                  DevCardController.playYearOfPlentyCard();
    }
}
