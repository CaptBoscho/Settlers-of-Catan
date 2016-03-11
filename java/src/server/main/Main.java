package server.main;

import static spark.Spark.*;

/**
 * @author Derek Argueta
 */
public class Main {
    public static void main(String[] args) {
        get("/hello", (req, res) -> "Hello World!");

        post("/user/login", (req, res) -> "Hello World!");
        post("/user/register", (req, res) -> "Hello World!");

        post("/games/create", (req, res) -> "Hello World!");
        post("/games/join", (req, res) -> "Hello World!");

        get("/game/model", (req, res) -> "Hello World!");
        post("/game/addAI", (req, res) -> "Hello World!");
        post("/game/listAI", (req, res) -> "Hello World!");

        post("/moves/sendChat", (req, res) -> "Hello World!");
        post("/moves/rollNumber", (req, res) -> "Hello World!");
        post("/moves/robPlayer", (req, res) -> "Hello World!");
        post("/moves/finishTurn", (req, res) -> "Hello World!");
        post("/moves/buyDevCard", (req, res) -> "Hello World!");
        post("/moves/Year_of_Plenty", (req, res) -> "Hello World!");
        post("/moves/Road_Building", (req, res) -> "Hello World!");
        post("/moves/Soldier", (req, res) -> "Hello World!");
        post("/moves/Monopoly", (req, res) -> "Hello World!");
        post("/moves/Monument", (req, res) -> "Hello World!");
        post("/moves/buildRoad", (req, res) -> "Hello World!");
        post("/moves/buildSettlement", (req, res) -> "Hello World!");
        post("/moves/buildCity", (req, res) -> "Hello World!");
        post("/moves/offerTrade", (req, res) -> "Hello World!");
        post("/moves/acceptTrade", (req, res) -> "Hello World!");
        post("/moves/maritimeTrade", (req, res) -> "Hello World!");
        post("/moves/discardCards", (req, res) -> "Hello World!");
    }
}
