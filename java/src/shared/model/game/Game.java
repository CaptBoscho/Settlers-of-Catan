package shared.model.game;

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
public class Game {
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
     * Initialize a new game
     * @param players List of players for this game
     */
    public void initializeGame(List<Player> players) {
        //Add the new players to the player manager

        //Shuffle the players' turn order
        this.randomizePlayers();

        //Initiate picking of settlement placements
        this.placeSettlements();

        //Give players their resources
        //this.map.giveInitialResources()

        //Start the game
        this.startGame();
    }

    /**
     * Randomize the players' turn order
     */
    private void randomizePlayers() {
        try{
            playerManager.randomizePlayers();
        } catch(Exception e) {

        }
    }

    /**
     * Place settlements phase of setup
     */
    private void placeSettlements() {
        //First phase order
        for(Player player : playerManager.getPlayers()) {
            //Set player turn
        }

        //Second phase order
        for(int i = playerManager.getPlayers().size() - 1; i >= 0; i--) {
            //Set player turn
        }
    }

    /**
     * Starts the game - after setup
     * @return id of winning player
     */
    private int startGame(){
        return 0;
    }

    /**
     * Handles a player's turn
     * @param player Player who's turn it is
     */
    private void playTurn(Player player){
        //Dev Card - technically one card can be played at anytime during a player's turn
//        try {
//            this.playDevCard(player.getDevelopmentCardBank());
//        }catch(Exception e){
//
//        }

        //Roll dice
        int roll = dice.roll();

        //Pass resources
        //map.giveResources(roll, 1); //// TODO: 1/24/2016 should be one that gives to all players

        //Trade Phase
        this.trade();

        //Build Phase
        this.build(player.getStructureBank());
    }

    /**
     * Initializes playing of a development card
     * @param devCrdBnk Development Card Bank from which to play a card
     * @throws Exception
     */
    private void playDevCard(DevelopmentCardBank devCrdBnk) throws Exception{
        //Init play of dev card
    }

    /**
     * Trade resources between players - Initializes phase
     */
    private void trade() {

    }

    /**
     * Build a building = Initializes phase
     * @param strBnk
     */
    private void build(StructureBank strBnk) {

    }

    /**
     * Gets the player with the longest road
     * @return Player with longest road or null if no player has it
     */
    public Player getPlayerWithLongestRoad() {
        if(this.longestRoadCard.getOwner() != -1){
            try {
                return playerManager.getPlayerByIndex(this.longestRoadCard.getOwner());
            } catch(Exception e) {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * Gets the player with the largest army
     * @return Player with largest army or null if no player has it
     */
    public Player getPlayerWithLargestArmy() {
        if(this.largestArmyCard.getOwner() != -1){
            try {
                return playerManager.getPlayerByIndex(this.largestArmyCard.getOwner());
            } catch(Exception e) {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * Get who's turn it is (by index)
     * @return index of the current player
     */
    public int currentTurn(){
        return turnTracker.getCurrentTurn();
    }

    /**
     * Boolean representing whether or not it's the specified player's turn
     * @param index position of player
     * @return True if it's the player's turn
     */
    public boolean isPlayerTurn(int index){
        return currentTurn() == index ? true : false;
    }

    //Game Can Do Methods - Maybe move the actual calculations to the player manager???
    /**
     * Boolean representing whether or not the player can take their turn
     * @param index position of the player
     * @return True if the player can play
     */
    public boolean canPlay(int index){
        return isPlayerTurn(index);
    }

    /**
     * Boolean representing whether the two players can trade
     * @param playerOne Player that wants to trade
     * @param playerTwo Player that wants to trade
     * @return True if the players can trade
     */
    public boolean canTrade(int playerOne, int playerTwo){
        return isPlayerTurn(playerOne) || isPlayerTurn(playerTwo);
    }

    /*===========================================
                   Getters/Setters
     ============================================*/
    public Dice getDice() {
        return dice;
    }

    public void setDice(Dice dice) {
        this.dice = dice;
    }

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public LongestRoad getLongestRoadCard() {
        return longestRoadCard;
    }

    public void setLongestRoadCard(LongestRoad longestRoadCard) {
        this.longestRoadCard = longestRoadCard;
    }

    public LargestArmy getLargestArmyCard() {
        return largestArmyCard;
    }

    public void setLargestArmyCard(LargestArmy largestArmyCard) {
        this.largestArmyCard = largestArmyCard;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public void setPlayerManager(PlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    public ResourceCardBank getResourceCardBank() {
        return resourceCardBank;
    }

    public void setResourceCardBank(ResourceCardBank resourceCardBank) {
        this.resourceCardBank = resourceCardBank;
    }

    public DevelopmentCardBank getDevelopmentCardBank() {
        return developmentCardBank;
    }

    public void setDevelopmentCardBank(DevelopmentCardBank developmentCardBank) {
        this.developmentCardBank = developmentCardBank;
    }
}