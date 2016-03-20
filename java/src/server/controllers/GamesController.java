package server.controllers;

import server.commands.CommandExecutionResult;
import server.factories.GamesCommandFactory;
import shared.dto.CreateGameDTO;
import shared.dto.JoinGameDTO;

/**
 * @author Derek Argueta
 */
public class GamesController {

    public static CommandExecutionResult createGame(final CreateGameDTO dto) throws Exception {
        return GamesCommandFactory.getInstance().executeCommand("create");
    }

    public static CommandExecutionResult joinGame(final JoinGameDTO dto) throws Exception {
        return GamesCommandFactory.getInstance().executeCommand("join");
    }

    public static CommandExecutionResult listCommand() throws Exception {
        return GamesCommandFactory.getInstance().executeCommand("list");
    }
}
