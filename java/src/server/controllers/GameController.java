package server.controllers;

import server.commands.CommandExecutionResult;
import server.factories.MovesCommandFactory;
import shared.dto.AddAIDTO;
import shared.dto.GameModelDTO;
import shared.dto.ListAIDTO;

/**
 * @author Derek Argueta
 */
public class GameController {

    public static CommandExecutionResult getModel(final GameModelDTO dto) {
        try {
            return MovesCommandFactory.getInstance().executeCommand("model", dto);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static CommandExecutionResult addAI(final AddAIDTO dto) {
        try {
            return MovesCommandFactory.getInstance().executeCommand("addAI", dto);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static CommandExecutionResult listAI(final ListAIDTO dto) {
        try {
            return MovesCommandFactory.getInstance().executeCommand("listAI", dto);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
