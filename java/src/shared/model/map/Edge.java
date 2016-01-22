package shared.model.map;
import shared.locations.EdgeLocation;
import shared.model.structures.Road;

/**
 * The edge class has a normalized EdgeLocation as well
 * as a road object. If there isn't a road on the edge
 * then the road object is set to null.
 * Created by Corbin on 1/16/2016.
 */
public class Edge {

    private EdgeLocation edgeLoc;
    private Road road;

    public Edge(EdgeLocation l){
        edgeLoc = l;
        road = null;
    }

    public void setRoad(Road r){
        road = r;
    }


    /**
     * The existsRoad() function will return the road
     * associated with this edge location. If there is
     * no road, then it will return a null value.
     * @return
     */
    /*public Road existsRoad(){
        return road;
    }*/

    public EdgeLocation getEdgeLoc(){
        return edgeLoc;
    }


}
