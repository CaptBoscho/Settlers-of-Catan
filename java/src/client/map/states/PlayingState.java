package client.map.states;

import client.data.RobPlayerInfo;
import client.map.MapController;
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
            return facade.canBuildRoad(userCookie.getPlayerId(), edgeLoc);
        } catch (InvalidLocationException | InvalidPlayerException | PlayerExistsException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean canPlaceSettlement(VertexLocation vertLoc) {
        vertLoc = getModelVertexLocation(vertLoc);
        try {
            return facade.canBuildSettlement(userCookie.getPlayerId(), vertLoc);
        } catch (InvalidLocationException | InvalidPlayerException | PlayerExistsException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean canPlaceCity(VertexLocation vertLoc) {
        vertLoc = getModelVertexLocation(vertLoc);
        try {
            return facade.canBuildCity(userCookie.getPlayerId(), vertLoc);
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
        try{
            if(isPlayingRoadBuildingCard && firstRoad == null) {
                firstRoad = edgeLoc;
                facade.buildFirstRoad(userCookie.getPlayerId(), edgeLoc);
            } else if(isPlayingRoadBuildingCard) {
                facade.playRoadBuildingCard(userCookie.getPlayerId(), firstRoad, edgeLoc);
            } else {
                facade.buildRoad(userCookie.getPlayerId(), edgeLoc);
            }
            mapController.getView().placeRoad(edgeLoc, facade.getPlayerColorByID(userCookie.getPlayerId()));
        } catch (MissingUserCookieException | PlayerExistsException e) {
                e.printStackTrace();
        }
    }

    @Override
    public void placeSettlement(VertexLocation vertLoc) {
        try {
            facade.buildSettlement(userCookie.getPlayerId(), vertLoc);
            mapController.getView().placeSettlement(vertLoc, facade.getPlayerColorByID(userCookie.getPlayerId()));
        } catch (MissingUserCookieException | PlayerExistsException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void placeCity(VertexLocation vertLoc) {
        try {
            facade.buildCity(userCookie.getPlayerId(), vertLoc);
            mapController.getView().placeCity(vertLoc, facade.getPlayerColorByID(userCookie.getPlayerId()));
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
            mapController.getView().startDrop(pieceType, facade.getPlayerColorByID(userCookie.getPlayerId()), true);
        } catch (PlayerExistsException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void cancelMove() {
        if(isPlayingRoadBuildingCard && firstRoad != null) {
            facade.deleteRoad(userCookie.getPlayerId(), firstRoad);
            facade.cancelRoadBuildingCard(userCookie.getPlayerId());
            initFromModel();
        }
    }

    @Override
    public void playSoldierCard(){}

    @Override
    public void playRoadBuildingCard() {
        isPlayingRoadBuildingCard = true;
        try {
            mapController.getView().startDrop(PieceType.ROAD, facade.getPlayerColorByID(userCookie.getPlayerId()), true);
            mapController.getView().startDrop(PieceType.ROAD, facade.getPlayerColorByID(userCookie.getPlayerId()), true);
        } catch (PlayerExistsException e) {
            e.printStackTrace();
        }
        isPlayingRoadBuildingCard = false;
        firstRoad = null;
    }

    @Override
    public void robPlayer(RobPlayerInfo victim){}
}
