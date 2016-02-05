package shared.model.player;

import shared.definitions.PortType;
import shared.definitions.ResourceType;
import shared.exceptions.DevCardException;
import shared.exceptions.MoveRobberException;
import shared.model.game.trade.TradeType;
import shared.model.resources.ResourceCard;

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
    void discardCards(List<ResourceType> cards); // TODO: 1/30/2016 Would be better with Card generic class

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
    void useMonopoly() throws DevCardException;

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
}
