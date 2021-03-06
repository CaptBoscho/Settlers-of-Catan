package server.commands.games;

import com.google.gson.JsonObject;
import server.commands.CommandExecutionResult;
import server.commands.ICommand;
import server.main.Config;
import shared.dto.IDTO;

/**
 * A command object that lists the available games.
 *
 * @author Danny Harding
 */
public final class ListCommand implements ICommand {

    /**
     * Communicates with the ServerFacade to carry out the List command
     *
     * @return CommandExecutionResult with information about the games list
     */
    @Override
    public CommandExecutionResult execute() {
        return Config.facade.list();
    }

    @Override
    public void setParams(IDTO dto) {
        // -- not needed
    }

    @Override
    public JsonObject toJson() {
        return null;
    }

    @Override
    public void getFromJson(String json) {

    }
}
