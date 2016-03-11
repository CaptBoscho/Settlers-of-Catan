package server.main;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import server.controllers.GamesController;
import server.controllers.MovesController;
import server.controllers.UserController;

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
        post("/user/login", (req, res) -> {
            res.status(HTTP_OK);
            res.type("application/json");
            final JsonObject body = new JsonParser().parse(req.body()).getAsJsonObject();
            return UserController.login(body);
        });
        post("/user/register", (req, res) -> {
            res.status(HTTP_OK);
            res.type("application/json");
            final JsonObject body = new JsonParser().parse(req.body()).getAsJsonObject();
            return UserController.register(body);
        });

        ////////// Games HTTP Requests //////////
        post("/games/create", (req, res) -> {
            res.status(HTTP_OK);
            res.type("application/json");
            final JsonObject body = new JsonParser().parse(req.body()).getAsJsonObject();
            return GamesController.createGame(body);
        });
        post("/games/join", (req, res) -> {
            res.status(HTTP_OK);
            res.type("application/json");
            final JsonObject body = new JsonParser().parse(req.body()).getAsJsonObject();
            return GamesController.joinGame(body);
        });

        ////////// Game HTTP Requests //////////
        get("/game/model", (req, res) -> {
            res.status(HTTP_OK);
            res.type("application/json");
            return "{\"Hello\": \"World!\"}";
        });
        post("/game/addAI", (req, res) -> {
            res.status(HTTP_OK);
            res.type("application/json");
            return "{\"Hello\": \"World!\"}";
        });
        post("/game/listAI", (req, res) -> {
            res.status(HTTP_OK);
            res.type("application/json");
            final JsonObject body = new JsonParser().parse(req.body()).getAsJsonObject();
            return GamesController.listCommand(body);
        });

        ////////// Moves HTTP Requests //////////
        post("/moves/sendChat", (req, res) -> {
            res.status(HTTP_OK);
            res.type("application/json");
            final JsonObject body = new JsonParser().parse(req.body()).getAsJsonObject();
            return MovesController.sendChat(body).toString();
        });
        post("/moves/rollNumber", (req, res) -> {
            res.status(HTTP_OK);
            res.type("application/json");
            final JsonObject body = new JsonParser().parse(req.body()).getAsJsonObject();
            return MovesController.rollNumber(body).toString();
        });
        post("/moves/robPlayer", (req, res) -> {
            res.status(HTTP_OK);
            res.type("application/json");
            final JsonObject body = new JsonParser().parse(req.body()).getAsJsonObject();
            return MovesController.robPlayer(body).toString();
        });
        post("/moves/finishTurn", (req, res) -> {
            res.status(HTTP_OK);
            res.type("application/json");
            final JsonObject body = new JsonParser().parse(req.body()).getAsJsonObject();
            return MovesController.finishTurn(body).toString();
        });
        post("/moves/buyDevCard", (req, res) -> {
            res.status(HTTP_OK);
            res.type("application/json");
            final JsonObject body = new JsonParser().parse(req.body()).getAsJsonObject();
            return MovesController.buyDevCard(body).toString();
        });
        post("/moves/Year_of_Plenty", (req, res) -> {
            res.status(HTTP_OK);
            res.type("application/json");
            final JsonObject body = new JsonParser().parse(req.body()).getAsJsonObject();
            return MovesController.yearOfPlenty(body).toString();
        });
        post("/moves/Road_Building", (req, res) -> {
            res.status(HTTP_OK);
            res.type("application/json");
            final JsonObject body = new JsonParser().parse(req.body()).getAsJsonObject();
            return MovesController.roadBuilding(body).toString();
        });
        post("/moves/Soldier", (req, res) -> {
            res.status(HTTP_OK);
            res.type("application/json");
            final JsonObject body = new JsonParser().parse(req.body()).getAsJsonObject();
            return MovesController.soldier(body).toString();
        });
        post("/moves/Monopoly", (req, res) -> {
            res.status(HTTP_OK);
            res.type("application/json");
            final JsonObject body = new JsonParser().parse(req.body()).getAsJsonObject();
            return MovesController.monopoly(body).toString();
        });
        post("/moves/Monument", (req, res) -> {
            res.status(HTTP_OK);
            res.type("application/json");
            final JsonObject body = new JsonParser().parse(req.body()).getAsJsonObject();
            return MovesController.monument(body).toString();
        });
        post("/moves/buildRoad", (req, res) -> {
            res.status(HTTP_OK);
            res.type("application/json");
            final JsonObject body = new JsonParser().parse(req.body()).getAsJsonObject();
            return MovesController.buildRoad(body).toString();
        });
        post("/moves/buildSettlement", (req, res) -> {
            res.status(HTTP_OK);
            res.type("application/json");
            final JsonObject body = new JsonParser().parse(req.body()).getAsJsonObject();
            return MovesController.buildSettlement(body).toString();
        });
        post("/moves/buildCity", (req, res) -> {
            res.status(HTTP_OK);
            res.type("application/json");
            final JsonObject body = new JsonParser().parse(req.body()).getAsJsonObject();
            return MovesController.buildCity(body).toString();
        });
        post("/moves/offerTrade", (req, res) -> {
            res.status(HTTP_OK);
            res.type("application/json");
            final JsonObject body = new JsonParser().parse(req.body()).getAsJsonObject();
            return MovesController.offerTrade(body).toString();
        });
        post("/moves/acceptTrade", (req, res) -> {
            res.status(HTTP_OK);
            res.type("application/json");
            final JsonObject body = new JsonParser().parse(req.body()).getAsJsonObject();
            return MovesController.acceptTrade(body).toString();
        });
        post("/moves/maritimeTrade", (req, res) -> {
            res.status(HTTP_OK);
            res.type("application/json");
            final JsonObject body = new JsonParser().parse(req.body()).getAsJsonObject();
            return MovesController.maritimeTrade(body).toString();
        });
        post("/moves/discardCards", (req, res) -> {
            res.status(HTTP_OK);
            res.type("application/json");
            final JsonObject body = new JsonParser().parse(req.body()).getAsJsonObject();
            return MovesController.discardCards(body).toString();
        });
    }
}
