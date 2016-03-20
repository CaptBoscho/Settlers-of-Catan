package shared.model.game;

/**
 * Representation of Longest Road Card
 */
public final class LongestRoad {
    private int playerID;
    private static final int pointWorth = 2;
    private int size = 0;

    /**
     * Default Constructor
     */
    public LongestRoad() {
        this.playerID = -1;
    }

    public LongestRoad(final int playerID) {
        assert playerID >= 0 && playerID <= 3;

        this.playerID = playerID;
    }

    /**
     * Get the current owner of the card
     * @return owner of the card by id
     */
    public int getOwner() {
        return this.playerID;
    }

    /**
     * Update the length of the longest road - for use when the player with the longest road makes that road longer
     *
     * @param newRoadSize
     */
    public void updateRoadSize(int newRoadSize) {
        assert newRoadSize > 0;
        assert newRoadSize > this.size;

        this.size = newRoadSize;
    }

    /**
     * Set the owner by player id
     * @param id id of the player owning this card (-1 for no owner)
     */
    public void setOwner(int id, int roadlength) {
        assert id >= 0 && id <= 3;
        assert id != this.playerID;  // assign to a *different* player
        assert roadlength > 0;
        assert roadlength > this.size;

        this.playerID = id;
        this.size = roadlength;
    }

    public int getSize(){
        return this.size;
    }

    /**
     * Get the points this card is worth
     * @return Point Value
     */
    public int getPointWorth() {
        return pointWorth;
    }

}
