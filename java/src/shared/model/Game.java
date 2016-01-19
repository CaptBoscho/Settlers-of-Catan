package shared.model;

import java.util.List;

/**
 * @name Game
 * @description Main model class for Catan Game
 */
public class Game{
    private Dice dice;
    private GameMap map;
    private Robber robber;
    private Integer longestRoadCard;
    private Integer largestArmyCard;
    private List<Player> players;
    private ResourceCardBank resourceCardBank;
    private DevelopmentCardBank developmentCardBank;

    /**
     * @name Game
     * @description Game class constructor
     * @param players
     */
    public Game(List<Player> players){ //// TODO: 1/19/2016 When should players be created??? 
        this.dice = new Dice();
        this.map = new GameMap();
        this.robber = new Robber();
        this.longestRoadCard = -1;
        this.largestArmyCard = -1;
        this.players = players;
        this.resourceCardBank = new ResourceCardBank();
        this.developmentCardBank = new DevelopmentCardBank();
    }

    //// TODO: 1/19/2016 any other methods 

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

    public Array<Player> getPlayers() {
        return players;
    }

    public void setPlayers(Array<Player> players) {
        this.players = players;
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