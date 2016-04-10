package server.persistence.dto;

import java.sql.Blob;

/**
 * Created by boscho on 4/8/16.
 */
public class GameDTO {
    private int gameID;
    private String state;
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public GameDTO() {
    }

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
