package dto;

/**
 * Created by boscho on 4/8/16.
 */
public class GameDTO {
    private int gameID;
    private String state;

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
