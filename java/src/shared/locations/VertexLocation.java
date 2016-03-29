package shared.locations;

import com.google.gson.JsonObject;
import shared.model.JsonSerializable;

/**
 * Represents the location of a vertex on a hex map
 */
public final class VertexLocation implements JsonSerializable {
	
	private HexLocation hexLoc;
	private VertexDirection dir;
	
	public VertexLocation(HexLocation hexLoc, VertexDirection dir) {
        assert hexLoc != null;
        assert dir != null;

		setHexLoc(hexLoc);
		setDir(dir);
	}

	public VertexLocation(JsonObject json) {
        assert json != null;
        assert json.has("x");
        assert json.has("y");

		final int x = json.get("x").getAsInt();
		final int y = json.get("y").getAsInt();
		hexLoc = new HexLocation(x, y);
		final String direction = json.get("direction").getAsString();
		switch (direction) {
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
		assert hexLoc != null;

		this.hexLoc = hexLoc;
	}
	
	public VertexDirection getDir()
	{
		return dir;
	}
	
	private void setDir(VertexDirection direction) {
        assert direction != null;
		this.dir = direction;
	}
	
	@Override
	public String toString() {
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
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VertexLocation other = (VertexLocation)obj;
		if (dir != other.dir)
			return false;
		if (hexLoc == null) {
			if (other.hexLoc != null) {
				return false;
			}
		} else if (!hexLoc.equals(other.hexLoc)) {
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
		final JsonObject obj = new JsonObject();
        obj.addProperty("x", this.hexLoc.getX());
        obj.addProperty("y", this.hexLoc.getY());
        obj.addProperty("direction", this.dir.toString());
        return obj;
	}
}
