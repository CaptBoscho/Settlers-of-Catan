package dto;

/**
 * Data for adding new Game object to database
 * Created by boscho on 4/6/16.
 */
public class NewGameDTO {

    private int gameID;
    private String gameState;

    public String getGameState() {
        return gameState;
    }

    public void setGameState(String gameState) {
        this.gameState = gameState;
    }

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

}
