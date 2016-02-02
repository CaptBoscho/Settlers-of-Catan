package shared.model.structures;

/**
 * A Road is built on an edge location
 *
 * @author Joel Bradley
 */
public class Road {

    private int playerID;

    public Road(int playerID) {
        this.playerID = playerID;
    }

    public int getPlayerID() {
        return playerID;
    }
}