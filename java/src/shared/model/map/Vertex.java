package shared.model.map;

import shared.definitions.*;
import shared.locations.VertexLocation;
import shared.model.structures.*;

/**
 * Contains a normalized VertexLocation
 * also contains a building object, if there
 * is no building on the vertex, set as null.
 * Also has a port object, if there is no port
 * connected to the vertex, then it is initialized
 * as null.
 *
 * @author Joel Bradley
 */
public class Vertex {

    private VertexLocation vertexLoc;
    private Settlement settlement;
    private City city;
    private Port port;

    public Vertex(VertexLocation vertexLoc) {
        this.vertexLoc = vertexLoc;
        settlement = null;
        city = null;
        port = null;
    }

    /**
     * Informs if the vertex has a building
     * @return boolean
     */
    public boolean hasBuilding() {
        return hasSettlement() || hasCity();
    }

    /**
     * Informs if the vertex has a Settlement
     * @return boolean
     */
    public boolean hasSettlement() {
        if(settlement != null && city == null) {
            return true;
        }
        return false;
    }

    /**
     * Informs if the vertex has a City
     * @return boolean
     */
    public boolean hasCity() {
        if(city != null && settlement == null) {
            return true;
        }
        return false;
    }

    public Settlement getSettlement() {
        return settlement;
    }

    public City getCity() {
        return city;
    }

    public int getPlayerID() {
        int playerID;
        if(hasSettlement()) {
            playerID = settlement.getPlayerID();
        } else {
            playerID = city.getPlayerID();
        }
        return playerID;
    }

    /**
     * Informs if a settlement can be built
     * @return boolean
     */
    public boolean canBuildSettlement() {
        if(settlement == null && city == null) {
            return true;
        }
        return false;
    }

    /**
     * Informs if a city can be built
     * @return boolean
     */
    public boolean canBuildCity() {
        if(settlement != null && city == null) {
            return true;
        }
        return false;
    }

    /**
     * Builds a settlement
     * @param settlement Settlement
     */
    public void buildSettlement(Settlement settlement) {
        if(canBuildSettlement()) {
            this.settlement = settlement;
        }
    }

    /**
     * Builds a city and demolishes the settlement
     * @param city City
     */
    public void buildCity(City city) {
        if(canBuildCity()) {
            settlement = null;
            this.city = city;
        }
    }

    /**
     * Informs if the vertex has a port
     * @return boolean
     */
    public boolean hasPort() {
        if(port != null) {
            return true;
        }
        return false;
    }

    public Port getPort() {
        return port;
    }

    public void setPort(Port port){
        this.port = port;
    }

    public VertexLocation getVertexLocation() {
        return vertexLoc;
    }

}
