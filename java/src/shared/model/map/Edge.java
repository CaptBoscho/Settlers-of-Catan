package shared.model.map;
import shared.locations.EdgeLocation;
import shared.model.structures.Road;

/**
 * The edge class has a normalized EdgeLocation as well
 * as a road object. If there isn't a road on the edge
 * then the road object is set to null.
 *
 * @author Joel Bradley
 */
public final class Edge {

    private EdgeLocation edgeLoc;
    private Road road;

    /**
     * Default constructor for an edge
     * @param edgeLoc EdgeLocation
     */
    public Edge(EdgeLocation edgeLoc) {
        assert edgeLoc != null;

        this.edgeLoc = edgeLoc;
        road = null;
    }

    /**
     * Informs if the edge has a Road
     * @return boolean
     */
    public boolean hasRoad() {
        return road != null;
    }

    public Road getRoad() {
        return road;
    }

    public void setRoad(Road road) {
        this.road = road;
    }

    public EdgeLocation getEdgeLoc(){
        return edgeLoc;
    }
}
