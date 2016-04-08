package dto;

import java.sql.Blob;

/**
 * Created by boscho on 4/8/16.
 */
public class GameDTO {
    private int gameID;
    private Blob state;

    public GameDTO() {
    }

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public Blob getState() {
        return state;
    }

    public void setState(Blob state) {
        this.state = state;
    }



}
