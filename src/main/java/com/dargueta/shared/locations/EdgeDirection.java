package shared.locations;

public enum EdgeDirection {
	
	NorthWest, North, NorthEast, SouthEast, South, SouthWest;
	
	private EdgeDirection opposite;
	
	static {
		NorthWest.opposite = SouthEast;
		North.opposite = South;
		NorthEast.opposite = SouthWest;
		SouthEast.opposite = NorthWest;
		South.opposite = North;
		SouthWest.opposite = NorthEast;
	}
	
	public EdgeDirection getOppositeDirection()
	{
		return opposite;
	}

	@Override
	public String toString() {
		switch (this) {
			case North:
				return "N";
			case NorthWest:
				return "NW";
			case NorthEast:
				return "NE";
			case South:
				return "S";
			case SouthEast:
				return "SE";
			case SouthWest:
				return "SW";
			default:
				return "";
		}
	}
}
