package shared.model.player;

import com.google.gson.JsonArray;
import shared.definitions.CatanColor;
import shared.definitions.DevCardType;
import shared.definitions.PortType;
import shared.definitions.ResourceType;
import shared.exceptions.*;
import shared.model.bank.InvalidTypeException;
import shared.model.cards.Card;
import shared.model.cards.devcards.DevelopmentCard;
import shared.model.cards.resources.ResourceCard;

import javax.naming.InsufficientResourcesException;
import java.util.List;

/**
 * @author Kyle Cornelison
 */
public interface IPlayerManager {
    //region Manager Methods
    //==========================================================

    /**
     * Changes control of the largest army card
     * @param playerOld
     * @param playerNew
     * @throws PlayerExistsException
     */
    void changeLargestArmyPossession(int playerOld, int playerNew) throws PlayerExistsException;

    /**
     * Moves new development cards to old pile - making them playable
     * @param playerIndex
     * @throws PlayerExistsException
     * @throws BadCallerException
     */
    void moveNewToOld(int playerIndex) throws PlayerExistsException, BadCallerException;
    //endregion

    //region Can Do
    //==========================================================
    /**
     * Determine if Player can discard cards
     * Checks resource cards, robber position,
     *        and hexes from dice roll
     * @param playerIndex index of the player
     * @return True if Player can discard cards
     */
    boolean canDiscardCards(int playerIndex) throws PlayerExistsException;

    /**
     * Determine if Player can offer a trade
     * Checks Player turn, phase, and resources
     * @param playerIndex index of the player
     * @return True if Player can offer a trade
     */
    boolean canOfferTrade(int playerIndex) throws PlayerExistsException;

    /**
     * Determine if Player can perform maritime trade
     * Checks Player turn, phase, resources, and ports
     * @param playerIndex index of the player
     * @param type Type of trade
     * @return True if Player can perform a maritime trade
     */
    boolean canMaritimeTrade(int playerIndex, PortType type) throws PlayerExistsException;

    /**
     * Determine if Player can buy a dev card
     * Checks Player turn, phase, and resources
     * @param playerIndex index of the player
     * @return True if Player can buy a dev card
     */
    boolean canBuyDevCard(int playerIndex) throws PlayerExistsException;

    /**
     * Determine if Player can play Year of Plenty
     * Checks Player turn, and dev cards
     * @param playerIndex index of the player
     * @return True if Player can play Year of Plenty
     */
    boolean canUseYearOfPlenty(int playerIndex) throws PlayerExistsException;

    /**
     * Determine if Player can play Road Builder
     * Checks Player turn, and dev cards
     * @param playerIndex index of the player
     * @return True if Player can play Road Builder
     */
    boolean canUseRoadBuilder(int playerIndex) throws PlayerExistsException;

    /**
     * Determine if Player can play Soldier
     * Checks Player turn, and dev cards
     * @param playerIndex index of the player
     * @return True if Player can play Soldier
     */
    boolean canUseSoldier(int playerIndex) throws PlayerExistsException;

    /**
     * Determine if Player can play Monopoly
     * Checks Player turn, and dev cards
     * @param playerIndex index of the player
     * @return True if Player can play Monopoly
     */
    boolean canUseMonopoly(int playerIndex) throws PlayerExistsException;

    /**
     * Determine if Player can play Monument
     * Checks Player turn, and dev cards
     * @param playerIndex index of the player
     * @return True if Player can play Monument
     */
    boolean canUseMonument(int playerIndex) throws PlayerExistsException;

    /**
     * Determine if Player can place the Robber
     * Checks Player turn, event(ie roll 7 or play Soldier)
     * @param playerIndex index of the player
     * @return True if Player can place the Robber
     */
    boolean canPlaceRobber(int playerIndex) throws PlayerExistsException;

    /**
     * Determine if Player can build a road
     * Checks resource cards
     * @param playerIndex index of the player
     * @return True if Player can build a road
     */
    boolean canBuildRoad(int playerIndex) throws PlayerExistsException;

    /**
     * Determine if Player can build a settlement
     * Checks resource cards
     * @param playerIndex index of the player
     * @return True if Player can build a settlement
     */
    boolean canBuildSettlement(int playerIndex) throws PlayerExistsException;

    /**
     * Determine if Player can build a city
     * Checks resource cards
     * @param playerIndex index of the player
     * @return True if Player can build a city
     */
    boolean canBuildCity(int playerIndex) throws PlayerExistsException;
    //endregion

    //region Do methods
    /**
     * Action - Player discards cards
     * @param playerIndex index of the player
     * @param cards Cards to be discarded
     */
    List<ResourceCard> discardCards(int playerIndex, List<Card> cards) throws PlayerExistsException, InsufficientResourcesException, InvalidTypeException; // TODO: 1/30/2016 Would be better with Card generic class

    /**
     * Removes the cards from the player's bank
     * @param playerIndex
     * @param cards
     * @return
     * @throws PlayerExistsException
     * @throws InsufficientResourcesException
     * @throws InvalidTypeException
     */
    List<ResourceCard> discardResourceType(int playerIndex, List<ResourceType> cards) throws PlayerExistsException, InsufficientResourcesException, InvalidTypeException;

    /**
     * Offer domestic trade between players
     * @param playerIndexOne
     * @param playerIndexTwo
     * @param playerOneCards
     * @param playerTwoCards
     * @throws PlayerExistsException
     * @throws InsufficientResourcesException
     * @throws InvalidTypeException
     */
    void offerTrade(int playerIndexOne, int playerIndexTwo, List<ResourceType> playerOneCards, List<ResourceType> playerTwoCards) throws PlayerExistsException, InsufficientResourcesException, InvalidTypeException;

    /**
     * Action - Player buys a dev card
     * @param playerIndex index of the player
     */
    void buyDevCard(int playerIndex) throws PlayerExistsException;

    /**
     * Action - Player plays Year of Plenty
     * @param playerIndex index of the player
     */
    void useYearOfPlenty(int playerIndex) throws DevCardException,PlayerExistsException;

    /**
     * Action - Player plays Road Builder
     * @param playerIndex index of the player
     */
    void useRoadBuilder(int playerIndex) throws DevCardException,PlayerExistsException;

    /**
     * Action - Player plays Soldier
     * @param playerIndex index of the player
     */
    void useSoldier(int playerIndex) throws DevCardException,PlayerExistsException;

    /**
     * Action - Player plays Monopoly
     * @param playerIndex index of the player
     */
    void useMonopoly(int playerIndex, ResourceType type) throws DevCardException,PlayerExistsException, InvalidTypeException, InsufficientResourcesException;

    /**
     * Action - Player plays Monument
     * @param playerIndex index of the player
     */
    void useMonument(int playerIndex) throws DevCardException,PlayerExistsException;

    /**
     * Action - Player places the Robber
     * @param playerRobbing index of the player robbing
     * @param playerRobbed index of the player being robbed
     */
    void placeRobber(int playerRobbing, int playerRobbed) throws InvalidTypeException, Exception;

    /**
     * Action - Player builds a road
     * @param playerIndex index of the player
     */
    void buildRoad(int playerIndex) throws PlayerExistsException;

    /**
     * Action - Player builds a settlement
     * @param playerIndex index of the player
     */
    void buildSettlement(int playerIndex) throws PlayerExistsException;

    /**
     * Action - Player builds a city
     * @param playerIndex index of the player
     */
    void buildCity(int playerIndex) throws PlayerExistsException;
    //endregion

    //region Add card methods
    /**
     * Add the resource card to the player
     * @param playerIndex
     * @param rc
     * @throws PlayerExistsException
     */
    void addResource(int playerIndex, ResourceCard rc) throws PlayerExistsException;

    /**
     * Add the dev card to the player
     * @param playerIndex
     * @param dc
     * @throws PlayerExistsException
     */
    void addDevCard(int playerIndex, DevelopmentCard dc) throws PlayerExistsException;
    //endregion

    //region Getters
    /**
     * Get all players
     * @return a list of players
     */
    List<Player> getPlayers();

    /**
     * Get the winning player
     * @return
     * @throws GameOverException
     */
    Player getWinner() throws GameOverException;

    /**
     * Gets a player by id
     * @param id ID of the Player
     * @return The Player with the specified ID
     * @throws PlayerExistsException
     */
    Player getPlayerByID(int id) throws PlayerExistsException;

    /**
     * Gets the specified player's color
     * @param playerIndex
     * @return
     * @throws PlayerExistsException
     */
    CatanColor getPlayerColorByIndex(int playerIndex) throws PlayerExistsException;

    /**
     * Gets a player by index
     * @param index Index of the player
     * @return Player at index
     * @throws PlayerExistsException
     */
    Player getPlayerByIndex(int index) throws PlayerExistsException;

    /**
     * Get the number of resource cards the player has
     * @param playerIndex
     * @return
     * @throws PlayerExistsException
     */
    int getNumberResourceCards(int playerIndex) throws PlayerExistsException;

    /**
     * Get number of roads the player has left
     * @param playerIndex
     * @return
     * @throws PlayerExistsException
     */
    int getAvailableRoads(int playerIndex) throws PlayerExistsException;

    /**
     * Get the number of settlements the player has left
     * @param playerIndex
     * @return
     * @throws PlayerExistsException
     */
    int getAvailableSettlements(int playerIndex) throws PlayerExistsException;

    /**
     * Get the number of cities the player has left
     * @param playerIndex
     * @return
     * @throws PlayerExistsException
     */
    int getAvailableCities(int playerIndex)throws PlayerExistsException;

    /**
     * Get number of dev cards the player has
     * @param type
     * @param playerIndex
     * @return
     */
    int getNumberDevCards(DevCardType type, int playerIndex);

    /**
     * Get the number of soldier cards the player has played
     * @param playerIndex
     * @return
     */
    int getNumberOfSoldiers(int playerIndex);

    /**
     * Determine if the user has discarded
     * @param playerIndex
     * @return
     */
    boolean hasDiscarded(int playerIndex);

    /**
     * Get the player by name
     * @param name
     * @return
     * @throws PlayerExistsException
     */
    Player getPlayerByName(String name) throws PlayerExistsException;

    /**
     * Get the player's color
     * @param name
     * @return
     */
    CatanColor getPlayerColorByName(String name);

    JsonArray toJSON();

    void changeLongestRoadPossession(int oldOwnerIndex, int newOwnerIndex) throws PlayerExistsException;

    void finishTurn(int playerIndex) throws PlayerExistsException, BadCallerException;
    //endregion
}
