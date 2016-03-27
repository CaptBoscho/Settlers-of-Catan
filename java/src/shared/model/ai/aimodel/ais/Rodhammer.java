package shared.model.ai.aimodel.ais;

import com.google.gson.JsonObject;
import shared.definitions.CatanColor;
import shared.exceptions.InvalidPlayerException;
import shared.model.ai.aimodel.AIPlayer;
import shared.model.ai.aimodel.AIType;
import shared.model.ai.aitrainer.ITrainable;

/**
 * Created by Kyle 'TMD' Cornelison on 3/26/2016.
 */
public class Rodhammer extends AIPlayer implements ITrainable {
    /**
     * Construct a Player object from a JSON blob
     *
     * @param json The JSON being used to construct this object
     */
    public Rodhammer(JsonObject json) {
        super(json);
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
    public Rodhammer(int points, CatanColor color, int id, int playerIndex, String name, AIType type) throws InvalidPlayerException {
        super(points, color, id, playerIndex, name, type);
    }
}
