package client.map.states;

import client.data.RobPlayerInfo;
import client.map.MapController;
import shared.definitions.CatanColor;
import shared.definitions.PieceType;
import shared.exceptions.PlayerExistsException;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexLocation;

/**
 * @author Joel Bradley
 *
 * Represents Setup 1 State
 */
public class SetupOneState extends MapState {

    /**
     * Constructor
     */
    public SetupOneState(MapController mapController){
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
        facade.initiateRoad(userCookie.getPlayerId(), edgeLoc);
    }

    @Override
    public void placeSettlement(VertexLocation vertLoc) {
        facade.initiateSettlement(userCookie.getPlayerId(), vertLoc);
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
        if(!isFree || !allowDisconnected) {
            return;
        }
        if(pieceType == PieceType.CITY || pieceType == PieceType.ROBBER) {
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
        System.out.println("You're a wizard Harry");
    }
}
