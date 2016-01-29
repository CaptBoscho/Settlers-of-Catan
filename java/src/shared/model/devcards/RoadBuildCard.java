package shared.model.devcards;

import shared.definitions.DevCardType;

/**
 * @author Danny Harding
 */
public class RoadBuildCard extends DevelopmentCard{

    RoadBuildCard() {
        this.type = DevCardType.ROAD_BUILD;
    }

    @Override
    public void playCard() throws InvalidCardTypeException {
//                  DevCardController.playRoadBuildCard();
    }
}
