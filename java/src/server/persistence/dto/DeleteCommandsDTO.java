package dto;

/**
 * Deletes all commands with the given gameID
 * Created by boscho on 4/6/16.
 */
public class DeleteCommandsDTO implements IDTO {

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    private int gameID;


}
