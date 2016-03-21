package server.main;

import client.data.PlayerInfo;
import shared.definitions.CatanColor;
import com.google.gson.Gson;
import shared.locations.HexLocation;
import shared.model.game.MessageList;
import shared.model.map.Map;

/**
 * @author Derek Argueta
 *
 * A dummy main class to test small things out
 */
public class Playground {

    public static void main(String[] args) {
        PlayerInfo playerInfo = new PlayerInfo("Derek", 0, CatanColor.BLUE, 3, 2, false, false);
        System.out.println(new Gson().toJson(playerInfo));

        HexLocation location = new HexLocation(3, 4);
        System.out.println(new Gson().toJson(location));
        System.out.print(location.toJSON());

        Map map = new Map(false, false, false);
        System.out.println(map.toJSON());
        System.out.println(new Gson().toJson(map));

        MessageList messageList = new MessageList();
        System.out.println(messageList.toJSON());
        System.out.println(new Gson().toJson(messageList));
    }
}
