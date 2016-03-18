package shared.model.game;

import com.google.gson.JsonObject;
import com.sun.xml.internal.bind.annotation.OverrideAnnotationOf;
import shared.exceptions.BadJsonException;

/**
 * Representation of Player Turns
 */
public final class TurnTracker {
    //region Member variables
    private int currentTurn;
    private Phase phase;
    private static final int NUM_PLAYERS = 4;
    //endregion

    //region Constructors
    /**
     * Default Constructor
     */
    public TurnTracker() {
        this.currentTurn = 0;
        this.phase = Phase.SETUPONE;
    }

    /**
     * Loading Constructor
     * @param turnIndex index of the current player
     * @param phase The phase, 0, 1, or 2, of the turn
     */
    public TurnTracker(final int turnIndex, final Phase phase) {
        assert turnIndex >= 0;
        assert phase != null;

        this.currentTurn = turnIndex;
        this.phase = phase;
    }

    /**
     * Build a TurnTracker object from JSON
     *
     * @param json The JSON being used to build the object
     */
    public TurnTracker(JsonObject json) throws BadJsonException {
        assert json.has("currentTurn");
        assert json.has("status");

        currentTurn = json.get("currentTurn").getAsInt();
        //'Rolling' or 'Robbing' or 'Playing' or 'Discarding' or 'FirstRound' or 'SecondRound'
        switch (json.get("status").getAsString()) {
            case "Rolling":
                phase = Phase.ROLLING;
                break;
            case "Robbing":
                phase = Phase.ROBBING;
                break;
            case "Playing":
                phase = Phase.PLAYING;
                break;
            case "Discarding":
                phase = Phase.DISCARDING;
                break;
            case "FirstRound":
                phase = Phase.SETUPONE;
                break;
            case "SecondRound":
                phase = Phase.SETUPTWO;
                break;
            default:
                throw new BadJsonException("Json has invalid value for 'status' in 'TurnTracker'.");
        }
    }
    //endregion

    //region TurnTracker methods
    /**
     * Increments the turn counter to the next player's turn
     * @return index of the next player
     */
    public int nextTurn() throws Exception {
        if (isSetupPhase()) {
            return nextSetupTurn();
        } else {
            currentTurn++;
            currentTurn %= NUM_PLAYERS;
            return currentTurn;
        }
    }

    /**
     * Goes to the next players turn during the setup phase.
     * @return The index of the next player to setup
     * @throws Exception if setupTurn > getNumPlayers()
     */
    private int nextSetupTurn() throws Exception {
        if (phase == Phase.SETUPONE) {
            if (currentTurn < NUM_PLAYERS - 1) {
                return currentTurn++;
            } else if (currentTurn == NUM_PLAYERS - 1) {
                nextPhase();
                return currentTurn;
            } else {
                throw new Exception("Current setup turn is invalid.  This is broken.");
            }
        } else if (phase == Phase.SETUPTWO) {
            if (currentTurn == 0) {
                nextPhase();
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
    public void nextPhase() throws Exception {
        phase = phase.next();
        if (phase == Phase.ROLLING) {
            nextTurn();
        }
    }
    //endregion

    //region Can do methods
    public boolean canRoll() {
        return phase == Phase.ROLLING;
    }

    public boolean canPlay() {
        return phase == Phase.PLAYING;
    }

    public boolean canDiscard() {
        return phase == Phase.DISCARDING;
    }

    public boolean canUseRobber() {
        return phase == Phase.ROBBING || phase == Phase.PLAYING;
    }
    //endregion

    //region Serialization
    /**
     * Converts the TurnTracker to JSON
     *
     * @return a JSON representation of the object
     */
    public JsonObject toJSON() {
        final JsonObject json = new JsonObject();
        json.addProperty("currentTurn", currentTurn);
        return json;
    }
    //endregion

    //region Getters
    /**
     * Get the current phase
     * @return
     */
    public Phase getPhase() {
        return phase;
    }

    /**
     * Get the current turn (by player index)
     * @return index of current player
     */
    public int getCurrentTurn() {
        return this.currentTurn;
    }

    /**
     * Determines if the current phase is a setup phase
     * @return
     */
    public boolean isSetupPhase() {
        return phase == Phase.SETUPONE || phase == Phase.SETUPTWO;
    }

    /**
     * Takes a playerID and responds whether it is that player's turn or not.
     * ID system is base 0 so we don't need to subtract one from the currentTurn
     * @param playerID the playerID to check
     * @return true if it is the given player's turn, else false
     */
    public boolean isPlayersTurn(final int playerID) {
        assert playerID >= 0;

        return playerID == currentTurn;
    }
    //endregion

    //region Setters
    /**
     * Set the current phase
     * @param phase
     */
    public void setPhase(Phase phase) {
        assert phase != null;

        this.phase = phase;
    }
    //endregion

    //region Phase enum
    /**
     * Enum to represent the various phases in a game
     */
    public enum Phase {
        SETUPONE,
        SETUPTWO,
        ROLLING {
            @Override
            public Phase next() {
                return PLAYING;
            }
        },
        DISCARDING,
        ROBBING {
            @Override
            public Phase next() {
                return PLAYING;
            }
        },
        PLAYING {
            @Override
            public Phase next() {
                return ROLLING;
            }
        },
        GAMEFINISHED;

        public Phase next() {
            return values()[ordinal() + 1];
        }
    }
    //endregion
}
