package server.main;

import server.exceptions.PluginNotFoundException;
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
import server.persistence.IDatabase;
import server.persistence.PersistenceCoordinator;
import server.persistence.Plugin;
import server.persistence.registry.Registry;
import server.utils.PluginLoader;

import java.lang.reflect.InvocationTargetException;

import static shared.definitions.Endpoints.*;
import static spark.Spark.*;

/**
 * @author Derek Argueta
 *
 * Start of execution for the server
 */
public class Main {

    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException {

        try {
            Plugin dbPlugin = Registry.getInstance().getPlugin("redis");
            IDatabase database = new PluginLoader().importDatabaseJar(dbPlugin);
            PersistenceCoordinator.setDatabase(database);
        } catch (PluginNotFoundException e) {
            e.printStackTrace();
        }

        // set the configuration
        Config.commandCount = Integer.parseInt(args[0]); //TODO: change this to a 1 later
        Config.facade = ServerFacade.getInstance();

        port(Config.port);

        Config.facade.importData();

        // the following endpoint patterns require authentication cookies
//        before("/games/*", new AuthenticationFilter()); TODO this is literally the worst application design that we are being forced to adhere to. And Swagger sucks kthnxbai.
        before("/game/*", new AuthenticationFilter());
        before("/moves/*", new AuthenticationFilter());

        // the following endpoint patterns require a valid game cookie
        before("/game/*", new GameFilter());
        before("/moves/*", new GameFilter());

        ////////// Swagger Requests ////////////
        final String pathName = System.getProperty("user.dir") + "/demo/";
        get("/docs/api/data", new Handlers.JSONAppender(pathName));
        get("/docs/api/data/*", new Handlers.JSONAppender(pathName));
        get("/docs/api/view/*", new Handlers.BasicFile(pathName));

        ////////// User HTTP Requests //////////
        post(LOGIN_ENDPOINT, new LoginHandler());
        post(REGISTER_ENDPOINT, new RegisterHandler());

        ////////// Games HTTP Requests //////////
        get(LIST_GAMES_ENDPOINT, new ListGamesHandler());
        post(CREATE_GAME_ENDPOINT, new CreateHandler());
        post(JOIN_GAME_ENDPOINT, new JoinHandler());

        ////////// Game HTTP Requests //////////
        get(GAME_MODEL_ENDPOINT, new ModelHandler());
        post(ADD_AI_ENDPOINT, new AddAIHandler());
        post(LIST_AI_ENDPOINT, new ListAIHandler());

        ////////// Moves HTTP Requests //////////
        post(SEND_CHAT_ENDPOINT, new SendChatHandler());
        post(ROLL_NUMBER_ENDPOINT, new RollNumberHandler());
        post(ROB_PLAYER_ENDPOINT, new RobPlayerHandler());
        post(FINISH_TURN_ENDPOINT, new FinishTurnHandler());
        post(BUY_DEV_CARD_ENDPOINT, new BuyDevCardHandler());
        post(YEAR_OF_PLENTY_ENDPOINT, new YearOfPlentyHandler());
        post(ROAD_BUILDING_ENDPOINT, new RoadBuildingHandler());
        post(SOLDIER_ENDPOINT, new SoldierHandler());
        post(MONOPOLY_ENDPOINT, new MonopolyHandler());
        post(MONUMENT_ENDPOINT, new MonumentHandler());
        post(BUILD_ROAD_ENDPOINT, new BuildRoadHandler());
        post(BUILD_SETTLEMENT_ENDPOINT, new BuildSettlementHandler());
        post(BUILD_CITY_ENDPOINT, new BuildCityHandler());
        post(OFFER_TRADE_ENDPOINT, new OfferTradeHandler());
        post(ACCEPT_TRADE_ENDPOINT, new AcceptTradeHandler());
        post(MARITIME_TRADE_ENDPOINT, new MaritimeTradeHandler());
        post(DISCARD_CARDS_ENDPOINT, new DiscardCardsHandler());

        get(RESET_ENDPOINT, (request, response) -> {
            UserManager.reset();
            GameManager.reset();
            return "";
        });
    }
}
