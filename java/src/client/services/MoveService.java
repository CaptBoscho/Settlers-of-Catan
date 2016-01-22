package client.services;

import shared.definitions.ClientModel;
import shared.definitions.ResourceType;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexLocation;

import java.util.HashMap;
import java.util.List;

/**
 * Created by derek on 1/17/16.
 *
 * Provides the functionality for players to perform actions in the game. All requests made by this class use the
 * POST HTTP method.
 */
public class MoveService {

    private static final String BASE_ENDPOINT = "/moves";

    /**
     * Sends a chat message
     *
     * @param playerIndex
     * @param content
     * @return
     */
    public static ClientModel sendChat(int playerIndex, String content) {
        final String endpoint = BASE_ENDPOINT + "/sendChat";
        return new ClientModel();
    }

    /**
     * Used to roll a number at the beginning of your turn
     *
     * @param playerIndex Who's sending this command (0-3)
     * @param numberRolled what number was rolled (2-12)
     * @return
     */
    public static ClientModel rollNumber(int playerIndex, int numberRolled) {
        final String endpoint = BASE_ENDPOINT + "/rollNumber";
        return new ClientModel();
    }

    /**
     * Moves the robber, selecting the new robber position and player to rob
     *
     * @param playerIndex Who's doing the robbing
     * @param victimIndex The order index of the player to rob
     * @param location The new location of the robber
     * @return
     */
    public static ClientModel robPlayer(int playerIndex, int victimIndex, HexLocation location) {
        final String endpoint = BASE_ENDPOINT + "/robPlayer";
        return new ClientModel();
    }

    /**
     * Used to finish your turn
     *
     * @param playerIndex Who's sending this command (0-3)
     * @return
     */
    public static ClientModel finishTurn(int playerIndex) {
        final String endpoint = BASE_ENDPOINT + "/finishTurn";
        return new ClientModel();
    }

    /**
     * Used to buy a development card
     *
     * @param playerIndex Who's playing this dev card
     * @return
     */
    public static ClientModel buyDevCard(int playerIndex) {
        final String endpoint = BASE_ENDPOINT + "/buyDevCard";
        return new ClientModel();
    }

    /**
     * Plays a 'Year of Plenty' card from the player's hand to gain the two specified resources
     *
     * @param playerIndex Who's playing this dev card
     * @param resource1
     * @param resource2
     * @return
     */
    public static ClientModel playYearOfPlentyCard(int playerIndex, ResourceType resource1, ResourceType resource2) {
        final String endpoint = BASE_ENDPOINT + "/Year_of_Plenty";
        return new ClientModel();
    }

    /**
     * Plays a 'Road Building' card from your hand to build two roads at the specified locations
     *
     * @param playerIndex Who's placing the roads
     * @param spot1 The EdgeLocation of the first road
     * @param spot2 The EdgeLocation of the second road
     * @return
     */
    public static ClientModel playRoadBuildingCard(int playerIndex, EdgeLocation spot1, EdgeLocation spot2) {
        final String endpoint = BASE_ENDPOINT + "/Road_Building";
        return new ClientModel();
    }

    /**
     * Plays a 'Soldier' from your hand, selecting the new robber position and player to rob
     *
     * @param playerIndex Who's playing this dev card
     * @param victimIndex The index of the player to rob
     * @param location The new location of the robber
     * @return
     */
    public static ClientModel playSoldierCard(int playerIndex, int victimIndex, HexLocation location) {
        final String endpoint = BASE_ENDPOINT + "/Soldier";
        return new ClientModel();
    }

    /**
     * Plays a 'Monopoly' card from your hand to monopolize the specified resource
     *
     * @param playerIndex Who's playing this dev card
     * @param resource
     * @return
     */
    public static ClientModel playMonopolyCard(int playerIndex, String resource) {
        final String endpoint = BASE_ENDPOINT + "/Monopoly";
        return new ClientModel();
    }

    /**
     * Plays a 'Monument' card from your hand to give you a victory point
     *
     * @param playerIndex Who's playing this dev card
     * @return
     */
    public static ClientModel playMonumentCard(int playerIndex) {
        final String endpoint = BASE_ENDPOINT + "/Monument";
        return new ClientModel();
    }

    /**
     * Builds a road at the specified location. (Set 'free' to true during initial setup.)
     * @param playerIndex Who's placing the road
     * @param roadLocation
     * @param free Whether this is placed for free (setup)
     * @return
     */
    public static ClientModel buildRoad(int playerIndex, EdgeLocation roadLocation, boolean free) {
        final String endpoint = BASE_ENDPOINT + "/buildRoad";
        return new ClientModel();
    }

    /**
     * Builds a settlement at the specified location. (Set 'free' to true during initial setup.)
     *
     * @param playerIndex Who's placing the settlement
     * @param location
     * @param free Whether this is placed for free (setup)
     * @return
     */
    public static ClientModel buildSettlement(int playerIndex, VertexLocation location, boolean free) {
        final String endpoint = BASE_ENDPOINT + "/buildSettlement";
        return new ClientModel();
    }

    /**
     * Builds a city at the specified location
     *
     * @param playerIndex Who's placing the city
     * @param location
     * @return
     */
    public static ClientModel buildCity(int playerIndex, VertexLocation location) {
        final String endpoint = BASE_ENDPOINT + "/buildCity";
        return new ClientModel();
    }

    /**
     * Offers a domestic trade to another player
     *
     * @param playerIndex Who's sending the offer
     * @param offer What you get (+) and what you give (-)
     * @param receiver Who you're offering the trade to (0-3)
     * @return
     */
    // TODO - ResourceList instead of HashMap
    public static ClientModel offerTrade(int playerIndex, HashMap<String, Integer> offer, int receiver) {
        final String endpoint = BASE_ENDPOINT + "/offerTrade";
        return new ClientModel();
    }

    /**
     * Used to accept or reject a trade offered to the player
     *
     * @param playerIndex Who's accepting / rejecting this trade
     * @param willAccept Whether the player accepted the trade or not
     * @return
     */
    public static ClientModel respondToTradeOffer(int playerIndex, boolean willAccept) {
        final String endpoint = BASE_ENDPOINT + "/acceptTrade";
        return new ClientModel();
    }

    /**
     * Used to execute a maritime trade
     *
     * @param playerIndex Who's doing the trading
     * @param ratio (<i>optional</i>) The ratio of the trade your doing as an integer (ie. put 3 for a 3:1 trade)
     * @param inputResource (<i>optional</i>) What type of resource you're giving
     * @param outputResource (<i>optional</i>) What type of resource you're getting
     * @return
     */
    public static ClientModel maritimeTrade(int playerIndex, int ratio, String inputResource, String outputResource) {
        final String endpoint = BASE_ENDPOINT + "/maritimeTrade";
        return new ClientModel();
    }

    /**
     * Discards the specified resource cards
     *
     * @param playerIndex Who's discarding
     * @param resourceList
     * @return
     */
    public static ClientModel discardCards(int playerIndex, List<ResourceType> resourceList) {
        final String endpoint = BASE_ENDPOINT + "/discardCards";
        return new ClientModel();
    }
}
