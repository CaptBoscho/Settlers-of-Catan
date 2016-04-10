package shared.model.game;

import java.io.Serializable;

/**
 * Representation of Largest Army
 */
public final class LargestArmy implements Serializable {
    private int playerIndex;
    private static final int pointWorth = 2;
    private int mostSoldiers = 0;

    /**
     * Default Constructor
     */
    public LargestArmy() {
        this.playerIndex = -1;
    }

    /**
     * Constructor
     * @param playerIndex
     */
    public LargestArmy(int playerIndex) {
        this.playerIndex = playerIndex;
    }

    /**
     * Get the current owner of the card
     * @return owner of the card by id
     */
    public int getOwner() {
        return this.playerIndex;
    }

    /**
     * Set the owner by player index
     * @param index index of the player owning this card (-1 for no owner)
     */
    public void setNewOwner(final int index, final int soldiers) {
        this.mostSoldiers = soldiers;
        this.playerIndex = index;
    }

    /**
     * Get the points this card is worth
     * @return Point Value
     */
    public int getPointWorth(){
        return pointWorth;
    }

    /**
     * Get the number of soldiers in the largest army
     * @return
     */
    public int getMostSoldiers(){ return mostSoldiers;}
}
