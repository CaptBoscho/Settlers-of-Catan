package server.controllers;

import server.commands.CommandExecutionResult;
import server.commands.CommandName;
import server.factories.GameCommandFactory;
import shared.dto.AddAIDTO;
import shared.dto.CookieWrapperDTO;
import shared.dto.IDTO;
import shared.dto.ListAIDTO;

import static server.commands.CommandName.*;

/**
 * @author Derek Argueta
 */
public class GameController {

    public static CommandExecutionResult getModel(final CookieWrapperDTO dto) {
        return executeCommand(GAME_MODEL, dto);
    }

    public static CommandExecutionResult addAI(final AddAIDTO dto) {
        return executeCommand(GAME_ADD_AI, dto);
    }

    public static CommandExecutionResult listAI(final ListAIDTO dto) {
        return executeCommand(GAME_LIST_AI, dto);
    }

    private static CommandExecutionResult executeCommand(final CommandName commandName, final IDTO dto) {
        try {
            return GameCommandFactory.getInstance().executeCommand(GAME_LIST_AI, dto);
        } catch (Exception e) {
            e.printStackTrace();
            CommandExecutionResult result = new CommandExecutionResult("Something went wrong :(");
            result.triggerError(500);
            return result;
        }
    }
}
