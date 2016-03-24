package testrunner;

/**
 * @author Derek Argueta
 */
public class TestRunner {

    public static void main(String[] args) {

        String[] testClasses = new String[] {
            "client.networking.ServerTest",
            "model.bank.IDevelopmentCardBankTest",
            "model.bank.StructureBankTests",
            "model.game.GameTest",
            "model.game.LargestArmyTests",
            "model.game.LongestRoadTests",
            "model.game.MessageLineTests",
            "model.game.MessageListTests",
            "model.game.TurnTrackerTests",
            "model.locations.HexLocationTests",
            "model.map.EdgeTests",
            "model.map.MapTest",
            "model.map.PortTests",
            "model.map.RobberTests",
            "model.map.VertexTests",
            "model.Player.PlayerManagerTest",
            "model.Player.PlayerTest",
            "server.commands.game.AddAICommandTest",
            "server.commands.game.ListAICommandTest",
            "server.commands.game.ModelCommandTest",
            "server.commands.games.CreateCommandTest",
            "server.commands.games.JoinCommandTest",
            "server.commands.games.ListCommandTest",
            "server.commands.user.LoginCommandTest",
            "server.commands.user.RegisterCommandTest",
            "server.commands.moves.AcceptTradeCommandTest",
            "server.commands.moves.BuildCityCommandTest",
            "server.commands.moves.BuildRoadCommandTest",
            "server.commands.moves.BuildSettlementCommandTest",
            "server.commands.moves.BuyDevCardCommandTest",
            "server.commands.moves.DiscardCardsCommandTest",
            "server.commands.moves.FinishTurnCommandTest",
            "server.commands.moves.MaritimeTradeCommandTest",
            "server.commands.moves.MonopolyCommandTest",
            "server.commands.moves.MonumentCommandTest",
            "server.commands.moves.OfferTradeCommandTest",
            "server.commands.moves.RoadBuildingCommandTest",
            "server.commands.moves.RobPlayerCommandTest",
            "server.commands.moves.RollNumberCommandTest",
            "server.commands.moves.SendChatCommandTest",
            "server.commands.moves.SoldierCommandTest",
            "server.commands.moves.YearOfPlentyCommandTest"
        };

        org.junit.runner.JUnitCore.main(testClasses);
    }
}
