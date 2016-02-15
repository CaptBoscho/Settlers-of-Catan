package client.data;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Derek Argueta
 */
public class ClientInfo {

    private int playerId = -1;
    boolean isSet = false;
    private String username = "";
    private static ClientInfo instance = null;
    private Set<Integer> games;
    private int currentGame = -1;

    public static ClientInfo getInstance() {
        if(instance == null) {
            instance = new ClientInfo();
            instance.isSet = false;
            instance.games = new HashSet<>();
        }

        return instance;
    }

    public void setInfo(final int playerId, final String username) {
        instance.playerId = playerId;
        instance.username = username;
    }

    public void joinGame(final int gameId) {
        instance.games.add(gameId);
    }

    public void enterGame(final int gameId) {
        assert instance.games.contains(gameId);

        instance.currentGame = gameId;
    }

    public void leaveGame(final int gameId) {
//        instance.games.remove(gameId);
        instance.currentGame = -1;
    }
}
