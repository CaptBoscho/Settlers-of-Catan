package dto;

/**
 * Created by boscho on 4/6/16.
 */
public class DeleteGameDTO {

    public DeleteGameDTO(int gameID) {
        this.gameID = gameID;
    }

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    private int gameID;
}
