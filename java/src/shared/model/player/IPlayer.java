package shared.model.player;

import shared.definitions.DevCardType;
import shared.definitions.PortType;
import shared.definitions.ResourceType;
import shared.exceptions.BadCallerException;
import shared.exceptions.DevCardException;
import shared.exceptions.MoveRobberException;
import shared.model.bank.InvalidTypeException;

import shared.model.cards.Card;
import shared.model.cards.resources.ResourceCard;

import javax.naming.InsufficientResourcesException;
import java.util.List;

/**
 * Created by corne on 1/30/2016.
 * Player Interface - has required method stubs
 */
public interface IPlayer {

    /**
     * Determine if Player can discard cards
     * Checks resource cards, robber position,
     *        and hexes from dice roll
     * @return True if Player can discard cards
     */
    boolean canDiscardCards();

    /**
     * Action - Player discards cards
     * @param cards Cards to be discarded
     */
    List<ResourceCard> discardCards(List<Card> cards) throws InsufficientResourcesException, InvalidTypeException; // TODO: 1/30/2016 Would be better with Card generic class


    List<ResourceCard> discardResourceCards(List<ResourceType> cards) throws InsufficientResourcesException, InvalidTypeException;
    /**
     * Determine if Player can offer a trade
     * Checks Player turn, phase, and resources
     * @return True if Player can offer a trade
     */
    boolean canOfferTrade();

    Integer howManyOfThisCard(ResourceType t)throws InvalidTypeException;

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
     * Action - Player buys a dev card
     */
    void buyDevCard();

    /**
     * Determine if Player can play Year of Plenty
     * Checks Player turn, and dev cards
     * @return True if Player can play Year of Plenty
     */
    boolean canUseYearOfPlenty();

    /**
     * Action - Player plays Year of Plenty
     */
    void useYearOfPlenty() throws DevCardException;

    /**
     * Determine if Player can play Road Builder
     * Checks Player turn, and dev cards
     * @return True if Player can play Road Builder
     */
    boolean canUseRoadBuilder();

    /**
     * Action - Player plays Road Builder
     */
    void useRoadBuilder() throws DevCardException;

    /**
     * Determine if Player can play Soldier
     * Checks Player turn, and dev cards
     * @return True if Player can play Soldier
     */
    boolean canUseSoldier();

    /**
     * Action - Player plays Soldier
     */
    void useSoldier() throws DevCardException;

    /**
     * Determine if Player can play Monopoly
     * Checks Player turn, and dev cards
     * @return True if Player can play Monopoly
     */
    boolean canUseMonopoly();

    /**
     * Action - Player plays Monopoly
     */
    void discardMonopoly() throws DevCardException;

    /**
     * Determine if Player can play Monument
     * Checks Player turn, and dev cards
     * @return True if Player can play Monument
     */
    boolean canUseMonument();

    /**
     * Action - Player plays Monument
     */
    void useMonument() throws DevCardException;

    /**
     * Determine if Player can place the Robber
     * Checks Player turn, event(ie roll 7 or play Soldier)
     * @return True if Player can place the Robber
     */
    boolean canPlaceRobber();

    /**
     * Action - Player places the Robber
     */
    void placeRobber() throws MoveRobberException;

    /**
     * Determine if Player can build a road
     * Checks resource cards
     * @return True if Player can build a road
     */
    boolean canBuildRoad();

    /**
     * Action - Player builds a road
     */
    void buildRoad();

    /**
     * Determine if Player can build a settlement
     * Checks resource cards
     * @return True if Player can build a settlement
     */
    boolean canBuildSettlement();

    /**
     * Action - Player builds a settlement
     */
    void buildSettlement();

    /**
     * Determine if Player can build a city
     * Checks resource cards
     * @return True if Player can build a city
     */
    boolean canBuildCity();

    /**
     * Action - Player builds a city
     */
    void buildCity();


    void playKnight();


    Integer getKnights();

    void loseArmyCard();

    void winArmyCard();

    ResourceCard robbed() throws InsufficientResourcesException, InvalidTypeException;

    Integer quantityOfDevCards();

    void moveNewToOld() throws BadCallerException;

    int getNumberOfDevCardsByType(DevCardType type);
}
