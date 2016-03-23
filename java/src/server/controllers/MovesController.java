package server.controllers;

import server.commands.CommandExecutionResult;
import server.commands.CommandName;
import server.factories.MovesCommandFactory;
import shared.dto.*;

import static server.commands.CommandName.*;

/**
 * @author Derek Argueta
 */
public final class MovesController {

    public static CommandExecutionResult sendChat(final CookieWrapperDTO dto) {
        return executeCommand(MOVES_SEND_CHAT, dto);
    }

    public static CommandExecutionResult rollNumber(final CookieWrapperDTO dto) {
        return executeCommand(MOVES_ROLL_NUMBER, dto);
    }

    public static CommandExecutionResult robPlayer(final CookieWrapperDTO dto) {
        return executeCommand(MOVES_ROB_PLAYER, dto);
    }

    public static CommandExecutionResult finishTurn(final CookieWrapperDTO dto) {
        return executeCommand(MOVES_FINISH_TURN, dto);
    }

    public static CommandExecutionResult buyDevCard(final CookieWrapperDTO dto) {
        return executeCommand(MOVES_BUY_DEV_CARD, dto);
    }

    public static CommandExecutionResult yearOfPlenty(final CookieWrapperDTO dto) {
        return executeCommand(MOVES_PLAY_YOP, dto);
    }

    public static CommandExecutionResult roadBuilding(final CookieWrapperDTO dto) {
        return executeCommand(MOVES_PLAY_ROAD_BUILDING, dto);
    }

    public static CommandExecutionResult soldier(final CookieWrapperDTO dto) {
        return executeCommand(MOVES_PLAY_SOLDIER, dto);
    }

    public static CommandExecutionResult monopoly(final CookieWrapperDTO dto) {
        return executeCommand(MOVES_PLAY_MONOPOLY, dto);
    }

    public static CommandExecutionResult monument(final CookieWrapperDTO dto) {
        return executeCommand(MOVES_PLAY_MONUMENT, dto);
    }

    public static CommandExecutionResult buildRoad(final CookieWrapperDTO dto) {
        return executeCommand(MOVES_BUILD_ROAD, dto);
    }

    public static CommandExecutionResult buildSettlement(final CookieWrapperDTO dto) {
        return executeCommand(MOVES_BUILD_SETTLEMENT, dto);
    }

    public static CommandExecutionResult buildCity(final CookieWrapperDTO dto) {
        return executeCommand(MOVES_BUILD_CITY, dto);
    }

    public static CommandExecutionResult offerTrade(final CookieWrapperDTO dto) {
        return executeCommand(MOVES_OFFER_TRADE, dto);
    }

    public static CommandExecutionResult acceptTrade(final CookieWrapperDTO dto) {
        return executeCommand(MOVES_RESPOND_TO_OFFER, dto);
    }

    public static CommandExecutionResult maritimeTrade(final CookieWrapperDTO dto) {
        return executeCommand(MOVES_MARITIME_TRADE, dto);
    }

    public static CommandExecutionResult discardCards(final CookieWrapperDTO dto) {
        return executeCommand(MOVES_DISCARD_CARDS, dto);
    }

    private static CommandExecutionResult executeCommand(final CommandName commandName, final IDTO dto) {
        try {
            return MovesCommandFactory.getInstance().executeCommand(commandName, dto);
        } catch (Exception e) {
            e.printStackTrace();
            CommandExecutionResult result = new CommandExecutionResult("Something went wrong :(");
            result.triggerError(500);
            return result;
        }
    }
}
