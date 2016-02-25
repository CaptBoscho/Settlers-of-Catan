package shared.model.structures;

/**
 * A Settlement is a first-level structure for players
 *
 * @author Joel Bradley
 */
public final class Settlement {

    private int playerIndex;

    public Settlement(int playerIndex) {
        assert playerIndex >= 0;

        this.playerIndex = playerIndex;
    }

    public int getPlayerIndex() {
        return playerIndex;
    }
}