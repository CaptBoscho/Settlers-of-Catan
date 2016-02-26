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
 * Represents Robbing a Player State
 */
public class RobbingState extends MapState {

    private static RobbingState instance;

    public static RobbingState getInstance(){
        if(instance == null){
            instance = new RobbingState();
        }
        return instance;
    }

    private HexLocation robbingLoc;

    /**
     * Constructor
     */
    public RobbingState(){
        super();
        robbingLoc = null;
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

    @Override
    public boolean canPlaceRobber(HexLocation hexLoc) {
        hexLoc = getModelHexLocation(hexLoc);
        return facade.canMoveRobber(userCookie.getPlayerIndex(), hexLoc);
    }

    @Override
    public void placeRoad(EdgeLocation edgeLoc){}

    @Override
    public void placeSettlement(VertexLocation vertLoc){}

    @Override
    public void placeCity(VertexLocation vertLoc){}

    @Override
    public void placeRobber(HexLocation hexLoc) {
        robbingLoc = hexLoc;
        hexLoc = getModelHexLocation(hexLoc);
        mapController.getView().placeRobber(robbingLoc);
        RobPlayerInfo[] rpi = facade.moveRobber(userCookie.getPlayerIndex(), hexLoc);
        if(rpi != null && rpi.length > 0) {
            mapController.getRobView().setPlayers(rpi);
            mapController.getRobView().showModal();
        } else {
            RobPlayerInfo self = new RobPlayerInfo();
            self.setPlayerIndex(userCookie.getPlayerIndex());
            robPlayer(self);
        }
    }

    @Override
    public void startMove(PieceType pieceType, boolean isFree, boolean allowDisconnected) {
        if(pieceType != PieceType.ROBBER) {
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
    public void playSoldierCard() {
        try {
            mapController.getView().startDrop(PieceType.ROBBER, facade.getPlayerColorByIndex(userCookie.getPlayerIndex()), true);
        } catch (PlayerExistsException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void playRoadBuildingCard(){}

    @Override
    public void robPlayer(RobPlayerInfo victim) {
        facade.rob(userCookie.getPlayerIndex(), victim, robbingLoc);
        robbingLoc = null;
    }
}
