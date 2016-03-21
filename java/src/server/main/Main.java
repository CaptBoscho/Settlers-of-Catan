package server.main;

import server.facade.ServerFacade;
import server.filters.AuthenticationFilter;
import server.filters.GameFilter;
import server.handlers.Handlers;
import server.handlers.auth.LoginHandler;
import server.handlers.auth.RegisterHandler;
import server.handlers.game.AddAIHandler;
import server.handlers.game.ListAIHandler;
import server.handlers.game.ModelHandler;
import server.handlers.games.CreateHandler;
import server.handlers.games.JoinHandler;
import server.handlers.games.ListGamesHandler;
import server.handlers.moves.*;
import server.managers.GameManager;
import server.managers.UserManager;

import static spark.Spark.*;

/**
 * @author Derek Argueta
 *
 * Start of execution for the server
 */
public class Main {
    private static final int HTTP_OK = 200;

    public static void main(String[] args) {

        // set the configuration
        if(args.length == 2) {
            Config.host = args[0];
            Config.port = Integer.parseInt(args[1]);
            Config.facade = ServerFacade.getInstance();
        } else {
            Config.facade = ServerFacade.getInstance();
        }

        // for now, hardcode to port 8081
        port(8081);

        // the following endpoint patterns require authentication cookies
        before("/games/*", new AuthenticationFilter());
        before("/game/*", new AuthenticationFilter());
        before("/moves/*", new AuthenticationFilter());

        // the following endpoint patterns require a valid game cookie
        before("/game/*", new GameFilter());
        before("/moves/*", new GameFilter());

        // TODO - enable configuring the mock server

        get("/hello", (req, res) -> {
            res.status(HTTP_OK);
            res.type("application/json");
            return "{\"Hello\": \"World!\"}";
        });

        ////////// Swagger Requests ////////////
        get("/docs/api/data", new Handlers.JSONAppender(System.getProperty("user.dir") + "/demo/"));
        get("/docs/api/data/*", new Handlers.JSONAppender(System.getProperty("user.dir") + "/demo/"));
        get("/docs/api/view/*", new Handlers.BasicFile(System.getProperty("user.dir") + "/demo/"));

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

        get("/test/reset", (request, response) -> {
            UserManager.reset();
            GameManager.reset();
            return "";
        });
    }
}
