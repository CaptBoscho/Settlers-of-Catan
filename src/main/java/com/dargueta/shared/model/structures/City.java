package com.dargueta.shared.model.structures;

/**
 * A City is a second-level structure for players, and can only be built from a previously existing settlement.
 *
 * @author Joel Bradley
 */
public final class City {

    private int playerIndex;

    public City(int playerIndex) {
        assert playerIndex >= 0;

        this.playerIndex = playerIndex;
    }

    public int getPlayerIndex() {
        return playerIndex;
    }
}