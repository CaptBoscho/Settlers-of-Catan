package shared.model.cards.devcards;

import shared.definitions.DevCardType;

import java.io.Serializable;

/**
 * @author Danny Harding
 */
public final class RoadBuildCard extends DevelopmentCard implements Serializable {

    public RoadBuildCard() {
        this.type = DevCardType.ROAD_BUILD;
    }

    @Override
    public void playCard() {
//                  DevCardController.playRoadBuildCard();
    }
}
