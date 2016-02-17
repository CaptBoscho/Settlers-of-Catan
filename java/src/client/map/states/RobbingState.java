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
        return facade.canMoveRobber(userCookie.getPlayerId(), hexLoc);
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
        facade.moveRobber(userCookie.getPlayerId(), hexLoc);
    }

    @Override
    public void startMove(PieceType pieceType, boolean isFree, boolean allowDisconnected) {
        if(pieceType != PieceType.ROBBER) {
            return;
        }
        try {
            mapController.getView().startDrop(pieceType, facade.getPlayerColorByID(userCookie.getPlayerId()), false);
        } catch (PlayerExistsException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void cancelMove() {
        //TODO: write code on what to do if player quits soldier card
    }

    @Override
    public void playSoldierCard() {
        //TODO: write code to implement the soldier card
    }

    @Override
    public void playRoadBuildingCard() {
        System.out.println("You're a wizard Harry");
    }

    @Override
    public void robPlayer(RobPlayerInfo victim) {
        //TODO: who is calling this and what do i put here?
    }
}
