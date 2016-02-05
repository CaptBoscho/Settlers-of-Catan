package shared.model.player;

import shared.definitions.PortType;
import shared.definitions.ResourceType;
import shared.exceptions.*;
import shared.model.bank.InvalidTypeException;
import shared.model.game.trade.TradeType;
import shared.model.resources.ResourceCard;

import javax.naming.InsufficientResourcesException;
import javax.security.sasl.AuthenticationException;
import java.util.List;

/**
 * Created by corne on 1/30/2016.
 */
public interface IPlayerManager {

    //Manager Methods
    //==========================================================
    /**
     * Randomize player order (turn order)
     * @throws FailedToRandomizeException
     */
    List<Integer> randomizePlayers() throws FailedToRandomizeException;

    /**
     * Authenticates a player
     * @param id ID of the player
     * @return True if player authentication is successful
     * @throws AuthenticationException
     */
    boolean authenticatePlayer(int id) throws AuthenticationException;

    /**
     * Gets a player by id
     * @param id ID of the Player
     * @return The Player with the specified ID
     * @throws PlayerExistException
     */
    Player getPlayerByID(int id) throws PlayerExistException;

    /**
     * Gets a player by index
     * @param index Index of the player
     * @return Player at index
     * @throws PlayerExistException
     */
    Player getPlayerByIndex(int index) throws PlayerExistException;

    Integer getKnights(int playerID) throws PlayerExistException;

    void playKnight(int playerID) throws PlayerExistException;

    void changeLargestArmyPossession(int playerold, int playernew) throws PlayerExistException;


    //Can Do & Do
    //==========================================================
    /**
     * Determine if Player can discard cards
     * Checks resource cards, robber position,
     *        and hexes from dice roll
     * @param id ID of the player
     * @return True if Player can discard cards
     */
    boolean canDiscardCards(int id) throws PlayerExistException;

    /**
     * Action - Player discards cards
     * @param id ID of the player
     * @param cards Cards to be discarded
     */
    List<ResourceCard> discardCards(int id, List<ResourceType> cards) throws PlayerExistException, InsufficientResourcesException, InvalidTypeException; // TODO: 1/30/2016 Would be better with Card generic class


    void addResource(int id, ResourceCard rc) throws PlayerExistException;

    /**
     * Determine if Player can offer a trade
     * Checks Player turn, phase, and resources
     * @param id ID of the player
     * @return True if Player can offer a trade
     */
    boolean canOfferTrade(int id) throws PlayerExistException;

    /**
     * Determine if Player can perform maritime trade
     * Checks Player turn, phase, resources, and ports
     * @param id ID of the player
     * @param type Type of trade
     * @return True if Player can perform a maritime trade
     */
    boolean canMaritimeTrade(int id, PortType type) throws PlayerExistException;


    void maritimeTrade(int id, PortType type, ResourceType want) throws InvalidTypeException, PlayerExistException;

    /**
     * Determine if Player can buy a dev card
     * Checks Player turn, phase, and resources
     * @param id ID of the player
     * @return True if Player can buy a dev card
     */
    boolean canBuyDevCard(int id) throws PlayerExistException;

    /**
     * Action - Player buys a dev card
     * @param id ID of the player
     */
    void buyDevCard(int id) throws PlayerExistException;

    /**
     * Determine if Player can play Year of Plenty
     * Checks Player turn, and dev cards
     * @param id ID of the player
     * @return True if Player can play Year of Plenty
     */
    boolean canUseYearOfPlenty(int id) throws PlayerExistException;

    /**
     * Action - Player plays Year of Plenty
     * @param id ID of the player
     */
    void useYearOfPlenty(int id) throws DevCardException,PlayerExistException;

    /**
     * Determine if Player can play Road Builder
     * Checks Player turn, and dev cards
     * @param id ID of the player
     * @return True if Player can play Road Builder
     */
    boolean canUseRoadBuilder(int id) throws PlayerExistException;

    /**
     * Action - Player plays Road Builder
     * @param id ID of the player
     */
    void useRoadBuilder(int id) throws DevCardException,PlayerExistException;

    /**
     * Determine if Player can play Soldier
     * Checks Player turn, and dev cards
     * @param id ID of the player
     * @return True if Player can play Soldier
     */
    boolean canUseSoldier(int id) throws PlayerExistException;

    /**
     * Action - Player plays Soldier
     * @param id ID of the player
     */
    void useSoldier(int id) throws DevCardException,PlayerExistException;

    /**
     * Determine if Player can play Monopoly
     * Checks Player turn, and dev cards
     * @param id ID of the player
     * @return True if Player can play Monopoly
     */
    boolean canUseMonopoly(int id) throws PlayerExistException;

    /**
     * Action - Player plays Monopoly
     * @param id ID of the player
     */
    void useMonopoly(int id) throws DevCardException,PlayerExistException;

    /**
     * Determine if Player can play Monument
     * Checks Player turn, and dev cards
     * @param id ID of the player
     * @return True if Player can play Monument
     */
    boolean canUseMonument(int id) throws PlayerExistException;

    /**
     * Action - Player plays Monument
     * @param id ID of the player
     */
    void useMonument(int id) throws DevCardException,PlayerExistException;

    /**
     * Determine if Player can place the Robber
     * Checks Player turn, event(ie roll 7 or play Soldier)
     * @param id ID of the player
     * @return True if Player can place the Robber
     */
    boolean canPlaceRobber(int id) throws PlayerExistException;

    /**
     * Action - Player places the Robber
     * @param robber ID of the player
     */
    ResourceType placeRobber(int robber, int robbed) throws MoveRobberException,PlayerExistException, InsufficientResourcesException, InvalidTypeException;

    /**
     * Determine if Player can build a road
     * Checks resource cards
     * @param id ID of the player
     * @return True if Player can build a road
     */
    boolean canBuildRoad(int id) throws PlayerExistException;

    /**
     * Action - Player builds a road
     * @param id ID of the player
     */
    void buildRoad(int id) throws PlayerExistException;

    /**
     * Determine if Player can build a settlement
     * Checks resource cards
     * @param id ID of the player
     * @return True if Player can build a settlement
     */
    boolean canBuildSettlement(int id) throws PlayerExistException;

    /**
     * Action - Player builds a settlement
     * @param id ID of the player
     */
    void buildSettlement(int id) throws PlayerExistException;

    /**
     * Determine if Player can build a city
     * Checks resource cards
     * @param id ID of the player
     * @return True if Player can build a city
     */
    boolean canBuildCity(int id) throws PlayerExistException;

    /**
     * Action - Player builds a city
     * @param id ID of the player
     */
    void buildCity(int id) throws PlayerExistException;
}
