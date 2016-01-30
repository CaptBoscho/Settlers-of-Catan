package shared.model.game;

import shared.player.Player;
import shared.model.devcards.DevCardType;
import shared.definitions.PortType;
import shared.definitions.ResourceType;

public interface IGame {

    /**
     * Starts the game, returns the Id for the first player
     * @param players
     * @return
     */
    public int initializeGame(List<Player> players);

    /**
     * returns the playerID for whose turn it is
     * @return
     */
    public int getCurrentTurn();

    /**
     * returns boolean value denoting if the player can build a
     * road (just checks cards really)
     * @param playerID
     * @return
     */
    public boolean canBuildRoad(int playerID);

    /**
     * builds a road for hte player
     */
    public void buildRoad(int playerID);

    /**
     * checks if the player has the cards to build a settlement
     * @return
     */
    public boolean canBuildSettlement(int playerID);

    /**
     * builds a settlement for this player
     */
    public void buildSettlement(int playerID);

    /**
     * checks if the player has the cards to ubild a city
     * @return
     */
    public boolean canBuildCity(int playerID);

    /**
     * builds a city for this player
     * @param playerID
     */
    public void buildCity(int playerID);

    /**
     * returns the value of how many roads is the LongestRoad
     * @return
     */
    public int currentLongestRoadSize();

    /**
     * returns the playerID of who owns the current longest road
     * @return
     */
    public int currentLongestRoadPlayer();

    /**
     * deducts Victory Points from playerIDOld
     * adds Victory Points to playerIDNew
     * Updates LongestRoad for playerIDNew and roadSize
     * @param playerIDOld
     * @param playerIDNew
     * @param roadSize
     */
    public void newLongestRoad(int playerIDOld, int playerIDNew, int roadSize);

    /**
     * checks if the player has the cards to buy a DevelopmentCard
     * @param playerID
     * @return
     */
    public boolean canBuyDevelopmentCard(int playerID);

    /**
     * Buys a new developmentCard for the player
     * deducts cards
     * adds new developmentCard to his DCBank
     * @param playerID
     */
    public DevCardType buyDevelopmentCard(int playerID);

    /**
     * checks if the player is in the trade sequence of his turn
     *
     * @param playerID
     * @return
     */
    public boolean canTrade(int playerID);

    /**
     * effectuates a trade between playerOneID and playerTwoID
     * trades the cards in the two lists
     * @param playerOneID
     * @param onecards
     * @param playerTwoID
     * @param twocards
     */
    public void tradePlayer(int playerOneID, List<ResourceType> onecards, int playerTwoID, List<ResourceType> twocards);

    /**
     * checks if that player has the card needed for that port's trade
     * @param playerID
     * @param port
     * @return
     */
    public boolean canMaritimeTrade(int playerID, PortType port);

    /**
     * effectuates a trade based on the port type
     * @param playerID
     * @param port
     */
    public void maritimeTrade(int playerID, PortType port);

    /**
     * checks if player can play that dc
     * @param playerID
     * @param dc
     * @return
     */
    public boolean canPlayDevelopmentCard(int playerID, DevCardType dc);

    /**
     * plays that development card
     * @param playerID
     * @param dc
     */
    public void playDevelopmentCard(int playerID, DevCardType dc);
}