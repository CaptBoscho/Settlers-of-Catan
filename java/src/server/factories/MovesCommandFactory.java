package server.factories;

import server.commands.CommandExecutionResult;
import server.commands.CommandName;
import server.commands.ICommand;
import server.commands.moves.*;
import server.exceptions.CommandExecutionFailedException;
import server.main.Config;
import server.persistence.PersistenceCoordinator;
import server.persistence.dto.CommandDTO;
import shared.dto.CookieWrapperDTO;
import shared.dto.IDTO;

import java.util.HashMap;
import java.util.Map;

import static server.commands.CommandName.*;
import static server.utils.Strings.BAD_COMMAND_NAME_MSG;

/**
 * A factory class that creates Moves Commands on demand.  Use this class to get a Moves Command
 *
 * {@link} https://en.wikipedia.org/wiki/Command_pattern#Java_8
 */
public final class MovesCommandFactory {

    private static MovesCommandFactory instance = null;
    private Map<CommandName, ICommand> commands;

    private MovesCommandFactory() {
        this.commands = new HashMap<>();
    }

    private void addCommand(final CommandName name, final ICommand command) {
        this.commands.put(name, command);
    }

    public static MovesCommandFactory getInstance() {
        if (instance == null) {
            instance = new MovesCommandFactory();
            instance.addCommand(MOVES_FINISH_TURN, new FinishTurnCommand());
            instance.addCommand(MOVES_SEND_CHAT, new SendChatCommand());
            instance.addCommand(MOVES_ROLL_NUMBER, new RollNumberCommand());
            instance.addCommand(MOVES_ROB_PLAYER, new RobPlayerCommand());
            instance.addCommand(MOVES_BUY_DEV_CARD, new BuyDevCardCommand());
            instance.addCommand(MOVES_PLAY_YOP, new YearOfPlentyCommand());
            instance.addCommand(MOVES_PLAY_ROAD_BUILDING, new RoadBuildingCommand());
            instance.addCommand(MOVES_PLAY_MONOPOLY, new MonopolyCommand());
            instance.addCommand(MOVES_PLAY_SOLDIER, new SoldierCommand());
            instance.addCommand(MOVES_PLAY_MONUMENT, new MonumentCommand());
            instance.addCommand(MOVES_BUILD_ROAD, new BuildRoadCommand());
            instance.addCommand(MOVES_BUILD_SETTLEMENT, new BuildSettlementCommand());
            instance.addCommand(MOVES_BUILD_CITY, new BuildCityCommand());
            instance.addCommand(MOVES_OFFER_TRADE, new OfferTradeCommand());
            instance.addCommand(MOVES_RESPOND_TO_OFFER, new AcceptTradeCommand());
            instance.addCommand(MOVES_MARITIME_TRADE, new MaritimeTradeCommand());
            instance.addCommand(MOVES_DISCARD_CARDS, new DiscardCardsCommand());
        }

        return instance;
    }

    public CommandExecutionResult executeCommand(final CommandName name, final IDTO dto) throws Exception {
        if(commands.containsKey(name)) {
            try {
                ICommand command = commands.get(name);
                command.setParams(dto);
                CommandDTO commandDTO;
                if(Config.plugin.equals("postgres")) {
                    commandDTO = new CommandDTO(((CookieWrapperDTO) dto).getGameId(), command.toJson().toString());

                } else {
                    commandDTO = new CommandDTO(((CookieWrapperDTO) dto).getGameId(), PersistenceCoordinator.serializeCommand(command));
                }
                CommandExecutionResult result = command.execute();
                PersistenceCoordinator.addCommand(commandDTO);
                // TODO - break out into "execute" and "fetchResult"
                return result;
            } catch (CommandExecutionFailedException e) {
                e.printStackTrace();
            }
        }

        throw new Exception(BAD_COMMAND_NAME_MSG);
    }

}
