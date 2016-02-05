package shared.model.structures;

/**
 * A Settlement is a first-level structure for players
 *
 * @author Joel Bradley
 */
public class Settlement {

    private int playerID;

    public Settlement(int playerID) {
        this.playerID = playerID;
    }

    public int getPlayerID() {
        return playerID;
    }
}