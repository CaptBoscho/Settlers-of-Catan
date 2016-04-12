package server.persistence.dto;

/**
 * Created by boscho on 4/8/16.
 */
public class CommandDTO {
    private String command;
    private int gameID;

    public CommandDTO(int gameID, String command) {
        this.gameID = gameID;
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public int getGameID(){
        return gameID;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("gameID: " + gameID + "\n");
        sb.append("command: " + command + "\n");

        return sb.toString();
    }

}
