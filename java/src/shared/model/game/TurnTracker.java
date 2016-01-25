package shared.model.game;

/**
 * Representation of Player Turns
 */
public class TurnTracker {
    private int currentTurn;
    private String status;
    private int longestRoad;
    private int largestArmy;
    private int numPlayers;

    /**
     * Default Constructor
     * @param index index of the current player
     */
    public TurnTracker(int index) {
        this.currentTurn = index;
        this.longestRoad = -1;
        this.largestArmy = -1;
    }

    /**
     * Loading Constructor
     * @param index index of the current player
     * @param lRoadIndex index of the player who owns the Longest Road
     * @param lArmyIndex index of the player who owns the Largest Army
     */
    public TurnTracker(int index, int lRoadIndex, int lArmyIndex) {
        this.currentTurn = index;
        this.longestRoad = lRoadIndex;
        this.largestArmy = lArmyIndex;
    }

    /**
     * Increments the turn counter to the next player's turn
     * @return index of the next player
     */
    public int incrementTurn() {
        this.currentTurn = (this.currentTurn++)%numPlayers;
        return this.currentTurn;
    }

    /**
     * Get the current turn (by player index)
     * @return index of current player
     */
    public int getCurrentTurn() {
        return this.currentTurn;
    }

    /**
     * Set the number of players
     * @param numPlayers number of players
     */
    public void setNumPlayers(int numPlayers) {
        this.numPlayers = numPlayers;
    }
}
