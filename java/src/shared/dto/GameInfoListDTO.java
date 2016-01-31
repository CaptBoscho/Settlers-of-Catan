package shared.dto;

import client.data.GameInfo;
import client.data.PlayerInfo;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import shared.definitions.CatanColor;
import shared.model.JsonSerializable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Derek Argueta
 */
public class GameInfoListDTO implements JsonSerializable {

    List<GameInfo> games;

    /**
     * Deserializes a string into a list of gameinfo objects
     * @param json a JSON representation of the games
     */
    public GameInfoListDTO(String json) {
        JsonParser parser = new JsonParser();
        JsonArray obj = parser.parse(json).getAsJsonArray();
        this.games = new ArrayList<>();
        for(JsonElement elem : obj) {
            JsonObject tmp = elem.getAsJsonObject();
            GameInfo newGame = new GameInfo();
            for(JsonElement playerJson : tmp.get("players").getAsJsonArray()) {
                JsonObject playerObj = playerJson.getAsJsonObject();
                PlayerInfo tmpInfo = new PlayerInfo();
                tmpInfo.setId(playerObj.get("id").getAsInt());
                tmpInfo.setColor(CatanColor.translateFromString(playerObj.get("color").getAsString()));
                tmpInfo.setName(playerObj.get("name").getAsString());
                newGame.addPlayer(tmpInfo);
            }
            newGame.setId(tmp.get("id").getAsInt());
            newGame.setTitle(tmp.get("title").getAsString());
            this.games.add(newGame);
        }
    }

    public List<GameInfo> getList() {
        return this.games;
    }

    /**
     * Converts the object to JSON
     *
     * @return The JSON representation of the object
     */
    @Override
    public JsonObject toJSON() {
        return null;
    }
}
