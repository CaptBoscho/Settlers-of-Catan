package client.map.states;

import client.data.RobPlayerInfo;
import client.map.MapController;
import client.services.MissingUserCookieException;
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

    /**
     * Constructor
     */
    public PlayingState(MapController mapController){
        super(mapController);
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
        return facade.canMoveRobber(hexLoc);
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
        facade.moveRobber(hexLoc);
    }

    /**
     * Play Soldier - State Implementation
     */
    @Override
    public void playSoldierCard() {
        super.playSoldierCard();
    }

    /**
     * Play RoadBuilding - State Implementation
     */
    @Override
    public void playRoadBuildingCard() {
        super.playRoadBuildingCard();
    }

    /**
     * Rob Player - State Implementation
     *
     * @param victim
     */
    @Override
    public void robPlayer(RobPlayerInfo victim) {
        super.robPlayer(victim);
    }
}
