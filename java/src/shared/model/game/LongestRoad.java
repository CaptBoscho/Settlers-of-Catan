package shared.model.game;

/**
 * Representation of Longest Road Card
 */
public class LongestRoad {
    private int playerID;
    private static final int pointWorth = 2;
    private int size = 0;

    /**
     * Default Constructor
     */
    public LongestRoad() {
        this.playerID = -1;
    }

    public LongestRoad(int playerID) {
        assert playerID >= -1;

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
     * Set the owner by player id
     * @param id id of the player owning this card (-1 for no owner)
     */
    public void setOwner(int id, int roadlength) {
        assert id >= 0;
        assert id != this.playerID;
        assert roadlength > 0;

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
