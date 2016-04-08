package dto;

import java.sql.Blob;

/**
 * Adds a command to the database
 * Created by boscho on 4/6/16.
 */
public class AddCommandDTO implements IDTO{

    private Blob iCommand;
    private int gameID;
    private int version;

    public Blob getiCommand() {
        return iCommand;
    }

    public void setiCommand(Blob iCommand) {
        this.iCommand = iCommand;
    }

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }


}
