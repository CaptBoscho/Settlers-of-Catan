package server.main;

import client.data.PlayerInfo;
import shared.definitions.CatanColor;
import com.google.gson.Gson;

/**
 * @author Derek Argueta
 *
 * A dummy main class to test small things out
 */
public class Playground {

    public static void main(String[] args) {
        PlayerInfo playerInfo = new PlayerInfo("Derek", 0, CatanColor.BLUE, 3, 2, false, false);
        System.out.println(new Gson().toJson(playerInfo));
    }
}
