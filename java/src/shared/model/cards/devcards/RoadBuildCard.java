package shared.model.cards.devcards;

import shared.definitions.DevCardType;

/**
 * @author Danny Harding
 */
public class RoadBuildCard extends DevelopmentCard{

    public RoadBuildCard() {
        this.type = DevCardType.ROAD_BUILD;
    }

    @Override
    public void playCard() {
//                  DevCardController.playRoadBuildCard();
    }
}
