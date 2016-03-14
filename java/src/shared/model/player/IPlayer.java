package shared.model.player;

import shared.definitions.DevCardType;
import shared.definitions.PortType;
import shared.definitions.ResourceType;
import shared.exceptions.BadCallerException;
import shared.exceptions.DevCardException;
import shared.exceptions.MoveRobberException;
import shared.exceptions.PlayerExistsException;
import shared.model.bank.InvalidTypeException;

import shared.model.cards.Card;
import shared.model.cards.resources.ResourceCard;

import javax.naming.InsufficientResourcesException;
import java.util.List;

/**
 * @author Kyle Cornelison
 * Player Interface - has required method stubs
 */
public interface IPlayer {
    //region Can do methods
    /**
     * Determine if Player can discard cards
     * Checks resource cards, robber position,
     *        and hexes from dice roll
     * @return True if Player can discard cards
     */
    boolean canDiscardCards();

    /**
     * Determine if Player can offer a trade
     * Checks Player turn, phase, and resources
     * @return True if Player can offer a trade
     */
    boolean canOfferTrade();

    /**
     * Determine if Player can perform maritime trade
     * Checks Player turn, phase, resources, and ports
     * @param type Type of trade
     * @return True if Player can perform a maritime trade
     */
    boolean canMaritimeTrade(PortType type);

    /**
     * Determine if Player can buy a dev card
     * Checks Player turn, phase, and resources
     * @return True if Player can buy a dev card
     */
    boolean canBuyDevCard();

    /**
     * Determine if Player can play Year of Plenty
     * Checks Player turn, and dev cards
     * @return True if Player can play Year of Plenty
     */
    boolean canUseYearOfPlenty();

    /**
     * Determine if Player can play Soldier
     * Checks Player turn, and dev cards
     * @return True if Player can play Soldier
     */
    boolean canUseSoldier();

    /**
     * Determine if Player can play Road Builder
     * Checks Player turn, and dev cards
     * @return True if Player can play Road Builder
     */
    boolean canUseRoadBuilder();

    /**
     * Determine if Player can play Monopoly
     * Checks Player turn, and dev cards
     * @return True if Player can play Monopoly
     */
    boolean canUseMonopoly();

    /**
     * Determine if Player can play Monument
     * Checks Player turn, and dev cards
     * @return True if Player can play Monument
     */
    boolean canUseMonument();

    /**
     * Determine if Player can place the Robber
     * Checks Player turn, event(ie roll 7 or play Soldier)
     * @return True if Player can place the Robber
     */
    boolean canPlaceRobber();

    /**
     * Determine if Player can build a road
     * Checks resource cards
     * @return True if Player can build a road
     */
    boolean canBuildRoad();

    /**
     * Determine if Player can build a settlement
     * Checks resource cards
     * @return True if Player can build a settlement
     */
    boolean canBuildSettlement();

    /**
     * Determine if Player can build a city
     * Checks resource cards
     * @return True if Player can build a city
     */
    boolean canBuildCity();
    //endregion

    //region Do methods
    /**
     * Action - Player discards cards
     * @param cards Cards to be discarded
     */
    List<ResourceCard> discardCards(List<Card> cards) throws InsufficientResourcesException, InvalidTypeException; // TODO: 1/30/2016 Would be better with Card generic class

    /**
     * Action - Player discards resource cards
     * @param cards
     * @return
     * @throws InsufficientResourcesException
     * @throws InvalidTypeException
     */
    List<ResourceCard> discardResourceCards(List<ResourceType> cards) throws InsufficientResourcesException, InvalidTypeException;

    /**
     * Action - Player buys a dev card
     */
    void buyDevCard();

    /**
     * Action - Player plays Year of Plenty
     */
    void useYearOfPlenty() throws DevCardException;

    /**
     * Action - Player plays Road Builder
     */
    void useRoadBuilder() throws DevCardException;

    /**
     * Action - Player plays Soldier
     */
    void useSoldier() throws DevCardException;

    /**
     * Action - Player plays Monopoly
     */
    void discardMonopoly() throws DevCardException;

    /**
     * Action - Player plays Monument
     */
    void useMonument() throws DevCardException;

    /**
     * Action - Player places the Robber
     */
    void placeRobber() throws MoveRobberException;

    /**
     * Action - Player builds a road
     */
    void buildRoad();

    /**
     * Action - Player builds a settlement
     */
    void buildSettlement();

    /**
     * Action - Player builds a city
     */
    void buildCity();

    /**
     * Action - Player loses the largest army card to another player
     */
    void loseArmyCard();

    /**
     * Action - Player wins the largest army card from another player
     */
    void winArmyCard();

    /**
     * Action - Player robbed by another player
     * @return
     * @throws InsufficientResourcesException
     * @throws InvalidTypeException
     */
    ResourceCard robbed() throws InsufficientResourcesException, InvalidTypeException;

    /**
     * Moves new development cards to old pile making them playable
     * @throws BadCallerException
     */
    void moveNewToOld() throws BadCallerException;
    //endregion

    //region Getters
    /**
     * Get the number of resource cards the player has
     * @return
     */
    int getNumberResourceCards();

    /**
     * Get the number of resource cards the player has matching the specified type
     * @param resourceType
     * @return
     */
    int getNumberOfType(ResourceType resourceType);

    /**
     * Get the number of dev cards the player has of the specified type
     * @param type
     * @return
     */
    int getNumberOfDevCardsByType(DevCardType type);

    /**
     * Determine how many resources the player has of the specified type
     * @param resource
     * @return
     * @throws InvalidTypeException
     */
    Integer howManyOfThisCard(ResourceType resource)throws InvalidTypeException;

    /**
     * Get the number of total dev cards the player has
     * @return
     */
    Integer quantityOfDevCards();
    //endregion
}
