package client.map.states;

import client.data.RobPlayerInfo;
import client.map.MapController;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexLocation;

/**
 * @author Kyle Cornelison
 *
 * Represents Robbing a Player State
 */
public class RobbingState extends MapState {

    /**
     * Constructor
     */
    public RobbingState(MapController mapController){
        super(mapController);
    }

    @Override
    public boolean canPlaceRoad(EdgeLocation edgeLoc) {
        return false;
    }

    @Override
    public boolean canPlaceSettlement(VertexLocation vertLoc) {
        return false;
    }

    @Override
    public boolean canPlaceCity(VertexLocation vertLoc) {
        return false;
    }

    /**
     * Place Road - State Implementation
     *
     * @param edgeLoc
     */
    @Override
    public void placeRoad(EdgeLocation edgeLoc) {
        super.placeRoad(edgeLoc);
    }

    /**
     * Place Settlement - State Implementation
     *
     * @param vertLoc
     */
    @Override
    public void placeSettlement(VertexLocation vertLoc) {
        super.placeSettlement(vertLoc);
    }

    /**
     * Place City - State Implementation
     *
     * @param vertLoc
     */
    @Override
    public void placeCity(VertexLocation vertLoc) {
        super.placeCity(vertLoc);
    }

    /**
     * Place Robber - State Implementation
     *
     * @param hexLoc
     */
    @Override
    public void placeRobber(HexLocation hexLoc) {
        super.placeRobber(hexLoc);
    }

    /**
     * Play Soldier - State Implementation
     */
    @Override
    public void playSoldierCard() {
        super.playSoldierCard();
    }

    /**
     * Play RoadBuilding - State Implementation
     */
    @Override
    public void playRoadBuildingCard() {
        super.playRoadBuildingCard();
    }

    /**
     * Rob Player - State Implementation
     *
     * @param victim
     */
    @Override
    public void robPlayer(RobPlayerInfo victim) {
        super.robPlayer(victim);
    }
}
