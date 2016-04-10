package shared.model.structures;

import java.io.Serializable;

/**
 * A Settlement is a first-level structure for players
 *
 * @author Joel Bradley
 */
public final class Settlement implements Serializable {

    private int playerIndex;

    public Settlement(int playerIndex) {
        assert playerIndex >= 0;

        this.playerIndex = playerIndex;
    }

    public int getPlayerIndex() {
        return playerIndex;
    }
}