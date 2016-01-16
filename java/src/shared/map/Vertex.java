package shared.map;

import shared.locations.VertexLocation;

/**
 * Created by Corbin on 1/16/2016.
 */
public class Vertex {

    private VertexLocation vertexLoc;
    //private Building building;
    //private Port port;

    public Vertex(VertexLocation loc){
        vertexLoc = loc;
        //building = null;
        //port = null;
    }

    /**
     *
     * @return
     */
    /*public Building existsBuilding(){

    }*/

    /**
     *
     * @return
     */
    public boolean canAddBuilding(){
        return true;
    }

    /*public void setBuilding(Building b){

    }*/
    /*
    public void setPort(Port p){

    }*/
}
