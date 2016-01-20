package shared.model;

import java.util.List;

/**
 * Game class representing a Catan game
 */
public class Game{
    private Dice dice;
    private GameMap map;
    private Robber robber;
    private Integer longestRoadCard;
    private Integer largestArmyCard;
    private PlayerManager playerManager;
    private ResourceCardBank resourceCardBank;
    private DevelopmentCardBank developmentCardBank;

    /**
     * Constructor
     */
    public Game(){
        this.dice = new Dice();
        this.map = new GameMap();
        this.robber = new Robber();
        this.longestRoadCard = -1; //// TODO: 1/19/2016 Should special victory cards be classes? 
        this.largestArmyCard = -1;
        this.playerManager = new PlayerManager();
        this.resourceCardBank = new ResourceCardBank();
        this.developmentCardBank = new DevelopmentCardBank();
    }

    /**
     * Gets the player with the longest road
     * @return Player with longest road or null if no player has it
     */
    public Player getPlayerWithLongestRoad(){
        if(this.longestRoadCard != -1){
            return playerManager.getPlayerByIndex(this.longestRoadCard);
        }else {
            return null;
        }
    }

    /**
     * Gets the player with the largest army
     * @return Player with largest army or null if no player has it
     */
    public Player getPlayerWithLargestArmy(){
        if(this.largestArmyCard != -1){
            return playerManager.getPlayerByIndex(this.largestArmyCard);
        }else {
            return null;
        }
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

    public GameMap getMap() {
        return map;
    }

    public void setMap(GameMap map) {
        this.map = map;
    }

    public Robber getRobber() {
        return robber;
    }

    public void setRobber(Robber robber) {
        this.robber = robber;
    }

    public Integer getLongestRoadCard() {
        return longestRoadCard;
    }

    public void setLongestRoadCard(Integer longestRoadCard) {
        this.longestRoadCard = longestRoadCard;
    }

    public Integer getLargestArmyCard() {
        return largestArmyCard;
    }

    public void setLargestArmyCard(Integer largestArmyCard) {
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