package server.controllers;

import server.commands.CommandExecutionResult;
import server.factories.MovesCommandFactory;

/**
 * @author Derek Argueta
 */
public class MovesController {

    public static CommandExecutionResult sendChat(final SendChatDTO dto) {
        try {
            return MovesCommandFactory.getInstance().executeCommand("sendChat", dto);
        } catch (Exception e) {
            CommandExecutionResult result = new CommandExecutionResult("Something went wrong sending a chat :(");
            result.triggerError(500);
            return result;
        }
    }

    public static CommandExecutionResult rollNumber(final RollNumberDTO dto) {
        try {
            return MovesCommandFactory.getInstance().executeCommand("rollNumber", dto);
        } catch (Exception e) {
            CommandExecutionResult result = new CommandExecutionResult("Something went wrong rolling :(");
            result.triggerError(500);
            return result;
        }
    }

    public static CommandExecutionResult robPlayer(final RobPlayerDTO dto) {
        try {
            return MovesCommandFactory.getInstance().executeCommand("robPlayer", dto);
        } catch (Exception e) {
            CommandExecutionResult result = new CommandExecutionResult("Something went wrong robbing :(");
            result.triggerError(500);
            return result;
        }
    }

    public static CommandExecutionResult finishTurn(final FinishTurnDTO dto) {
        try {
            return MovesCommandFactory.getInstance().executeCommand("finishTurn", dto);
        } catch (Exception e) {
            CommandExecutionResult result = new CommandExecutionResult("Something went wrong finishing a turn :(");
            result.triggerError(500);
            return result;
        }
    }

    public static CommandExecutionResult buyDevCard(final BuyDevCardDTO dto) {
        try {
            return MovesCommandFactory.getInstance().executeCommand("buyDevCard", dto);
        } catch (Exception e) {
            CommandExecutionResult result = new CommandExecutionResult("Something went wrong buying a dev card :(");
            result.triggerError(500);
            return result;
        }
    }

    public static CommandExecutionResult yearOfPlenty(final PlayYOPCardDTO dto) {
        try {
            return MovesCommandFactory.getInstance().executeCommand("playYOP", dto);
        } catch (Exception e) {
            CommandExecutionResult result = new CommandExecutionResult("Something went wrong with playing year of plenty :(");
            result.triggerError(500);
            return result;
        }
    }

    public static CommandExecutionResult roadBuilding(final RoadBuildingDTO dto) {
        try {
            return MovesCommandFactory.getInstance().executeCommand("playRoadBuilding", dto);
        } catch (Exception e) {
            CommandExecutionResult result = new CommandExecutionResult("Something went wrong with playing road building :(");
            result.triggerError(500);
            return result;
        }
    }

    public static CommandExecutionResult soldier(final PlaySoldierCardDTO dto) {
        try {
            return MovesCommandFactory.getInstance().executeCommand("playSoldier", dto);
        } catch (Exception e) {
            CommandExecutionResult result = new CommandExecutionResult("Something went wrong playing soldier :(");
            result.triggerError(500);
            return result;
        }
    }

    public static CommandExecutionResult monopoly(final PlayMonopolyDTO dto) {
        try {
            return MovesCommandFactory.getInstance().executeCommand("playMonopoly", dto);
        } catch (Exception e) {
            CommandExecutionResult result = new CommandExecutionResult("Something went wrong playing monopoly :(");
            result.triggerError(500);
            return result;
        }
    }

    public static CommandExecutionResult monument(final PlayMonumentDTO dto) {
        try {
            return MovesCommandFactory.getInstance().executeCommand("playMonument", dto);
        } catch (Exception e) {
            CommandExecutionResult result = new CommandExecutionResult("Something went wrong with playing monument :(");
            result.triggerError(500);
            return result;
        }
    }

    public static CommandExecutionResult buildRoad(final BuildRoadDTO dto) {
        try {
            return MovesCommandFactory.getInstance().executeCommand("buildRoad", dto);
        } catch (Exception e) {
            CommandExecutionResult result = new CommandExecutionResult("Something went wrong building a road :(");
            result.triggerError(500);
            return result;
        }
    }

    public static CommandExecutionResult buildSettlement(final BuildSettlementDTO dto) {
        try {
            return MovesCommandFactory.getInstance().executeCommand("buildSettlement", dto);
        } catch (Exception e) {
            CommandExecutionResult result = new CommandExecutionResult("Something went wrong building a settlement :(");
            result.triggerError(500);
            return result;
        }
    }

    public static CommandExecutionResult buildCity(final BuildCityDTO dto) {
        try {
            return MovesCommandFactory.getInstance().executeCommand("buildCity", dto);
        } catch (Exception e) {
            CommandExecutionResult result = new CommandExecutionResult("Something went wrong building a city :(");
            result.triggerError(500);
            return result;
        }
    }

    public static CommandExecutionResult offerTrade(final OfferTradeDTO dto) {
        try {
            return MovesCommandFactory.getInstance().executeCommand("offerTrade", dto);
        } catch (Exception e) {
            CommandExecutionResult result = new CommandExecutionResult("Something went wrong offering a trade :(");
            result.triggerError(500);
            return result;
        }
    }

    public static CommandExecutionResult acceptTrade(final TradeOfferResponseDTO dto) {
        try {
            return MovesCommandFactory.getInstance().executeCommand("respondToOffer", dto);
        } catch (Exception e) {
            CommandExecutionResult result = new CommandExecutionResult("Something went wrong responding to offer");
            result.triggerError(500);
            return result;
        }
    }

    public static CommandExecutionResult maritimeTrade(final MaritimeTradeDTO dto) {
        try {
            return MovesCommandFactory.getInstance().executeCommand("maritimeTrade", dto);
        } catch (Exception e) {
            CommandExecutionResult result = new CommandExecutionResult("Something went wrong with maritime trade :(");
            result.triggerError(500);
            return result;
        }
    }

    public static CommandExecutionResult discardCards(final DiscardCardsDTO dto) {
        try {
            return MovesCommandFactory.getInstance().executeCommand("discardCards", dto);
        } catch (Exception e) {
            CommandExecutionResult result = new CommandExecutionResult("Something went wrong discarding :(");
            result.triggerError(500);
            return result;
        }
    }
}
