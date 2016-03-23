package server.controllers;

import server.commands.CommandExecutionResult;
import server.factories.GamesCommandFactory;
import shared.dto.CookieWrapperDTO;
import shared.dto.CreateGameDTO;

/**
 * @author Derek Argueta
 */
public class GamesController {

    public static CommandExecutionResult createGame(final CreateGameDTO dto) {
        try {
            return GamesCommandFactory.getInstance().executeCommand("create", dto);
        } catch (Exception e) {
            e.printStackTrace();
            CommandExecutionResult result = new CommandExecutionResult("Something went wrong creating a game :(");
            result.triggerError(500);
            return result;
        }
    }

    public static CommandExecutionResult joinGame(final CookieWrapperDTO dto) {
        try {
            return GamesCommandFactory.getInstance().executeCommand("join", dto);
        } catch (Exception e) {
            e.printStackTrace();
            CommandExecutionResult result = new CommandExecutionResult("Something went wrong joining a game :(");
            result.triggerError(500);
            return result;
        }
    }

    public static CommandExecutionResult listGame() {
        try {
            return GamesCommandFactory.getInstance().executeCommand("list");
        } catch (Exception e) {
            e.printStackTrace();
            CommandExecutionResult result = new CommandExecutionResult("Something went wrong listing games :(");
            result.triggerError(500);
            return result;
        }
    }
}
