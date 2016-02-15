package shared.model.game;

import shared.exceptions.PlayerExistsException;
import shared.exceptions.PlayerExistsException;
import shared.model.cards.Card;
import shared.model.cards.resources.ResourceCard;
import shared.exceptions.*;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexLocation;

import shared.model.bank.InvalidTypeException;
import shared.model.cards.resources.ResourceCard;

import shared.model.map.Map;
import shared.model.player.Player;
import shared.definitions.DevCardType;
import shared.definitions.PortType;
import shared.definitions.ResourceType;

import javax.annotation.Resource;
import javax.naming.InsufficientResourcesException;
import java.util.List;
import java.util.Observer;
import java.util.Set;

public interface IGame {

    /**
     * Starts the game, returns the Id for the first player
     * @param players
     * @return
     */
    public int initializeGame(List<Player> players, boolean randomhex, boolean randomchits, boolean randomports) throws FailedToRandomizeException;

    /**
     * returns the playerID for whose turn it is
     * @return
     */
    public int getCurrentTurn();


    public boolean canInitiateSettlement(int playerID, VertexLocation vertex) throws InvalidLocationException, InvalidPlayerException;


    public boolean canInitiateRoad(int playerID, VertexLocation vertex, EdgeLocation edge) throws InvalidLocationException, InvalidPlayerException;

    public void initiateSettlement(int playerID, VertexLocation vertex) throws InvalidLocationException, InvalidPlayerException, StructureException;

    public void initiateRoad(int playerID, VertexLocation vertex, EdgeLocation edge) throws InvalidLocationException, InvalidPlayerException, StructureException;

    /**
     * Determine if Player can discard cards
     * Checks resource cards, robber position,
     *        and hexes from dice roll
     * @param playerID ID of Player performing action
     * @return True if Player can discard cards
     */
    boolean canDiscardCards(int playerID) throws PlayerExistsException;

    /**
     * Action - Player discards cards
     * @param playerID ID of Player performing action
     * @param cards Cards to be discarded
     */
    void discardCards(int playerID, List<ResourceType> cards) throws PlayerExistsException, InsufficientResourcesException, InvalidTypeException; // TODO: 1/30/2016 Would be better with Card generic class


    /**
     * Determine if Player can roll the dice
     * Checks Player turn and phase of turn
     * @param playerID ID of Player performing action
     * @return True if Player can roll the die
     */
    boolean canRollNumber(int playerID);

    /**
     * Action - Player rolls the dice
     * @param playerID ID of Player performing action
     */
    int rollNumber(int playerID) throws InvalidDiceRollException;

    /**
     * Determine if Player can offer a trade
     * Checks Player turn, phase, and resources
     * @param playerID ID of Player performing action
     * @return True if Player can offer a trade
     */
    boolean canOfferTrade(int playerID);

    /**
     * Action - Player offers trade
     * @param playerIDOne ID of Player offering the trade
     * @param playerIDTwo ID of Player being offered the trade
     */
    void offerTrade(int playerIDOne, int playerIDTwo, List<ResourceType> onecards, List<ResourceType> twocards) throws PlayerExistsException, InsufficientResourcesException, InvalidTypeException;


    /**
     * Determine if Player can finish their turn
     * Checks Player turn and phase
     * @param playerID ID of Player performing action
     * @return True if Player can finish their turn
     */
    boolean canFinishTurn(int playerID);

    /**
     * Action - Player finishes their turn
     * @param playerID ID of Player performing action
     */
    Integer finishTurn(int playerID) throws Exception;

    public TurnTracker.Phase getCurrentPhase();

    void nextPhase();


    void addObserver(Observer o);

    /**
     * Determine if Player can play Year of Plenty
     * Checks Player turn, and dev cards
     * @param playerID ID of Player performing action
     * @return True if Player can play Year of Plenty
     */
    boolean canUseYearOfPlenty(int playerID) throws PlayerExistsException;

    /**
     * Action - Player plays Year of Plenty
     * @param playerID ID of Player performing action
     */
    void useYearOfPlenty(int playerID, ResourceType want1, ResourceType want2) throws PlayerExistsException, DevCardException, InsufficientResourcesException, InvalidTypeException;

    /**
     * Determine if Player can play Road Builder
     * Checks Player turn, and dev cards
     * @param playerID ID of Player performing action
     * @return True if Player can play Road Builder
     */
    boolean canUseRoadBuilder(int playerID) throws PlayerExistsException;

    /**
     * Action - Player plays Road Builder
     * @param playerID ID of Player performing action
     */
    void useRoadBuilder(int playerID, EdgeLocation edge1, EdgeLocation edge2) throws PlayerExistsException, DevCardException, InvalidPlayerException, InvalidLocationException, StructureException;

    /**
     * Determine if Player can play Soldier
     * Checks Player turn, and dev cards
     * @param playerID ID of Player performing action
     * @return True if Player can play Soldier
     */
    boolean canUseSoldier(int playerID) throws PlayerExistsException;

    /**
     * Action - Player plays Soldier
     * @param playerID ID of Player performing action
     */
    void useSoldier(int playerID) throws PlayerExistsException, DevCardException;

    /**
     * Determine if Player can play Monopoly
     * Checks Player turn, and dev cards
     * @param playerID ID of Player performing action
     * @return True if Player can play Monopoly
     */
    boolean canUseMonopoly(int playerID) throws PlayerExistsException;

    /**
     * Action - Player plays Monopoly
     * @param playerID ID of Player performing action
     */
    void useMonopoly(int playerID, ResourceType type) throws PlayerExistsException, DevCardException, InsufficientResourcesException, InvalidTypeException;

    /**
     * Determine if Player can play Monument
     * Checks Player turn, and dev cards
     * @param playerID ID of Player performing action
     * @return True if Player can play Monument
     */
    boolean canUseMonument(int playerID) throws PlayerExistsException;

    /**
     * Action - Player plays Monument
     * @param playerID ID of Player performing action
     */
    void useMonument(int playerID) throws PlayerExistsException, DevCardException;

    /**
     * Determine if Player can place the Robber
     * Checks Player turn, event(ie roll 7 or play Soldier)
     * @param playerID ID of Player performing action
     * @return True if Player can place the Robber
     */
    boolean canPlaceRobber(int playerID);

    /**
     * Action - Player places the Robber
     * @param playerID ID of Player performing action
     */
    Set<Integer> placeRobber(int playerID, HexLocation hexloc) throws AlreadyRobbedException, InvalidLocationException;

    ResourceType rob(int playerrobber, int playerrobbed) throws MoveRobberException, InvalidTypeException, PlayerExistsException, InsufficientResourcesException;

    /**
     * returns boolean value denoting if the player can build a
     * road (just checks cards really)
     * @param playerID
     * @return
     */
    public boolean canBuildRoad(int playerID, EdgeLocation edge) throws InvalidPlayerException, InvalidLocationException, PlayerExistsException;

    /**
     * builds a road for the player
     */
    public void buildRoad(int playerID, EdgeLocation edge) throws InvalidPlayerException, InvalidLocationException, StructureException, PlayerExistsException;

    /**
     * checks if the player has the cards to build a settlement
     * @return
     */
    public boolean canBuildSettlement(int playerID, VertexLocation vertex) throws InvalidPlayerException, InvalidLocationException, PlayerExistsException;

    /**
     * builds a settlement for this player
     */
    public void buildSettlement(int playerID, VertexLocation vertex) throws InvalidPlayerException, InvalidLocationException, StructureException, PlayerExistsException;

    /**
     * checks if the player has the cards to ubild a city
     * @return
     */
    public boolean canBuildCity(int playerID, VertexLocation vertex) throws InvalidPlayerException, InvalidLocationException, PlayerExistsException;

    /**
     * builds a city for this player
     * @param playerID
     */
    public void buildCity(int playerID, VertexLocation vertex) throws InvalidPlayerException, InvalidLocationException, StructureException, PlayerExistsException;

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
    public boolean canBuyDevelopmentCard(int playerID) throws PlayerExistsException;

    /**
     * Buys a new developmentCard for the player
     * deducts cards
     * adds new developmentCard to his DCBank
     * @param playerID
     */
    public DevCardType buyDevelopmentCard(int playerID) throws PlayerExistsException, Exception;

    /**
     * checks if the player is in the trade sequence of his turn
     *
     * @param playerID
     * @return
     */
    public boolean canTrade(int playerID);



    /**
     * checks if that player has the card needed for that port's trade
     * @param playerID
     * @param port
     * @return
     */
    public boolean canMaritimeTrade(int playerID, PortType port) throws InvalidPlayerException, PlayerExistsException;

    /**
     * effectuates a trade based on the port type
     * @param playerID
     * @param port
     */
    public void maritimeTrade(int playerID, PortType port, ResourceType want) throws InvalidPlayerException, PlayerExistsException, InvalidTypeException, InsufficientResourcesException;


    public void maritimeTradeThree(int playerID, PortType port, ResourceType give, ResourceType want) throws InvalidPlayerException, PlayerExistsException, InsufficientResourcesException, InvalidTypeException;

    public Set<PortType> getPortTypes(int playerID) throws InvalidPlayerException;


    public Map getMap();

}