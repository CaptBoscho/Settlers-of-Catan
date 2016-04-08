package dto;

import java.util.List;
import java.util.Objects;

/**
 * Info to return all the commands
 * Created by boscho on 4/6/16.
 */
public class GetAllCommandsDTO implements IDTO {

    public List<CommandDTO> getAllCommands() {
        return allCommands;
    }

    public void setAllCommands(List<CommandDTO> allCommands) {
        this.allCommands = allCommands;
    }

    public void addCommand(CommandDTO command){
        allCommands.add(command);
    }

    List<CommandDTO> allCommands;
}
