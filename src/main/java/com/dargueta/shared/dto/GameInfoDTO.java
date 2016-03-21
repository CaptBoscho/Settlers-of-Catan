package shared.dto;

import client.data.GameInfo;
import com.google.gson.JsonObject;

/**
 * @author Derek Argueta
 */
public class GameInfoDTO implements IDTO {

    private GameInfo game;

    public GameInfoDTO(final GameInfo game) {
        this.game = game;
    }

    @Override
    public JsonObject toJSON() {
        final JsonObject obj = new JsonObject();
        obj.addProperty("id", this.game.getId());
        obj.addProperty("title", this.game.getTitle());
        obj.addProperty("players", this.game.getPlayers().toString());
        return obj;
    }
}
