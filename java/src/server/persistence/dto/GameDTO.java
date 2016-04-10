package server.persistence.dto;

/**
 * Created by boscho on 4/8/16.
 */
public class GameDTO {
    private int gameID;
    private String state;
    private String title;

    public GameDTO(int gameID, String title, String state) {
        this.gameID = gameID;
        this.title = title;
        this.state = state;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getGameID() {
        return gameID;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
