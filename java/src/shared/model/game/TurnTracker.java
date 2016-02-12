package shared.model.game;

import com.google.gson.JsonObject;
import shared.exceptions.BadJsonException;

/**
 * Representation of Player Turns
 */
public class TurnTracker {
    private static final int NUMBER_OF_PHASES = 3;

    private boolean setupPhase;
    private boolean setupRoundOne;
    private boolean canUseRobber = false;

    private int currentTurn;
    private Phase phase;
    private int longestRoad;
    private int largestArmy;
    private int numPlayers;

    /**
     * Default Constructor
     * @param index index of the current player
     */
    public TurnTracker(int index) {
        assert index >= 0;

        this.currentTurn = index;
        this.setupPhase = true;
        this.setupRoundOne = true;
        this.longestRoad = -1;
        this.largestArmy = -1;
        this.phase = Phase.ROLLING;
    }

    /**
     * Loading Constructor
     * @param turnIndex index of the current player
     * @param longestRoadIndex index of the player who owns the Longest Road
     * @param largestArmyIndex index of the player who owns the Largest Army
     * @param phase The phase, 0, 1, or 2, of the turn
     */
    public TurnTracker(int turnIndex, int longestRoadIndex, int largestArmyIndex, Phase phase) {
        this.currentTurn = turnIndex;
        this.longestRoad = longestRoadIndex;
        this.largestArmy = largestArmyIndex;
        this.phase = phase;
    }

    /**
     * Build a TurnTracker object from JSON
     *
     * @param json The JSON being used to build the object
     */
    public TurnTracker(JsonObject json) throws BadJsonException {
        currentTurn = json.get("currentTurn").getAsInt();
        //'Rolling' or 'Robbing' or 'Playing' or 'Discarding' or 'FirstRound' or 'SecondRound'
        switch (json.get("status").getAsString()) {
            case "Rolling":
                phase = Phase.ROLLING;
                setupPhase = false;
                break;
            case "Robbing":
                phase = Phase.ROLLING;
                setupPhase = false;
                break;
            case "Playing":
                phase = Phase.PLAYING;
                setupPhase = false;
                break;
            case "Discarding":
                phase = Phase.DISCARDING;
                setupPhase = false;
                break;
            case "FirstRound":
                phase = Phase.PLAYING;
                setupPhase = true;
                break;
            case "SecondRound":
                phase = Phase.PLAYING;
                setupPhase = true;
                break;
            default:
                throw new BadJsonException("Json has invalid value for 'status' in 'TurnTracker'.");
        }
    }

    /**
     * Increments the turn counter to the next player's turn
     * @return index of the next player
     */
    public int nextTurn() throws Exception {
        if (setupPhase) {
            nextSetupTurn();
            return this.currentTurn;
        }
        this.currentTurn++;
        this.currentTurn = (this.currentTurn) % numPlayers;
        if(this.currentTurn == 0){this.currentTurn = numPlayers;}
        return this.currentTurn;
    }

    /**
     * Get the current turn (by player index)
     * @return index of current player
     */
    public int getCurrentTurn() {
        return this.currentTurn;
    }



    public boolean isSetupPhase(){
        return this.setupPhase;
    }


    /**
     * Goes to the next players turn during the setup phase.
     * @return The index of the next player to setup
     * @throws Exception if setupTurn > getNumPlayers()
     */
    private int nextSetupTurn() throws Exception {
        if (setupPhase && setupRoundOne) {
            if (currentTurn < getNumPlayers()) {
                return currentTurn++;
            } else if (currentTurn == numPlayers) {
                setupRoundOne = false;
                return currentTurn--;
            } else {
                throw new Exception("Current setup turn is invalid.  This is broken.");
            }
        } else if (setupPhase) {
            if (currentTurn == 0) {
                setupPhase = false;
                return currentTurn;
            } else {
                return currentTurn--;
            }
        } else {
            throw new Exception("Setup phase must be true to do setup.");
        }
    }

    /**
     * Moves phase to the next phase
     */
    public void nextPhase() {
        phase = phase.next();
    }

    public Phase getPhase() {
        return phase;
    }

    public void setPhase(Phase p){this.phase = p;}
    /**
     * Set the number of players
     * @param numPlayers number of players
     */
    public void setNumPlayers(int numPlayers) {
        assert numPlayers > 0;
        this.numPlayers = numPlayers;
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    public boolean canRoll() {
        return phase == Phase.ROLLING;
    }

    public boolean canPlay() {
        return phase == Phase.PLAYING;
    }

    public boolean canDiscard() {
        return phase == Phase.DISCARDING;
    }

    public void updateRobber(boolean now){
        this.canUseRobber = now;
    }

    public boolean canUseRobber(){
        return this.canUseRobber;
    }

    public void setSetupPhase(boolean s){this.setupPhase = s;}

    /**
     * Takes a playerID and responds whether it is that player's turn or not.
     * ID system is base 0 so we don't need to subtract one fromt the currentTurn
     * @param playerID the playerID to check
     * @return true if it is the given player's turn, else false
     */
    public boolean isPlayersTurn(int playerID) {
        return playerID == currentTurn;
    }

    /**
     * Converts the TurnTracker to JSON
     *
     * @return a JSON representation of the object
     */
    public JsonObject toJSON() {
        JsonObject json = new JsonObject();
        json.addProperty("currentTurn", currentTurn);
        //'Rolling' or 'Robbing' or 'Playing' or 'Discarding' or 'FirstRound' or 'SecondRound'
//        json.addProperty("status", status);
        return json;
    }


    public enum Phase {
        ROLLING,
        PLAYING,
        DISCARDING {
            @Override
            public Phase next() {
                return values()[0];
            }
        };

        public Phase next() {
            return values()[ordinal() + 1];
        }
    }

}
