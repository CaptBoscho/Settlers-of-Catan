package com.dargueta.shared.model.structures;

/**
 * A Road is built on an edge location
 *
 * @author Joel Bradley
 */
public final class Road {

    private int playerIndex;
    private boolean visited;

    public Road(int playerIndex) {
        assert playerIndex >= 0;

        this.playerIndex = playerIndex;
        visited = false;
    }

    public int getPlayerIndex() {
        return playerIndex;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }
}