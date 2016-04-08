package dto;

/**
 * Created by boscho on 4/8/16.
 */
public class CommandDTO {
    private int version;
    private Object command;
    private int gameID;

    public CommandDTO(){}

    public Object getCommand() {
        return command;
    }

    public void setCommand(Object command) {
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

    public void setGameID(int game){
        gameID = game;
    }

}
