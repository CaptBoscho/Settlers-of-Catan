package shared.model.devcards;

import shared.definitions.DevCardType;

/**
 * Created by Danny on 1/18/16.
 */
public abstract class DevelopmentCard {

    protected DevCardType type;

    /**
     * Plays a Development Card based on its DevCardType
     * @throws Exception
     */
    public void playCard() throws InvalidCardTypeException {
        switch (type) {
            case ROAD_BUILD:
//                DevCardController.playRoadBuildCard();
                break;
            case MONOPOLY:
//                DevCardController.playMonopolyCard();
                break;
            case MONUMENT:
//                DevCardController.playMonumentCard();
                break;
            case SOLDIER:
//                DevCardController.playSoldierCard();
                break;
            case YEAR_OF_PLENTY:
//                DevCardController.playYearOfPlentyCard();
                break;
            default:
                throw new InvalidCardTypeException("DevelopmentCard does not have a valid DevCardType");
        }
    }

    public class InvalidCardTypeException extends Throwable {
        public InvalidCardTypeException(String s) {
            super(s);
        }
    }
}
