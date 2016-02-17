package client.map.states;

import client.data.RobPlayerInfo;
import client.map.MapController;
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

    /**
     * Place Road - State Implementation
     *
     * @param edgeLoc
     */
    @Override
    public void placeRoad(EdgeLocation edgeLoc) {
        super.placeRoad(edgeLoc);
    }

    /**
     * Place Settlement - State Implementation
     *
     * @param vertLoc
     */
    @Override
    public void placeSettlement(VertexLocation vertLoc) {
        super.placeSettlement(vertLoc);
    }

    /**
     * Place City - State Implementation
     *
     * @param vertLoc
     */
    @Override
    public void placeCity(VertexLocation vertLoc) {
        super.placeCity(vertLoc);
    }

    /**
     * Place Robber - State Implementation
     *
     * @param hexLoc
     */
    @Override
    public void placeRobber(HexLocation hexLoc) {
        super.placeRobber(hexLoc);
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
