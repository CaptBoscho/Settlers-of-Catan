package server.controllers;

import server.commands.CommandExecutionResult;
import server.factories.MovesCommandFactory;
import shared.dto.CreateGameDTO;
import shared.dto.JoinGameDTO;

/**
 * @author Derek Argueta
 */
public class GamesController {

    public static CommandExecutionResult createGame(final CreateGameDTO dto) {
        try {
            return MovesCommandFactory.getInstance().executeCommand("create", dto);
        } catch (Exception e) {
            CommandExecutionResult result = new CommandExecutionResult("Something went wrong creating a game :(");
            result.triggerError(500);
            return result;
        }
    }

    public static CommandExecutionResult joinGame(final JoinGameDTO dto) {
        try {
            return MovesCommandFactory.getInstance().executeCommand("join", dto);
        } catch (Exception e) {
            CommandExecutionResult result = new CommandExecutionResult("Something went wrong joining a game :(");
            result.triggerError(500);
            return result;
        }
    }

    public static CommandExecutionResult listGame() {
        try {
            return MovesCommandFactory.getInstance().executeCommand("list");
        } catch (Exception e) {
            CommandExecutionResult result = new CommandExecutionResult("Something went wrong sending a chat :(");
            result.triggerError(500);
            return result;
        }
    }
}
