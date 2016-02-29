package client.map.states;

import client.data.RobPlayerInfo;
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

    private static SetupOneState instance;

    public static SetupOneState getInstance(){
        if(instance == null){
            instance = new SetupOneState();
        }
        return instance;
    }

    /**
     * Constructor
     */
    private SetupOneState(){
        super();
    }

    @Override
    public boolean canPlaceRoad(EdgeLocation edgeLoc) {
        edgeLoc = getModelEdgeLocation(edgeLoc);
        return facade.canInitiateRoad(userCookie.getPlayerIndex(), edgeLoc);
    }

    @Override
    public boolean canPlaceSettlement(VertexLocation vertLoc) {
        vertLoc = getModelVertexLocation(vertLoc);
        return facade.canInitiateSettlement(userCookie.getPlayerIndex(), vertLoc);
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
        edgeLoc = getModelEdgeLocation(edgeLoc);
        facade.initiateRoad(userCookie.getPlayerIndex(), edgeLoc);
        try {
            mapController.getView().placeRoad(getUIEdgeLocation(edgeLoc), facade.getPlayerColorByIndex(userCookie.getPlayerIndex()));
        } catch (PlayerExistsException e) {
            e.printStackTrace();
        }
        facade.finishTurn(userCookie.getPlayerIndex());
    }

    @Override
    public void placeSettlement(VertexLocation vertLoc) {
        vertLoc = getModelVertexLocation(vertLoc);
        facade.initiateSettlement(userCookie.getPlayerIndex(), vertLoc);
        try {
            mapController.getView().placeSettlement(getUIVertexLocation(vertLoc), facade.getPlayerColorByIndex(userCookie.getPlayerIndex()));
        } catch (PlayerExistsException e) {
            e.printStackTrace();
        }
        this.startMove(PieceType.ROAD, true, true);
    }

    @Override
    public void placeCity(VertexLocation vertLoc){}

    @Override
    public void placeRobber(HexLocation hexLoc){}

    @Override
    public void startMove(PieceType pieceType, boolean isFree, boolean allowDisconnected) {
        if(pieceType == PieceType.CITY || pieceType == PieceType.ROBBER) {
            return;
        }
        try {
            mapController.getView().startDrop(pieceType, facade.getPlayerColorByIndex(userCookie.getPlayerIndex()), false);
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
