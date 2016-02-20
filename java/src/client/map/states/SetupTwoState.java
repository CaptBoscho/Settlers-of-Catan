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
 * Represents Setup 2 State
 */
public class SetupTwoState extends MapState {

    private static SetupTwoState instance;

    public static SetupTwoState getInstance(){
        if(instance == null){
            instance = new SetupTwoState();
        }
        return instance;
    }

    /**
     * Constructor
     */
    public SetupTwoState(){
        super();
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
        return false;
    }

    @Override
    public boolean canPlaceRobber(HexLocation hexLoc) {
        return false;
    }

    @Override
    public void placeRoad(EdgeLocation edgeLoc) {
        facade.initiateRoad(userCookie.getPlayerId(), edgeLoc);
        try {
            mapController.getView().placeRoad(edgeLoc, facade.getPlayerColorByID(userCookie.getPlayerId()));
        } catch (PlayerExistsException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void placeSettlement(VertexLocation vertLoc) {
        facade.initiateSettlement(userCookie.getPlayerId(), vertLoc);
        try {
            mapController.getView().placeSettlement(vertLoc, facade.getPlayerColorByID(userCookie.getPlayerId()));
        } catch (PlayerExistsException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void placeCity(VertexLocation vertLoc){}

    @Override
    public void placeRobber(HexLocation hexLoc){}

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
            e.printStackTrace();
        }
    }

    @Override
    public void cancelMove(){}

    @Override
    public void playSoldierCard(){}

    @Override
    public void playRoadBuildingCard(){}

    @Override
    public void robPlayer(RobPlayerInfo victim){}
}
