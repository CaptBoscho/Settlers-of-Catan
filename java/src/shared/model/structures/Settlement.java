package shared.model.structures;

/**
 * A Settlement is a first-level structure for players
 *
 * @author Joel Bradley
 */
public final class Settlement {

    private int playerID;

    public Settlement(int playerID) {
        assert playerID >= 0;

        this.playerID = playerID;
    }

    public int getPlayerID() {
        return playerID;
    }
}