package client.map.states;

import client.data.RobPlayerInfo;
import client.map.MapController;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexLocation;

/**
 * @author Joel Bradley
 *
 * Represents Setup 2 State
 */
public class SetupTwoState extends MapState {

    /**
     * Constructor
     */
    public SetupTwoState(MapController mapController){
        super(mapController);
    }

    @Override
    public boolean canPlaceRoad(EdgeLocation edgeLoc) {
        edgeLoc = getModelEdgeLocation(edgeLoc);
        return facade.canInitiateRoad(userCookie.getPlayerId(), edgeLoc);
    }

    @Override
    public boolean canPlaceSettlement(VertexLocation vertLoc) {
        vertLoc = getModelVertexLocation(vertLoc);
        return facade.canInitiateSettlement(userCookie.getPlayerId(), vertLoc);
    }

    @Override
    public boolean canPlaceCity(VertexLocation vertLoc) {
        vertLoc = getModelVertexLocation(vertLoc);
        return false;
    }

    @Override
    public boolean canPlaceRobber(HexLocation hexLoc) {
        hexLoc = getModelHexLocation(hexLoc);
        return false;
    }

    @Override
    public void placeRoad(EdgeLocation edgeLoc) {
        if(canPlaceRoad(edgeLoc)) {
            facade.initiateRoad(userCookie.getPlayerId(), edgeLoc);
        }
    }

    @Override
    public void placeSettlement(VertexLocation vertLoc) {
        if(canPlaceSettlement(vertLoc)) {
            facade.initiateSettlement(userCookie.getPlayerId(), vertLoc);
        }
    }

    @Override
    public void placeCity(VertexLocation vertLoc) {
        if(canPlaceCity(vertLoc)) {
            System.out.println("You're a wizard Harry");
        }
    }

    @Override
    public void placeRobber(HexLocation hexLoc) {
        if(canPlaceRobber(hexLoc)) {
            System.out.println("You're a wizard Harry");
        }
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
