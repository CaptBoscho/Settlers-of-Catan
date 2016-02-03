package shared.model.game;

import com.google.gson.JsonObject;

/**
 * Representation of Player Turns
 */
public class TurnTracker {
    private static final int NUMBER_OF_PHASES = 3;

    int setupTurn;
    private int currentTurn;
    private String status; //'Rolling' or 'Robbing' or 'Playing' or 'Discarding' or 'FirstRound' or 'SecondRound'
    private int longestRoad;
    private int largestArmy;
    private int numPlayers;

    /**
     * Default Constructor
     * @param index index of the current player
     */
    public TurnTracker(int index) {
        this.currentTurn = index;
        this.setupTurn = 0;
        this.longestRoad = -1;
        this.largestArmy = -1;
    }

    /**
     * Loading Constructor
     * @param turnIndex index of the current player
     * @param longestRoadIndex index of the player who owns the Longest Road
     * @param largestArmyIndex index of the player who owns the Largest Army
     * @param status 'Rolling' or 'Robbing' or 'Playing' or 'Discarding' or 'FirstRound' or 'SecondRound'
     */
    public TurnTracker(int turnIndex, int longestRoadIndex, int largestArmyIndex, String status) {
        this.currentTurn = turnIndex;
        this.longestRoad = longestRoadIndex;
        this.largestArmy = largestArmyIndex;
        this.status = status;
    }

    /**
     * Build a TurnTracker object from JSON
     *
     * @param json The JSON being used to build the object
     */
    public TurnTracker(JsonObject json) {
        currentTurn = json.get("currentTurn").getAsInt();
        status = json.get("status").getAsString();
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
     * Get the current turn (by player index)
     * @return index of current player
     */
    public int getCurrentTurn() {
        return this.currentTurn;
    }

    /**
     * Goes to the next players turn during the setup phase.
     * @return The index of the next player to setup
     * @throws Exception if setupTurn > getNumPlayers()
     */
    public int nextSetupTurn() throws Exception {
        if (status.equals("FirstRound")) {
            if (setupTurn < getNumPlayers()) {
                return setupTurn++;
            } else if (setupTurn == numPlayers) {
                status = "SecondRound";
                return setupTurn;
            } else {
                throw new Exception("Current setup turn is invalid.  This is broken.");
            }
        } else if (status.equals("SecondRound")) {
            return setupTurn--;
        } else {
            throw new Exception("Invalid status. Can only do setup when status is 'FirstRound' or 'SecondRound'");
        }
    }

    public String getStatus() {
        return status;
    }

    /**
     * Set the number of players
     * @param numPlayers number of players
     */
    public void setNumPlayers(int numPlayers) {
        this.numPlayers = numPlayers - 1;
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    /**
     * Converts the object to JSON
     *
     * @return a JSON representation of the object
     */
    public JsonObject toJSON() {
        JsonObject json = new JsonObject();
        json.addProperty("currentTurn", currentTurn);
        json.addProperty("status", status);
        return json;
    }

}
