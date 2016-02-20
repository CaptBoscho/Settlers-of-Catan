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
    private EdgeLocation firstRoadBuilding;

    /**
     * Constructor
     */
    public PlayingState(){
        super();
        isPlayingRoadBuildingCard = false;
        firstRoadBuilding = null;
    }

    @Override
    public boolean canPlaceRoad(EdgeLocation edgeLoc) {
        edgeLoc = getModelEdgeLocation(edgeLoc);
        try {
            return facade.canBuildRoad(userCookie.getPlayerId(), edgeLoc);
        } catch (InvalidLocationException | InvalidPlayerException | PlayerExistsException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Override
    public boolean canPlaceSettlement(VertexLocation vertLoc) {
        vertLoc = getModelVertexLocation(vertLoc);
        try {
            return facade.canBuildSettlement(userCookie.getPlayerId(), vertLoc);
        } catch (InvalidLocationException | InvalidPlayerException | PlayerExistsException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Override
    public boolean canPlaceCity(VertexLocation vertLoc) {
        vertLoc = getModelVertexLocation(vertLoc);
        try {
            return facade.canBuildCity(userCookie.getPlayerId(), vertLoc);
        } catch (InvalidLocationException | InvalidPlayerException | PlayerExistsException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Override
    public boolean canPlaceRobber(HexLocation hexLoc) {
        return false;
    }

    @Override
    public void placeRoad(EdgeLocation edgeLoc) {
        if(firstRoadBuilding == null) {
            firstRoadBuilding = edgeLoc;
        }
        try {
            if(isPlayingRoadBuildingCard) {
                //TODO: facade needs this method added to guarantee that resources are not deleted
                //facade.buildRoadPlayingCardRoad(userCookie.getPlayerId(), edgeLoc);
            } else {
                facade.buildRoad(userCookie.getPlayerId(), edgeLoc);
            }
        } catch (MissingUserCookieException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void placeSettlement(VertexLocation vertLoc) {
        try {
            facade.buildSettlement(userCookie.getPlayerId(), vertLoc);
        } catch (MissingUserCookieException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void placeCity(VertexLocation vertLoc) {
        try {
            facade.buildCity(userCookie.getPlayerId(), vertLoc);
        } catch (MissingUserCookieException e) {
            System.out.println(e.getMessage());
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
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void cancelMove() {
        if(isPlayingRoadBuildingCard && firstRoadBuilding != null) {
            //TODO: write code to cancel the road building card.  This will include deleting a road from the server
            //TODO: if one was already built.  The Map class in the model has a deleteRoad method that can be used.
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
            System.out.println(e.getMessage());
        }
        isPlayingRoadBuildingCard = false;
    }

    @Override
    public void robPlayer(RobPlayerInfo victim){}
}
