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
            e.printStackTrace();
        }
        return null;
    }

    public static CommandExecutionResult joinGame(final JoinGameDTO dto) {
        try {
            return MovesCommandFactory.getInstance().executeCommand("join", dto);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

//    public static CommandExecutionResult listGame(final ListGameDTO dto) {
//        try {
//            return MovesCommandFactory.getInstance().executeCommand("list", dto);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
}
