package client.map.states;

import client.data.RobPlayerInfo;
import client.services.MissingUserCookieException;
import shared.definitions.PieceType;
import shared.exceptions.InvalidLocationException;
import shared.exceptions.InvalidPlayerException;
import shared.exceptions.PlayerExistsException;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexLocation;

/**
 * @author Joel Bradley
 *
 * Represents Playing State - Default Gameplay State
 */
public class PlayingState extends MapState {

    private static PlayingState instance;

    public static PlayingState getInstance(){
        if(instance == null){
            instance = new PlayingState();
        }
        return instance;
    }

    private boolean isPlayingRoadBuildingCard;
    private EdgeLocation firstRoad;

    /**
     * Constructor
     */
    public PlayingState(){
        super();
        isPlayingRoadBuildingCard = false;
        firstRoad = null;
    }

    @Override
    public boolean canPlaceRoad(EdgeLocation edgeLoc) {
        edgeLoc = getModelEdgeLocation(edgeLoc);
        try {
            return facade.canBuildRoad(userCookie.getPlayerIndex(), edgeLoc);
        } catch (InvalidLocationException | InvalidPlayerException | PlayerExistsException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean canPlaceSettlement(VertexLocation vertLoc) {
        vertLoc = getModelVertexLocation(vertLoc);
        try {
            return facade.canBuildSettlement(userCookie.getPlayerIndex(), vertLoc);
        } catch (InvalidLocationException | InvalidPlayerException | PlayerExistsException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean canPlaceCity(VertexLocation vertLoc) {
        vertLoc = getModelVertexLocation(vertLoc);
        try {
            return facade.canBuildCity(userCookie.getPlayerIndex(), vertLoc);
        } catch (InvalidLocationException | InvalidPlayerException | PlayerExistsException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean canPlaceRobber(HexLocation hexLoc) {
        return false;
    }

    @Override
    public void placeRoad(EdgeLocation edgeLoc) {
        edgeLoc = getModelEdgeLocation(edgeLoc);
        try{
            if(isPlayingRoadBuildingCard && firstRoad == null) {
                firstRoad = edgeLoc;
                facade.buildFirstRoad(userCookie.getPlayerIndex(), edgeLoc);
            } else if(isPlayingRoadBuildingCard) {
                facade.playRoadBuildingCard(userCookie.getPlayerIndex(), firstRoad, edgeLoc);
            } else {
                facade.buildRoad(userCookie.getPlayerIndex(), edgeLoc);
            }
            mapController.getView().placeRoad(getUIEdgeLocation(edgeLoc), facade.getPlayerColorByIndex(userCookie.getPlayerIndex()));
        } catch (MissingUserCookieException | PlayerExistsException e) {
                e.printStackTrace();
        }
    }

    @Override
    public void placeSettlement(VertexLocation vertLoc) {
        vertLoc = getModelVertexLocation(vertLoc);
        try {
            facade.buildSettlement(userCookie.getPlayerId(), vertLoc);
            mapController.getView().placeSettlement(getUIVertexLocation(vertLoc), facade.getPlayerColorByIndex(userCookie.getPlayerIndex()));
        } catch (MissingUserCookieException | PlayerExistsException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void placeCity(VertexLocation vertLoc) {
        vertLoc = getModelVertexLocation(vertLoc);
        try {
            facade.buildCity(userCookie.getPlayerId(), vertLoc);
            mapController.getView().placeCity(getUIVertexLocation(vertLoc), facade.getPlayerColorByIndex(userCookie.getPlayerIndex()));
        } catch (MissingUserCookieException | PlayerExistsException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void placeRobber(HexLocation hexLoc){}

    @Override
    public void startMove(PieceType pieceType, boolean isFree, boolean allowDisconnected) {
        if(pieceType == PieceType.ROBBER) {
            return;
        }
        try {
            mapController.getView().startDrop(pieceType, facade.getPlayerColorByIndex(userCookie.getPlayerIndex()), true);
        } catch (PlayerExistsException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void cancelMove() {
        if(isPlayingRoadBuildingCard && firstRoad != null) {
            facade.deleteRoad(userCookie.getPlayerIndex(), firstRoad);
            facade.cancelRoadBuildingCard(userCookie.getPlayerIndex());
            initFromModel();
        }
    }

    @Override
    public void playSoldierCard(){}

    @Override
    public void playRoadBuildingCard() {
        isPlayingRoadBuildingCard = true;
        try {
            mapController.getView().startDrop(PieceType.ROAD, facade.getPlayerColorByIndex(userCookie.getPlayerIndex()), true);
            mapController.getView().startDrop(PieceType.ROAD, facade.getPlayerColorByIndex(userCookie.getPlayerIndex()), true);
        } catch (PlayerExistsException e) {
            e.printStackTrace();
        }
        isPlayingRoadBuildingCard = false;
        firstRoad = null;
    }

    @Override
    public void robPlayer(RobPlayerInfo victim){}
}
