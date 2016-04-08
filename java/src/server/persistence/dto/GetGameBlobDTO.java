package dto;

/**
 * Get Game State for the game ID
 * Created by boscho on 4/6/16.
 */
public class GetGameBlobDTO {

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
