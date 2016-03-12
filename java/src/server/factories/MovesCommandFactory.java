package server.factories;

import com.google.gson.JsonObject;
import server.commands.ICommand;
import server.commands.moves.*;
import server.facade.IFacade;

/**
 * A factory class that creates Moves Commands on demand.  Use this class to get a Moves Command
 */
public class MovesCommandFactory {

    private IFacade facade;
    private static MovesCommandFactory instance = null;
    private MovesCommandFactory() {

    }

    public static MovesCommandFactory getInstance() {
        if (instance == null) {
            instance = new MovesCommandFactory();
        }

        return instance;
    }

    public void bind(IFacade new_facade){
        facade = new_facade;
    }

    /**
     * Creates a Moves command based on a given string
     * @param command The string indicating what type of command to return
     * @return an ICommand object
     */
    public ICommand createCommand(String command, JsonObject json) {
        assert command != null;

        switch(command) {
            case "sendChat":
                return new SendChatCommand();
            case "rollNumber":
                return new RollNumberCommand();
            case "robPlayer":
                return new RobPlayerCommand();
            case "finishTurn":
                return new FinishTurnCommand();
            case "buyDevCard":
                return new BuyDevCardCommand();
            case "Year_Of_Plenty":
                return new YearOfPlentyCommand();
            case "Road_Building":
                return new RoadBuildingCommand();
            case "Soldier":
                return new SoldierCommand();
            case "Monopoly":
                return new MonopolyCommand();
            case "Monument":
                return new MonumentCommand();
            case "buildRoad":
                return new BuildRoadCommand();
            case "buildSettlement":
                //return new BuildSettlementCommand();
            case "buildCity":
                return new BuildCityCommand();
            case "offerTrade":
                int sender = json.get("sender").getAsInt();
                int receiver = json.get("receiver").getAsInt();

                JsonObject jj = json.get("offer").getAsJsonObject();
                int brick = jj.get("brick").getAsInt();
                int ore = jj.get("ore").getAsInt();
                int sheep = jj.get("sheep").getAsInt();
                int wheat = jj.get("wheat").getAsInt();
                int wood = jj.get("wood").getAsInt();
                return new OfferTradeCommand(sender, receiver, brick, ore, sheep, wheat, wood, facade);
            case "acceptTrade":
                return new AcceptTradeCommand();
            case "maritimeTrade":
                return new MaritimeTradeCommand();
            case "discardCards":
                return new DiscardCardsCommand();
            default:
                return null;
        }
    }

}
