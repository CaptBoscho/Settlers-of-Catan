package server.controllers;

import server.commands.CommandExecutionResult;
import server.commands.CommandName;
import server.factories.GamesCommandFactory;
import server.managers.GameManager;
import server.persistence.PersistenceCoordinator;
import server.persistence.dto.GameDTO;
import shared.dto.CookieWrapperDTO;
import shared.dto.CreateGameDTO;
import shared.dto.IDTO;
import shared.model.game.Game;

import static server.commands.CommandName.*;

/**
 * @author Derek Argueta
 */
public final class GamesController {

    public static CommandExecutionResult createGame(final CreateGameDTO dto) {
        final CommandExecutionResult result =  executeCommand(GAMES_CREATE, dto);
        final int gameId = Integer.parseInt(result.getNewCookies().get("catan.game"));
        final Game game = GameManager.getInstance().getGameByID(gameId);
        final GameDTO gameDTO = new GameDTO(gameId, game.getTitle(), "");
        PersistenceCoordinator.addGame(gameDTO);
        return result;
    }

    public static CommandExecutionResult joinGame(final CookieWrapperDTO dto) {
        return executeCommand(GAMES_JOIN, dto);
    }

    public static CommandExecutionResult listGame() {
        try {
            return GamesCommandFactory.getInstance().executeCommand(GAMES_LIST);
        } catch (Exception e) {
            e.printStackTrace();
            CommandExecutionResult result = new CommandExecutionResult("Something went wrong listing games :(");
            result.triggerError(500);
            return result;
        }
    }

    private static CommandExecutionResult executeCommand(final CommandName commandName, final IDTO dto) {
        try {
            return GamesCommandFactory.getInstance().executeCommand(commandName, dto);
        } catch (Exception e) {
            e.printStackTrace();
            CommandExecutionResult result = new CommandExecutionResult("Something went wrong :(");
            result.triggerError(500);
            return result;
        }
    }
}
