package shared.model.map;

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
        assert vertexLoc != null;
        assert vertexLoc.getDir() != null;
        assert vertexLoc.getHexLoc() != null;
//        assert vertexLoc.getHexLoc().getX() >= 0;
//        assert vertexLoc.getHexLoc().getY() >= 0;

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
        return settlement != null && city == null;
    }

    /**
     * Informs if the vertex has a City
     * @return boolean
     */
    public boolean hasCity() {
        return city != null && settlement == null;
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
        return settlement == null && city == null;
    }

    /**
     * Informs if a city can be built
     * @return boolean
     */
    public boolean canBuildCity() {
        return settlement != null && city == null;
    }

    /**
     * Builds a settlement
     * @param settlement Settlement
     */
    public void buildSettlement(Settlement settlement) {
        assert settlement != null;
        assert settlement.getPlayerID() >= 0;

        if(canBuildSettlement()) {
            this.settlement = settlement;
        }
    }

    /**
     * Builds a city and demolishes the settlement
     * @param city City
     */
    public void buildCity(City city) {
        assert city != null;
        assert city.getPlayerID() >= 0;

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
        return port != null;
    }

    public Port getPort() {
        return port;
    }

    public void setPort(Port port){
        assert port != null;
        assert port.getPortType() != null;

        this.port = port;
    }
}
