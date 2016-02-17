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

    private boolean isPlayingRoadBuildingCard;

    /**
     * Constructor
     */
    public PlayingState(MapController mapController){
        super(mapController);
        isPlayingRoadBuildingCard = false;
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
        hexLoc = getModelHexLocation(hexLoc);
        return false;
    }

    @Override
    public void placeRoad(EdgeLocation edgeLoc) {
        try {
            facade.buildRoad(userCookie.getPlayerId(), edgeLoc);
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
    public void placeRobber(HexLocation hexLoc) {
        System.out.println("You're a wizard Harry");
    }

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
        if(isPlayingRoadBuildingCard) {
            //TODO: write code to cancel the road building card
        }
    }

    @Override
    public void playSoldierCard() {
        System.out.println("You're a wizard Harry");
    }

    @Override
    public void playRoadBuildingCard() {
        isPlayingRoadBuildingCard = true;
        //TODO: write code to implement road building card
    }

    @Override
    public void robPlayer(RobPlayerInfo victim) {
        System.out.println("You're a wizard Harry");
    }
}
