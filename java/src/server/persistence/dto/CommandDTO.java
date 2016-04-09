package server.persistence.dto;

import java.sql.Blob;

/**
 * Created by boscho on 4/8/16.
 */
public class CommandDTO {
    private int version;
    private Blob command;
    private int gameID;

    public CommandDTO(){}

    public Blob getCommand() {
        return command;
    }

    public void setCommand(Blob command) {
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
