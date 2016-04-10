package shared.dto;

import client.data.GameInfo;
import client.data.PlayerInfo;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.Serializable;
import java.util.List;

/**
 * @author Derek Argueta
 */
public class ListGamesDTO implements Serializable, IDTO {

    private List<GameInfo> games;

    public ListGamesDTO(List<GameInfo> games) {
        this.games = games;
    }

    // dumb
    @Override
    public JsonObject toJSON() { return null; }


    public JsonArray toJSONArr() {

        final JsonArray allGames = new JsonArray();
        for(final GameInfo game : this.games) {
            // first serialize all players
            final JsonArray players = new JsonArray();
            for(final PlayerInfo player : game.getPlayers()) {
                final JsonObject playerObj = new JsonObject();
                playerObj.addProperty("id", player.getId());
                playerObj.addProperty("color", player.getColor().toString());
                playerObj.addProperty("name", player.getName());
                players.add(playerObj);
            }

            // now get game info
            final JsonObject gameObj = new JsonObject();
            gameObj.add("players", players);
            gameObj.addProperty("title", game.getTitle());
            gameObj.addProperty("id", game.getId());

            allGames.add(gameObj);
        }

        return allGames;
    }
}
