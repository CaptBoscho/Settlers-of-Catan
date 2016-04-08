package dto;

/**
 * Adds a command to the database
 * Created by boscho on 4/6/16.
 */
public class AddCommandDTO implements IDTO{

    private Object iCommand;
    private int gameID;
    private int version;

    public Object getiCommand() {
        return iCommand;
    }

    public void setiCommand(Object iCommand) {
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
