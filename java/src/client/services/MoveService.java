package client.services;

import shared.definitions.ResourceType;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexLocation;

import java.util.HashMap;
import java.util.List;

/**
 * Created by derek on 1/17/16.
 */
public class MoveService {

    private static final String BASE_ENDPOINT = "/moves";

    public static boolean sendChat(int playerIndex, String content) {
        final String endpoint = BASE_ENDPOINT + "/sendChat";
        return true;
    }

    public static boolean rollNumber(int playerIndex, int numberRolled) {
        final String endpoint = BASE_ENDPOINT + "/rollNumber";
        return true;
    }

    public static boolean robPlayer(int playerIndex, int victimIndex, HexLocation location) {
        final String endpoint = BASE_ENDPOINT + "/robPlayer";
        return true;
    }

    public static boolean finishTurn(int playerIndex) {
        final String endpoint = BASE_ENDPOINT + "/finishTurn";
        return true;
    }

    public static boolean buyDevCard(int playerIndex) {
        final String endpoint = BASE_ENDPOINT + "/buyDevCard";
        return true;
    }

    public static boolean playYearOfPlentyCard(int playerIndex, ResourceType resource1, ResourceType resource2) {
        final String endpoint = BASE_ENDPOINT + "/Year_of_Plenty";
        return true;
    }

    public static boolean playRoadBuildingCard(int playerIndex, EdgeLocation spot1, EdgeLocation spot2) {
        final String endpoint = BASE_ENDPOINT + "/Road_Building";
        return true;
    }

    public static boolean playSoldierCard(int playerIndex, int victimIndex, HexLocation location) {
        final String endpoint = BASE_ENDPOINT + "/Soldier";
        return true;
    }

    public static boolean playMonopolyCard(int playerIndex, String resource) {
        final String endpoint = BASE_ENDPOINT + "/Monopoly";
        return true;
    }

    public static boolean playMonumentCard(int playerIndex) {
        final String endpoint = BASE_ENDPOINT + "/Monument";
        return true;
    }

    public static boolean buildRoad(int playerIndex, EdgeLocation roadLocation, boolean free) {
        final String endpoint = BASE_ENDPOINT + "/buildRoad";
        return true;
    }

    public static boolean buildSettlement(int playerIndex, VertexLocation location, boolean free) {
        final String endpoint = BASE_ENDPOINT + "/buildSettlement";
        return true;
    }

    public static boolean buildCity(int playerIndex, VertexLocation location) {
        final String endpoint = BASE_ENDPOINT + "/buildCity";
        return true;
    }

    // TODO - ResourceList instead of HashMap
    public static boolean offerTrade(int playerIndex, HashMap<String, Integer> offer, int receiver) {
        final String endpoint = BASE_ENDPOINT + "/offerTrade";
        return true;
    }

    public static boolean respondToTradeOffer(int playerIndex, boolean willAccept) {
        final String endpoint = BASE_ENDPOINT + "/acceptTrade";
        return true;
    }

    public static boolean maritimeTrade(int playerIndex, int ratio, String inputResource, String outputResource) {
        final String endpoint = BASE_ENDPOINT + "/maritimeTrade";
        return true;
    }

    public static boolean discardCards(int playerIndex, List<ResourceType> resourceList) {
        final String endpoint = BASE_ENDPOINT + "/discardCards";
        return true;
    }
}
