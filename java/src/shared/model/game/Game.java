package shared.model.game;

import shared.definitions.PortType;
import shared.exceptions.FailedToRandomizeException;
import shared.model.bank.DevelopmentCardBank;
import shared.model.bank.ResourceCardBank;
import shared.model.bank.StructureBank;
import shared.model.map.Map;
import shared.model.player.Player;
import shared.model.player.PlayerManager;

import java.util.List;

/**
 * game class representing a Catan game
 */
public class Game implements IGame,IGameActions {
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


    /**
     * Starts the game, returns the Id for the first player
     *
     * @param players
     * @return Id of first player
     */
    public int initializeGame(List<Player> players) {
        //Add players to PlayerManager
        playerManager.

        //Shuffle Player Turn Order

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