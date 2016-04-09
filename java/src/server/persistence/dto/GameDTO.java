package dto;

import java.sql.Blob;

/**
 * Created by boscho on 4/8/16.
 */
public class GameDTO {
    private int gameID;
    private Blob state;
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

    public Blob getState() {
        return state;
    }

    public void setState(Blob state) {
        this.state = state;
    }



}
