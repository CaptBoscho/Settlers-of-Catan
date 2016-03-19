package server.controllers;

import server.exceptions.CommandExecutionFailedException;
import server.factories.GamesCommandFactory;
import shared.dto.CreateGameDTO;
import shared.dto.GameInfoListDTO;
import shared.dto.JoinGameDTO;

/**
 * @author Derek Argueta
 */
public class GamesController {

    public static String createGame(final CreateGameDTO dto) {

        try {
            return GamesCommandFactory.getInstance().createCommand(dto).execute().toString();
        } catch (CommandExecutionFailedException e) {
            e.printStackTrace();
            return "return something else here.";
        }
    }

    public static String joinGame(final JoinGameDTO dto) {
        try {
            return GamesCommandFactory.getInstance().createCommand(dto).execute().toString();
        } catch (CommandExecutionFailedException e) {
            e.printStackTrace();
            return "return something else here.";
        }
    }

    public static String listCommand(final GameInfoListDTO dto) {
        try {
            return GamesCommandFactory.getInstance().createCommand(dto).execute().toString();
        } catch (CommandExecutionFailedException e) {
            e.printStackTrace();
            return "return something else here.";
        }
    }
}
