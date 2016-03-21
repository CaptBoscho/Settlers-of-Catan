package com.dargueta.shared.definitions;

public enum PortType {
	WOOD,
	BRICK,
	SHEEP,
	WHEAT,
	ORE,
	THREE;

	@Override
	public String toString(){
		return name().toLowerCase();
	}
}
