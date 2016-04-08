package dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Get Commands that coincide with gameID
 * Created by boscho on 4/6/16.
 */
public class GetCommandsDTO implements IDTO {

    private int gameID;
    private List<CommandDTO> commands;

    public GetCommandsDTO(){
        commands = new ArrayList<>();
    }

    public List<CommandDTO> getCommands() {
        return commands;
    }

    public void setCommands(List<CommandDTO> commands) {
        this.commands = commands;
    }

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public void addCommand(CommandDTO command){
        this.commands.add(command);
    }



}
