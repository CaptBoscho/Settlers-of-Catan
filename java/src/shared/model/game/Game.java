package shared.model.game;

import shared.definitions.PortType;
import shared.definitions.ResourceType;
import shared.model.bank.DevelopmentCardBank;
import shared.model.bank.ResourceCardBank;
import shared.definitions.DevCardType;
import shared.model.map.Map;
import shared.model.player.Player;
import shared.model.player.PlayerManager;
import shared.model.resources.ResourceCard;

import java.util.List;

/**
 * game class representing a Catan game
 */
public class Game implements IGame {
    private Dice dice;
    private Map map;
    private TurnTracker turnTracker;
    private LongestRoad longestRoadCard;
    private LargestArmy largestArmyCard;
    private PlayerManager playerManager;
    private ResourceCardBank resourceCardBank;
    private DevelopmentCardBank developmentCardBank;

    /**
     * Constructor
     */
    public Game() {
        this.dice = new Dice();
        this.map = new Map();
        this.turnTracker = null;//new TurnTracker(0,0);
        this.longestRoadCard = new LongestRoad();
        this.largestArmyCard = new LargestArmy();
        this.playerManager = new PlayerManager();
        this.resourceCardBank = new ResourceCardBank(this);
        this.developmentCardBank = new DevelopmentCardBank(this);
    }

    //IGame Methods
    //======================================================
    /**
     * Starts the game, returns the Id for the first player
     *
     * @param players
     * @return Id of first player
     */
    public int initializeGame(List<Player> players) {
        //Add players to PlayerManager


        //Shuffle Player Turn Order
        return 0;
    }

    /**
     * returns the playerID for whose turn it is
     *
     * @return
     */
    @Override
    public int getCurrentTurn() {
        return 0;
    }

    /**
     * Determine if Player can discard cards
     * Checks resource cards, robber position,
     * and hexes from dice roll
     *
     * @param playerID ID of Player performing action
     * @return True if Player can discard cards
     */
    @Override
    public boolean canDiscardCards(int playerID) {
        return false;
    }

    /**
     * Action - Player discards cards
     *
     * @param playerID ID of Player performing action
     * @param cards    Cards to be discarded
     */
    @Override
    public void discardCards(int playerID, List<ResourceCard> cards) {

    }

    /**
     * Determine if Player can roll the dice
     * Checks Player turn and phase of turn
     *
     * @param playerID ID of Player performing action
     * @return True if Player can roll the die
     */
    @Override
    public boolean canRollNumber(int playerID) {
        return false;
    }

    /**
     * Action - Player rolls the dice
     *
     * @param playerID ID of Player performing action
     */
    @Override
    public void rollNumber(int playerID) {

    }

    /**
     * Determine if Player can offer a trade
     * Checks Player turn, phase, and resources
     *
     * @param playerID ID of Player performing action
     * @return True if Player can offer a trade
     */
    @Override
    public boolean canOfferTrade(int playerID) {
        return false;
    }

    /**
     * Action - Player offers trade
     *
     * @param playerID   ID of Player offering the trade
     * @param playerIDTo ID of Player being offered the trade
     */
    @Override
    public void offerTrade(int playerID, int playerIDTo) {

    }

    /**
     * Determine if Player can perform maritime trade
     * Checks Player turn, phase, resources, and ports
     *
     * @param playerID ID of Player performing action
     * @return True if Player can perform a maritime trade
     */
    @Override
    public boolean canMaritimeTrade(int playerID) {
        return false;
    }

    /**
     * Action - Player performs maritime trade
     *
     * @param playerID    ID of Player performing action
     * @param cardsToGive Cards to trade away
     * @param cardsToGet  Cards to trade for
     */
    @Override
    public void maritimeTrade(int playerID, List<ResourceCard> cardsToGive, List<ResourceCard> cardsToGet) {

    }

    /**
     * Determine if Player can finish their turn
     * Checks Player turn and phase
     *
     * @param playerID ID of Player performing action
     * @return True if Player can finish their turn
     */
    @Override
    public boolean canFinishTurn(int playerID) {
        return false;
    }

    /**
     * Action - Player finishes their turn
     *
     * @param playerID ID of Player performing action
     */
    @Override
    public void finishTurn(int playerID) {

    }

    /**
     * Determine if Player can buy a dev card
     * Checks Player turn, phase, and resources
     *
     * @param playerID ID of Player performing action
     * @return True if Player can buy a dev card
     */
    @Override
    public boolean canBuyDevCard(int playerID) {
        return false;
    }

    /**
     * Action - Player buys a dev card
     *
     * @param playerID ID of Player performing action
     */
    @Override
    public void buyDevCard(int playerID) {

    }

    /**
     * Determine if Player can play Year of Plenty
     * Checks Player turn, and dev cards
     *
     * @param playerID ID of Player performing action
     * @return True if Player can play Year of Plenty
     */
    @Override
    public boolean canUseYearOfPlenty(int playerID) {
        return false;
    }

    /**
     * Action - Player plays Year of Plenty
     *
     * @param playerID ID of Player performing action
     */
    @Override
    public void useYearOfPlenty(int playerID) {

    }

    /**
     * Determine if Player can play Road Builder
     * Checks Player turn, and dev cards
     *
     * @param playerID ID of Player performing action
     * @return True if Player can play Road Builder
     */
    @Override
    public boolean canUseRoadBuilder(int playerID) {
        return false;
    }

    /**
     * Action - Player plays Road Builder
     *
     * @param playerID ID of Player performing action
     */
    @Override
    public void useRoadBuilder(int playerID) {

    }

    /**
     * Determine if Player can play Soldier
     * Checks Player turn, and dev cards
     *
     * @param playerID ID of Player performing action
     * @return True if Player can play Soldier
     */
    @Override
    public boolean canUseSoldier(int playerID) {
        return false;
    }

    /**
     * Action - Player plays Soldier
     *
     * @param playerID ID of Player performing action
     */
    @Override
    public void useSoldier(int playerID) {

    }

    /**
     * Determine if Player can play Monopoly
     * Checks Player turn, and dev cards
     *
     * @param playerID ID of Player performing action
     * @return True if Player can play Monopoly
     */
    @Override
    public boolean canUseMonopoly(int playerID) {
        return false;
    }

    /**
     * Action - Player plays Monopoly
     *
     * @param playerID ID of Player performing action
     */
    @Override
    public void useMonopoly(int playerID) {

    }

    /**
     * Determine if Player can play Monument
     * Checks Player turn, and dev cards
     *
     * @param playerID ID of Player performing action
     * @return True if Player can play Monument
     */
    @Override
    public boolean canUseMonument(int playerID) {
        return false;
    }

    /**
     * Action - Player plays Monument
     *
     * @param playerID ID of Player performing action
     */
    @Override
    public void useMonument(int playerID) {

    }

    /**
     * Determine if Player can place the Robber
     * Checks Player turn, event(ie roll 7 or play Soldier)
     *
     * @param playerID ID of Player performing action
     * @return True if Player can place the Robber
     */
    @Override
    public boolean canPlaceRobber(int playerID) {
        return false;
    }

    /**
     * Action - Player places the Robber
     *
     * @param playerID ID of Player performing action
     */
    @Override
    public void placeRobber(int playerID) {

    }

    /**
     * returns boolean value denoting if the player can build a
     * road (just checks cards really)
     *
     * @param playerID
     * @return
     */
    @Override
    public boolean canBuildRoad(int playerID) {
        return false;
    }

    /**
     * builds a road for hte player
     *
     * @param playerID
     */
    @Override
    public void buildRoad(int playerID) {

    }

    /**
     * checks if the player has the cards to build a settlement
     *
     * @param playerID
     * @return
     */
    @Override
    public boolean canBuildSettlement(int playerID) {
        return false;
    }

    /**
     * builds a settlement for this player
     *
     * @param playerID
     */
    @Override
    public void buildSettlement(int playerID) {


    }

    /**
     * checks if the player has the cards to ubild a city
     *
     * @param playerID
     * @return
     */
    @Override
    public boolean canBuildCity(int playerID) {
        return false;
    }

    /**
     * builds a city for this player
     *
     * @param playerID
     */
    @Override
    public void buildCity(int playerID) {

    }

    /**
     * returns the value of how many roads is the LongestRoad
     *
     * @return
     */
    @Override
    public int currentLongestRoadSize() {
        return 0;
    }

    /**
     * returns the playerID of who owns the current longest road
     *
     * @return
     */
    @Override
    public int currentLongestRoadPlayer() {
        return 0;
    }

    /**
     * deducts Victory Points from playerIDOld
     * adds Victory Points to playerIDNew
     * Updates LongestRoad for playerIDNew and roadSize
     *
     * @param playerIDOld
     * @param playerIDNew
     * @param roadSize
     */
    @Override
    public void newLongestRoad(int playerIDOld, int playerIDNew, int roadSize) {

    }

    /**
     * checks if the player has the cards to buy a DevelopmentCard
     *
     * @param playerID
     * @return
     */
    @Override
    public boolean canBuyDevelopmentCard(int playerID) {
        return false;
    }

    /**
     * Buys a new developmentCard for the player
     * deducts cards
     * adds new developmentCard to his DCBank
     *
     * @param playerID
     */
    @Override
    public DevCardType buyDevelopmentCard(int playerID) {
        return null;
    }

    /**
     * checks if the player is in the trade sequence of his turn
     *
     * @param playerID
     * @return
     */
    @Override
    public boolean canTrade(int playerID) {
        return false;
    }

    /**
     * checks if that player has the card needed for that port's trade
     *
     * @param playerID
     * @param port
     * @return
     */
    @Override
    public boolean canMaritimeTrade(int playerID, PortType port) {
        return false;
    }

    /**
     * effectuates a trade based on the port type
     *
     * @param playerID
     * @param port
     */
    @Override
    public void maritimeTrade(int playerID, PortType port) {

    }

    /**
     * checks if player can play that dc
     *
     * @param playerID
     * @param dc
     * @return
     */
    @Override
    public boolean canPlayDevelopmentCard(int playerID, DevCardType dc) {
        return false;
    }

    /**
     * plays that development card
     *
     * @param playerID
     * @param dc
     */
    public void playDevelopmentCard(int playerID, DevCardType dc) {

    }

    /**
     * effectuates a trade between playerOneID and playerTwoID
     * trades the cards in the two lists
     *
     * @param playerOneID
     * @param onecards
     * @param playerTwoID
     * @param twocards
     */
    public void tradePlayer(int playerOneID, List<ResourceType> onecards, int playerTwoID, List<ResourceType> twocards) {

    }

    /*======================================================
    * Private - Helper Methods
    * ======================================================*/
    /**
     * Randomize the players' turn order
     */
    private void randomizePlayers() {
        try{
            playerManager.randomizePlayers();
        } catch(Exception e) {
            //throw new
        }
    }
}