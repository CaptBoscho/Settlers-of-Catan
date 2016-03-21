package shared.locations;

import com.google.gson.JsonObject;
import shared.model.JsonSerializable;

/**
 * Represents the location of an edge on a hex map
 */
public final class EdgeLocation implements JsonSerializable {
	
	private HexLocation hexLoc;
	private EdgeDirection dir;
	
	public EdgeLocation(HexLocation hexLoc, EdgeDirection dir) {
        assert hexLoc != null;
        assert dir != null;
		setHexLoc(hexLoc);
		setDir(dir);
	}

    /**
     * Construct a EdgeLocation object from a JSON blob
     *
     * @param json The JSON being used to construct this object
     */
	public EdgeLocation(JsonObject json) {
		int x = json.get("x").getAsInt();
		int y = json.get("y").getAsInt();
		hexLoc = new HexLocation(x, y);
        String direction = json.get("direction").getAsString();
        switch(direction) {
            case "NW":
                dir = EdgeDirection.NorthWest;
                break;
            case "N":
                dir = EdgeDirection.North;
                break;
            case "NE":
                dir = EdgeDirection.NorthEast;
                break;
            case "SE":
                dir = EdgeDirection.SouthEast;
                break;
            case "S":
                dir = EdgeDirection.South;
                break;
            case "SW":
                dir = EdgeDirection.SouthWest;
                break;
            default:
                break;
        }
    }
	
	public HexLocation getHexLoc()
	{
		return hexLoc;
	}
	
	private void setHexLoc(HexLocation hexLoc) {
		if(hexLoc == null) {
			throw new IllegalArgumentException("hexLoc cannot be null");
		}
		this.hexLoc = hexLoc;
	}
	
	public EdgeDirection getDir()
	{
		return dir;
	}
	
	private void setDir(EdgeDirection dir) {
        assert dir != null;
		this.dir = dir;
	}
	
	@Override
	public String toString()
	{
		return "EdgeLocation [hexLoc=" + hexLoc + ", dir=" + dir + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dir == null) ? 0 : dir.hashCode());
		result = prime * result + ((hexLoc == null) ? 0 : hexLoc.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj)
			return true;
		if(obj == null)
			return false;
		if(getClass() != obj.getClass())
			return false;
		EdgeLocation other = (EdgeLocation)obj;
		if(dir != other.dir)
			return false;
		if(hexLoc == null) {
			if(other.hexLoc != null)
				return false;
		} else if(!hexLoc.equals(other.hexLoc))
			return false;
		return true;
	}
	
	/**
	 * Returns a canonical (i.e., unique) value for this edge location. Since
	 * each edge has two different locations on a map, this method converts a
	 * hex location to a single canonical form. This is useful for using hex
	 * locations as map keys.
	 * 
	 * @return Normalized hex location
	 */
	public EdgeLocation getNormalizedLocation() {
		
		// Return an EdgeLocation that has direction NW, N, or NE
		
		switch (dir) {
			case NorthWest:
			case North:
			case NorthEast:
				return this;
			case SouthWest:
			case South:
			case SouthEast:
				return new EdgeLocation(hexLoc.getNeighborLoc(dir),
										dir.getOppositeDirection());
			default:
				assert false;
				return null;
		}
	}

	/**
	 * Converts the object to JSON
	 *
	 * @return The JSON representation of the object
	 */
	@Override
	public JsonObject toJSON() {
		JsonObject obj = new JsonObject();
		obj.addProperty("x", this.hexLoc.getX());
        obj.addProperty("y", this.hexLoc.getY());
        obj.addProperty("direction", this.dir.toString());
        return obj;
	}
}
