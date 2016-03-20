package server.commands.games;

import server.commands.CommandExecutionResult;
import server.commands.ICommand;
import server.managers.GameManager;
import shared.model.game.Game;

import java.util.Collection;

/**
 * A command object that lists the available games.
 *
 * @author Danny Harding
 */
public class ListCommand implements ICommand {

    /**
     * Communicates with the ServerFacade to carry out the List command
     *
     * @return CommandExecutionResult with information about the games list
     */
    @Override
    public CommandExecutionResult execute() {

        Collection<Game> games = GameManager.getInstance().getAllGames();
        CommandExecutionResult result = new CommandExecutionResult(games.toString());
        return result;
    }
}
