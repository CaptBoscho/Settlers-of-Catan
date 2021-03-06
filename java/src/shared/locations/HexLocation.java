package shared.locations;

import com.google.gson.JsonObject;
import shared.model.JsonSerializable;

import java.io.Serializable;

/**
 * Represents the location of a hex on a hex map
 */
public final class HexLocation implements Serializable, JsonSerializable {
	
	private int x;
	private int y;
	
	public HexLocation(int x, int y) {
		setX(x);
		setY(y);
	}

    /**
     * Construct a HexLocation object from a JSON blob
     *
     * @param json The JSON being used to construct this object
     */
	public HexLocation(JsonObject json) {
        assert json != null;
        assert json.has("x");
        assert json.has("y");

        x = json.get("x").getAsInt();
		y = json.get("y").getAsInt();
    }
	
	public int getX() {
		return x;
	}
	
	private void setX(int x) {
//        assert x >= 0;

		this.x = x;
	}
	
	public int getY() {
		return y;
	}
	
	private void setY(int y) {
		this.y = y;
	}
	
	@Override
	public String toString() {
		return "HexLocation [x=" + x + ", y=" + y + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
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
        HexLocation other = (HexLocation) obj;
        return x == other.x && y == other.y;
    }
	
	public HexLocation getNeighborLoc(EdgeDirection dir) {
		switch (dir) {
			case NorthWest:
				return new HexLocation(x - 1, y - 1);
			case North:
				return new HexLocation(x, y - 1);
			case NorthEast:
				return new HexLocation(x + 1, y);
			case SouthWest:
				return new HexLocation(x - 1, y);
			case South:
				return new HexLocation(x, y + 1);
			case SouthEast:
				return new HexLocation(x + 1, y + 1);
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
        obj.addProperty("x", this.x);
        obj.addProperty("y", this.y);
		return obj;
	}
}
