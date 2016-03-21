package com.dargueta.shared.definitions;

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

	public static ResourceType translateFromString(String resource) {
		switch(resource) {
			case "ore":
				return ResourceType.ORE;
			case "brick":
				return ResourceType.BRICK;
			case "sheep":
				return ResourceType.SHEEP;
			case "wheat":
				return ResourceType.WHEAT;
			case "wood":
				return ResourceType.WOOD;
		}

		return null;
	}
}
