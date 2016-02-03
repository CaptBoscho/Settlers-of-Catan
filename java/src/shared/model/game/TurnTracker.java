package shared.model.game;

import com.google.gson.JsonObject;

/**
 * Representation of Player Turns
 */
public class TurnTracker {
    private static final int NUMBER_OF_PHASES = 3;

    int setupTurn;
    boolean finalSetupRound = false;
    private int currentTurn;
    private int currentPhase;
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
        this.currentPhase = 0;
        this.setupTurn = 0;
        this.longestRoad = -1;
        this.largestArmy = -1;
    }

    /**
     * Loading Constructor
     * @param turnIndex index of the current player
     * @param longestRoadIndex index of the player who owns the Longest Road
     * @param largestArmyIndex index of the player who owns the Largest Army
     * @param phase 0, 1, or 2, indicating which phase of the turn the player is in
     */
    public TurnTracker(int turnIndex, int longestRoadIndex, int largestArmyIndex, int phase) {
        this.currentTurn = turnIndex;
        this.longestRoad = longestRoadIndex;
        this.largestArmy = largestArmyIndex;
        this.currentPhase = phase;
    }

    /**
     * Build a TurnTracker object from JSON
     *
     * @param json The JSON being used to build the object
     */
    public TurnTracker(JsonObject json) {

    }

    /**
     * Increments the turn counter to the next player's turn
     * @return index of the next player
     */
    public int incrementTurn() {
        this.currentTurn = (this.currentTurn++) % numPlayers;
        return this.currentTurn;
    }

    /**
     * Moves to the next phase of a player's turn.
     * @return the new phase of the player's turn
     */
    public int nextPhase() {
        this.currentPhase = (this.currentPhase++) % NUMBER_OF_PHASES;
        return this.currentPhase;
    }

    /**
     * Get the current turn (by player index)
     * @return index of current player
     */
    public int getCurrentTurn() {
        return this.currentTurn;
    }

    public int getCurrentPhase() {
        return currentPhase;
    }

    /**
     * Goes to the next players turn during the setup phase.
     * @return The index of the next player to setup
     * @throws Exception if setupTurn > getNumPlayers()
     */
    public int nextSetupTurn() throws Exception {
        if (!finalSetupRound) {
            if (setupTurn < getNumPlayers()) {
                return setupTurn++;
            } else if (setupTurn == numPlayers) {
                finalSetupRound = true;
                return setupTurn;
            } else {
                throw new Exception("Current setup turn is invalid.  This is broken.");
            }
        } else {
            return setupTurn--;
        }
    }

    /**
     * Set the number of players
     * @param numPlayers number of players
     */
    public void setNumPlayers(int numPlayers) {
        this.numPlayers = numPlayers - 1;
    }

    /**
     * Converts the object to JSON
     *
     * @return a JSON representation of the object
     */
    public JsonObject toJSON() {
        return null;
    }

    public int getNumPlayers() {
        return numPlayers;
    }
}
