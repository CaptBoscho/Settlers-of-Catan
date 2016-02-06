package shared.locations;

import com.google.gson.JsonObject;
import shared.model.JsonSerializable;

/**
 * Represents the location of a vertex on a hex map
 */
public class VertexLocation implements JsonSerializable {
	
	private HexLocation hexLoc;
	private VertexDirection dir;
	
	public VertexLocation(HexLocation hexLoc, VertexDirection dir) {
		setHexLoc(hexLoc);
		setDir(dir);
	}

	public VertexLocation(JsonObject json) {
		int x = json.get("x").getAsInt();
		int y = 0;
		switch(x) {
			case -3:
				y = json.get("y").getAsInt() - 3;
				break;
			case -2:
				y = json.get("y").getAsInt() - 2;
				break;
			case -1:
				y = json.get("y").getAsInt() - 1;
				break;
			case 0:
				y = json.get("y").getAsInt();
				break;
			case 1:
				y = json.get("y").getAsInt() + 1;
				break;
			case 2:
				y = json.get("y").getAsInt() + 2;
				break;
			case 3:
				y = json.get("y").getAsInt() + 3;
				break;
			default:
				break;
		}
		hexLoc = new HexLocation(x, y);
		String direction = json.get("direction").getAsString();
		switch(direction) {
			case "NW":
				dir = VertexDirection.NorthWest;
				break;
			case "NE":
				dir = VertexDirection.NorthEast;
				break;
			case "E":
				dir = VertexDirection.East;
				break;
			case "SE":
				dir = VertexDirection.SouthEast;
				break;
			case "SW":
				dir = VertexDirection.SouthWest;
				break;
			case "W":
				dir = VertexDirection.West;
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
	
	public VertexDirection getDir()
	{
		return dir;
	}
	
	private void setDir(VertexDirection direction)
	{
		this.dir = direction;
	}
	
	@Override
	public String toString()
	{
		return "VertexLocation [hexLoc=" + hexLoc + ", dir=" + dir + "]";
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
		VertexLocation other = (VertexLocation)obj;
		if(dir != other.dir)
			return false;
		if(hexLoc == null) {
			if(other.hexLoc != null) {
				return false;
			}
		} else if(!hexLoc.equals(other.hexLoc)) {
            return false;
        }
		return true;
	}
	
	/**
	 * Returns a canonical (i.e., unique) value for this vertex location. Since
	 * each vertex has three different locations on a map, this method converts
	 * a vertex location to a single canonical form. This is useful for using
	 * vertex locations as map keys.
	 * 
	 * @return Normalized vertex location
	 */
	public VertexLocation getNormalizedLocation() {
		
		// Return location that has direction NW or NE
		
		switch (dir) {
			case NorthWest:
			case NorthEast:
				return this;
			case West:
				return new VertexLocation(
										  hexLoc.getNeighborLoc(EdgeDirection.SouthWest),
										  VertexDirection.NorthEast);
			case SouthWest:
				return new VertexLocation(
										  hexLoc.getNeighborLoc(EdgeDirection.South),
										  VertexDirection.NorthWest);
			case SouthEast:
				return new VertexLocation(
										  hexLoc.getNeighborLoc(EdgeDirection.South),
										  VertexDirection.NorthEast);
			case East:
				return new VertexLocation(
										  hexLoc.getNeighborLoc(EdgeDirection.SouthEast),
										  VertexDirection.NorthWest);
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
