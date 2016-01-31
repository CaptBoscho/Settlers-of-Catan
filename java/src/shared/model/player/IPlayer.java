package shared.model.player;

import shared.model.resources.ResourceCard;

import java.util.List;

/**
 * Created by corne on 1/30/2016.
 */
public interface IPlayer {

    /*
    CanUseYearOfPlenty, CanUseRoadBuilder,
    CanUseSoldier, CanUseMonopoly,
    CanUseMonument, CanPlaceRobber
    */

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
    void discardCards(List<ResourceCard> cards); // TODO: 1/30/2016 Would be better with Card generic class

    /**
     * Determine if Player can roll the dice
     * Checks Player turn and phase of turn
     * @return True if Player can roll the die
     */
    boolean CanRollNumber();

    /**
     * Action - Player rolls the dice
     */
    void rollNumber();

    /**
     * Determine if Player can offer a trade
     * Checks Player turn, phase, and resources
     * @return True if Player can offer a trade
     */
    boolean CanOfferTrade();

    /**
     * Action - Player offers trade
     * @param playerId ID of Player to offer the trade
     */
    void offerTrade(int playerId);

    /**
     * Determine if Player can perform maritime trade
     * Checks Player turn, phase, resources, and ports
     * @return True if Player can perform a maritime trade
     */
    boolean CanMaritimeTrade();

    /**
     * Action - Player performs maritime trade
     * @param cardsToGive Cards to trade away
     * @param cardsToGet Cards to trade for
     */
    void maritimeTrade(List<ResourceCard> cardsToGive, List<ResourceCard> cardsToGet);

    /**
     * Determine if Player can finish their turn
     * Checks Player turn and phase
     * @return True if Player can finish their turn
     */
    boolean canFinishTurn();

    /**
     * Action - Player finishes their turn
     */
    void finishTurn();

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
     * Determine if Player can perform maritime trade
     * Checks Player turn, phase, resources, and ports
     * @return True if Player can perform a maritime trade
     */
    boolean CanMaritimeTrade();

    /**
     * Action - Player performs maritime trade
     * @param cardsToGive Cards to trade away
     * @param cardsToGet Cards to trade for
     */
    void maritimeTrade(List<ResourceCard> cardsToGive, List<ResourceCard> cardsToGet);


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
