package shared.model.game;

import shared.model.bank.DevelopmentCardBank;
import shared.model.bank.ResourceCardBank;
import shared.model.map.Map;
import shared.model.player.Player;
import shared.model.player.PlayerManager;

/**
 * game class representing a Catan game
 */
public class Game{
    private Dice dice;
    private Map map;
    private Robber robber;
    private TurnTracker turnTracker;
    private LongestRoad longestRoadCard;
    private LargestArmy largestArmyCard;
    private PlayerManager playerManager;
    private ResourceCardBank resourceCardBank;
    private DevelopmentCardBank developmentCardBank;

    /**
     * Constructor
     */
    public Game(){
        this.dice = new Dice();
        this.map = new Map();
        this.robber = new Robber();
        this.turnTracker = new TurnTracker(0,0);
        this.longestRoadCard = new LongestRoad();
        this.largestArmyCard = new LargestArmy();
        this.playerManager = new PlayerManager();
        this.resourceCardBank = new ResourceCardBank(this);
        this.developmentCardBank = new DevelopmentCardBank(this);
    }

    /**
     * Gets the player with the longest road
     * @return Player with longest road or null if no player has it
     */
    public Player getPlayerWithLongestRoad(){
        if(this.longestRoadCard.getOwner() != -1){
            return playerManager.getPlayerByIndex(this.longestRoadCard.getOwner());
        }else {
            return null;
        }
    }

    /**
     * Gets the player with the largest army
     * @return Player with largest army or null if no player has it
     */
    public Player getPlayerWithLargestArmy(){
        if(this.largestArmyCard.getOwner() != -1){
            return playerManager.getPlayerByIndex(this.largestArmyCard.getOwner());
        }else {
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
        return (isPlayerTurn(playerOne) || isPlayerTurn(playerTwo)) ? true : false;
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

    public Robber getRobber() {
        return robber;
    }

    public void setRobber(Robber robber) {
        this.robber = robber;
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