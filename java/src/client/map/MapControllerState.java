package client.map;

import client.data.RobPlayerInfo;
import shared.definitions.CatanColor;
import shared.definitions.PieceType;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexLocation;

/**
 * @author Kyle Cornelison
 *
 * Base class for the state of the Map Controller
 */
public class MapControllerState {
    /**
     * Constructor
     */
    public MapControllerState(){
        // TODO -- implement
    }

    /**
     * Initializes the state
     */
    public void initFromModel(){
        // TODO -- implement
    }

    /**
     * Place Road - State Implementation
     * @param edgeLoc
     */
    public void placeRoad(EdgeLocation edgeLoc) {
        // TODO -- implement
    }

    /**
     * Place Settlement - State Implementation
     * @param vertLoc
     */
    public void placeSettlement(VertexLocation vertLoc) {
        // TODO -- implement
    }

    /**
     * Place City - State Implementation
     * @param vertLoc
     */
    public void placeCity(VertexLocation vertLoc) {
        // TODO -- implement
    }

    /**
     * Place Robber - State Implementation
     * @param hexLoc
     */
    public void placeRobber(HexLocation hexLoc) {
        // TODO -- implement
    }

    /**
     * Play Soldier - State Implementation
     */
    public void playSoldierCard() {
        // TODO -- implement
    }

    /**
     * Play RoadBuilding - State Implementation
     */
    public void playRoadBuildingCard() {
        // TODO -- implement
    }

    /**
     * Rob Player - State Implementation
     * @param victim
     */
    public void robPlayer(RobPlayerInfo victim) {
        // TODO -- implement
    }
}
