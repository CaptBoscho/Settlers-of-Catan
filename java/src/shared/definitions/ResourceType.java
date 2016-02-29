package shared.definitions;

public enum ResourceType {
	WOOD,
	BRICK,
	SHEEP,
	WHEAT,
	ORE;

	@Override
	public String toString(){
		return name().toLowerCase();
	}
}
