package shared.model.map;

import shared.definitions.*;
import shared.locations.VertexLocation;
import shared.model.structures.Building;

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
    private Building building;
    private Port port;

    public Vertex(VertexLocation vertexLoc) {
        this.vertexLoc = vertexLoc;
        building = null;
        port = null;
    }

    /**
     * Informs if the vertex has a Building
     * @return boolean
     */
    public boolean hasBuilding() {
        if(building != null) {
            return true;
        }
        return false;
    }

    public Building getBuilding() {
        return building;
    }

    public void setBuilding(Building building){
        this.building = building;
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

    /**
     * Gives resources to a building
     * @param resourceType
     */
    public void giveResources(ResourceType resourceType) {
        if(hasBuilding()) {
            building.addResources(resourceType);
        }
    }
}
