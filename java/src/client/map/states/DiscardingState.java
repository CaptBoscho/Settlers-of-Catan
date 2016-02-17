package client.map.states;

import client.data.RobPlayerInfo;
import client.map.MapController;
import shared.definitions.PieceType;
import shared.exceptions.PlayerExistsException;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexLocation;

/**
 * @author Joel Bradley
 *
 * Represents Discarding State
 */
public class DiscardingState extends MapState {

    /**
     * Constructor
     */
    public DiscardingState(MapController mapController){
        super(mapController);
    }

    @Override
    public boolean canPlaceRoad(EdgeLocation edgeLoc) {
        edgeLoc = getModelEdgeLocation(edgeLoc);
        return false;
    }

    @Override
    public boolean canPlaceSettlement(VertexLocation vertLoc) {
        vertLoc = getModelVertexLocation(vertLoc);
        return false;
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
        System.out.println("You're a wizard Harry");
    }

    @Override
    public void placeSettlement(VertexLocation vertLoc) {
        System.out.println("You're a wizard Harry");
    }

    @Override
    public void placeCity(VertexLocation vertLoc) {
        System.out.println("You're a wizard Harry");
    }

    @Override
    public void placeRobber(HexLocation hexLoc) {
        System.out.println("You're a wizard Harry");
    }

    @Override
    public void startMove(PieceType pieceType, boolean isFree, boolean allowDisconnected) {
        System.out.println("You're a wizard Harry");
    }

    @Override
    public void cancelMove() {
        System.out.println("You're a wizard Harry");
    }

    @Override
    public void playSoldierCard() {
        System.out.println("You're a wizard Harry");
    }

    @Override
    public void playRoadBuildingCard() {
        System.out.println("You're a wizard Harry");
    }

    @Override
    public void robPlayer(RobPlayerInfo victim) {
        //TODO: do i write code here?
    }
}
