package shared.model.ai.aimodel;

import com.google.gson.JsonObject;
import shared.definitions.CatanColor;
import shared.exceptions.InvalidPlayerException;
import shared.model.player.Player;
import shared.model.player.PlayerType;

/**
 * Created by Kyle 'TMD' Cornelison on 3/19/2016.
 */
public class AIPlayer extends Player {
    private AIType aiType;
    
    /**
     * Construct a Player object from a JSON blob
     *
     * @param json The JSON being used to construct this object
     */
    public AIPlayer(JsonObject json) {
        super(json);
        // TODO: 3/19/2016 Figure this out...
    }

    /**
     * New Player Constructor
     *
     * @param points      Initial points
     * @param color       Player Color
     * @param id          Player ID
     * @param playerIndex The index of the player in a particular game (0-3)
     * @param name        Player Name
     * @param type
     */
    public AIPlayer(int points, CatanColor color, int id, int playerIndex, String name, AIType type) throws InvalidPlayerException {
        super(points, color, id, playerIndex, name);
        super.setPlayerType(PlayerType.AI);
        this.aiType = type;
    }

    // TODO: State pattern version of handling do methods for ai types
}
