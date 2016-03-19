package server.main;

import server.handlers.auth.LoginHandler;
import server.handlers.auth.RegisterHandler;
import server.handlers.game.AddAIHandler;
import server.handlers.game.ListAIHandler;
import server.handlers.game.ModelHandler;
import server.handlers.games.ListGamesHandler;
import server.handlers.moves.*;
import server.handlers.games.CreateHandler;
import server.handlers.games.JoinHandler;

import static spark.Spark.*;

/**
 * @author Derek Argueta
 */
public class Main {
    private static final int HTTP_OK = 200;

    public static void main(String[] args) {
        get("/hello", (req, res) -> {
            res.status(HTTP_OK);
            res.type("application/json");
            return "{\"Hello\": \"World!\"}";
        });

        ////////// User HTTP Requests //////////
        post("/user/login", new LoginHandler());
        post("/user/register", new RegisterHandler());

        ////////// Games HTTP Requests //////////
        get("/games/list", new ListGamesHandler());
        post("/games/create", new CreateHandler());
        post("/games/join", new JoinHandler());

        ////////// Game HTTP Requests //////////
        get("/game/model", new ModelHandler());
        post("/game/addAI", new AddAIHandler());
        post("/game/listAI", new ListAIHandler());

        ////////// Moves HTTP Requests //////////
        post("/moves/sendChat", new SendChatHandler());
        post("/moves/rollNumber", new RollNumberHandler());
        post("/moves/robPlayer", new RobPlayerHandler());
        post("/moves/finishTurn", new FinishTurnHandler());
        post("/moves/buyDevCard", new BuyDevCardHandler());
        post("/moves/Year_of_Plenty", new YearOfPlentyHandler());
        post("/moves/Road_Building", new RoadBuildingHandler());
        post("/moves/Soldier", new SoldierHandler());
        post("/moves/Monopoly", new MonopolyHandler());
        post("/moves/Monument", new MonumentHandler());
        post("/moves/buildRoad", new BuildRoadHandler());
        post("/moves/buildSettlement", new BuildSettlementHandler());
        post("/moves/buildCity", new BuildCityHandler());
        post("/moves/offerTrade", new OfferTradeHandler());
        post("/moves/acceptTrade", new AcceptTradeHandler());
        post("/moves/maritimeTrade", new MaritimeTradeHandler());
        post("/moves/discardCards", new DiscardCardsHandler());
    }
}
