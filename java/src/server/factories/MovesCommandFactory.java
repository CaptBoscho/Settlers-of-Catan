package server.factories;

import server.commands.CommandExecutionResult;
import server.commands.ICommand;
import server.commands.moves.*;
import server.exceptions.CommandExecutionFailedException;
import server.facade.IFacade;
import shared.dto.IDTO;

import java.util.HashMap;
import java.util.Map;

/**
 * A factory class that creates Moves Commands on demand.  Use this class to get a Moves Command
 */
public class MovesCommandFactory {

    private IFacade facade;
    private static MovesCommandFactory instance = null;
    private Map<String, ICommand> commands;

    private MovesCommandFactory() {
        this.commands = new HashMap<>();
    }

    private void addCommand(final String name, final ICommand command) {
        this.commands.put(name, command);
    }

    public static MovesCommandFactory getInstance() {
        if (instance == null) {
            instance = new MovesCommandFactory();
            instance.addCommand("finishTurn", new FinishTurnCommand());
            instance.addCommand("sendChat", new SendChatCommand());
            instance.addCommand("rollNumber", new RollNumberCommand());
            instance.addCommand("robPlayer", new RobPlayerCommand());
            instance.addCommand("buyDevCard", new BuyDevCardCommand());
            instance.addCommand("playYOP", new YearOfPlentyCommand());
            instance.addCommand("playRoadBuilding", new RoadBuildingCommand());
            instance.addCommand("playMonopoly", new MonopolyCommand());
            instance.addCommand("playSoldier", new SoldierCommand());
            instance.addCommand("playMonument", new MonumentCommand());
            instance.addCommand("buildRoad", new BuildRoadCommand());
            instance.addCommand("buildSettlement", new BuildSettlementCommand());
            instance.addCommand("buildCity", new BuildCityCommand());
            instance.addCommand("offerTrade", new OfferTradeCommand());
            instance.addCommand("respondToOffer", new AcceptTradeCommand());
            instance.addCommand("maritimeTrade", new MaritimeTradeCommand());
            instance.addCommand("discardCards", new DiscardCardsCommand());
        }

        return instance;
    }

    public CommandExecutionResult executeCommand(final String name, final IDTO dto) throws Exception {
        if(commands.containsKey(name)) {
            try {
                ICommand command = commands.get(name);
                command.setParams(dto);
                // TODO - break out into "execute" and "fetchResult"
                return command.execute();
            } catch (CommandExecutionFailedException e) {
                e.printStackTrace();
            }
        }

        throw new Exception("no matching command found");
    }

}
