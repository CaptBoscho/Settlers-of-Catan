package server.controllers;

import server.commands.CommandExecutionResult;
import server.factories.GameCommandFactory;
import shared.dto.AddAIDTO;
import shared.dto.GameModelDTO;
import shared.dto.ListAIDTO;

/**
 * @author Derek Argueta
 */
public class GameController {

    public static CommandExecutionResult getModel(final GameModelDTO dto) {
        try {
            return GameCommandFactory.getInstance().executeCommand("model", dto);
        } catch (Exception e) {
            CommandExecutionResult result = new CommandExecutionResult("Something went wrong getting the model :(");
            result.triggerError(500);
            return result;
        }
    }

    public static CommandExecutionResult addAI(final AddAIDTO dto) {
        try {
            return GameCommandFactory.getInstance().executeCommand("addAI", dto);
        } catch (Exception e) {
            CommandExecutionResult result = new CommandExecutionResult("Something went wrong adding an AI :(");
            result.triggerError(500);
            return result;
        }
    }

    public static CommandExecutionResult listAI(final ListAIDTO dto) {
        try {
            return GameCommandFactory.getInstance().executeCommand("listAI", dto);
        } catch (Exception e) {
            CommandExecutionResult result = new CommandExecutionResult("Something went wrong listing the AI :(");
            result.triggerError(500);
            return result;
        }
    }
}
