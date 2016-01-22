package shared.model.map;
import shared.locations.EdgeLocation;

/**
 * Created by Corbin on 1/16/2016.
 */
public class Edge {

    private EdgeLocation edgeLoc;
    //private Road road;

    public Edge(EdgeLocation l){
        edgeLoc = l;
        //road = null;
    }

    /*public void setRoad(Road r){
        road = r;
    }
     */

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
