package shared.dto;

import com.google.gson.JsonObject;
import shared.model.JsonSerializable;
import shared.model.game.Game;

/**
 * @author Kyle Cornelison
 */
public class GameModelDTO implements IDTO,JsonSerializable {

    private Game game;
    private int version;

    public GameModelDTO(JsonObject gameJSON) {
        game = new Game(gameJSON);
    }

    public GameModelDTO(final int version) {
        this.version = version;
    }

    public int getVersion() {
        return this.version;
    }

    /**
     * Converts the object to JSON
     *
     * @return The JSON representation of the object
     */
    @Override
    public JsonObject toJSON() {
        return game.toJSON();
    }
}
