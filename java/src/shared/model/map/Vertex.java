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
public final class Vertex {

    private VertexLocation vertexLoc;
    private Settlement settlement;
    private City city;
    private Port port;

    public Vertex(final VertexLocation vertexLoc) {
        assert vertexLoc != null;
        assert vertexLoc.getDir() != null;
        assert vertexLoc.getHexLoc() != null;

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
        if(!this.hasBuilding()) return -1;

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
    public void buildSettlement(final Settlement settlement) {
        assert settlement != null;
        assert this.port == null;
        assert settlement.getPlayerID() >= 0;
        assert canBuildSettlement(); // code should only call this after verifying canBuildSettlement

        this.settlement = settlement;
    }

    /**
     * Builds a city and demolishes the settlement
     * @param city City
     */
    public void buildCity(final City city) {
        assert city != null;
        assert this.city == null;
        assert this.port == null;
        assert this.settlement != null;
        assert city.getPlayerID() >= 0;
        assert city.getPlayerID() == this.settlement.getPlayerID();
        assert canBuildCity(); // code should only call this after verifying canBuildCity

        settlement = null;
        this.city = city;
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

    public void setPort(final Port port){
        assert port != null;
        assert port.getPortType() != null;

        this.port = port;
    }

    public VertexLocation getVertexLoc() {
        return vertexLoc;
    }

    public void setVertexLoc(VertexLocation vertexLoc) {
        this.vertexLoc = vertexLoc;
    }
}
