package shared.model;


import shared.locations.HexLocation;

public class Robber {
    private HexLocation location;

    public Robber(){
        this.location = new HexLocation(0,0); //// TODO: 1/19/2016 Figure out where the desert is going to be
    }

    public Robber(HexLocation location){
        this.location = location;
    }

    public HexLocation getLocation() {
        return location;
    }

    public void setLocation(HexLocation location) {
        this.location = location;
    }
}
