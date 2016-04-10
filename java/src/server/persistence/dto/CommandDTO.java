package server.persistence.dto;

/**
 * Created by boscho on 4/8/16.
 */
public class CommandDTO {
    private int version;
    private String command;
    private int gameID;

    public CommandDTO(int gameID, int version, String command) {
        this.gameID = gameID;
        this.version = version;
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getGameID(){
        return gameID;
    }

}
