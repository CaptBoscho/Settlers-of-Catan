package shared.model.map;

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
 * @author Corbin Byers
 */
public class Vertex {

    private VertexLocation vertexLoc;
    private Building building;
    private Port port;

    public Vertex(VertexLocation loc){
        vertexLoc = loc;
        building = null;
        port = null;
    }

    /**
     * Tells if there is a building
     * @return
     */
    public Building existsBuilding(){
        return null;
    }

    /**
     * Checks to see if there is a building on the vertex.
     * If not, then it checks the nearest 3 vertices and checks
     * if there exists buildings on them, if they don't then
     * it can add a building.
     * @return
     */
    public boolean canAddBuilding(){
        return true;
    }

    public void setBuilding(Building b){

    }

    public void setPort(Port p){

    }
}
