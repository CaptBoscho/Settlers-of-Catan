package dto;

import java.sql.Blob;

/**
 * Data for adding new Game object to database
 * Created by boscho on 4/6/16.
 */
public class NewGameDTO {

    private int gameID;
    private Blob gameState;

    public Blob getGameState() {
        return gameState;
    }

    public void setGameState(Blob gameState) {
        this.gameState = gameState;
    }

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

}
