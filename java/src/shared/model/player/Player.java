package shared.model.player;

import com.google.gson.JsonObject;
import shared.exceptions.InvalidPlayerException;
import shared.model.bank.DevelopmentCardBank;
import shared.model.bank.ResourceCardBank;
import shared.model.bank.StructureBank;
import shared.definitions.CatanColor;
import shared.model.devcards.DevelopmentCard;
import shared.model.resources.ResourceCard;

/**
 * Representation of a player in the game
 *
 * @author Kyle Cornelison
 */
public class Player implements IPlayer,Comparable<Player>{ // TODO: 1/30/2016 Add exceptions when danny is done
    private int _id;
    private Name name;
    private boolean discarded;
    private int monuments;
    private int playerIndex;
    private boolean playedDevCard;
    private int victoryPoints;
    private CatanColor color;
    private ResourceCardBank resourceCardBank;
    private DevelopmentCardBank developmentCardBank;
    private StructureBank structureBank;

    /**
     * Default Constructor
     */
    public Player() {
        this.victoryPoints = 0;
        this.color = null;
        this.resourceCardBank = new ResourceCardBank(this);
        this.developmentCardBank = new DevelopmentCardBank(this);
        this.structureBank = new StructureBank();
    }

    /**
     * New Player Constructor
     * @param points    Initial points
     * @param color     Player Color
     * @param name      Player Name
     */
    public Player(int points, CatanColor color, int id, Name name) {
        this.victoryPoints = points;
        this.color = color;
        this.resourceCardBank = new ResourceCardBank(this);
        this.developmentCardBank = new DevelopmentCardBank(this);
        this.structureBank = new StructureBank();
        this.name = name;
        this._id = id;
    }

    /**
     * Load Player Constructor
     * @param points    Player Point Counter
     * @param color     Player Color
     * @param rCrdBnk   Player Resources
     * @param devCrdBnk Player Development Cards
     * @param sBnk      Player Structures
     * @param name      Player Name
     * @param index     Player Index
     * @throws InvalidPlayerException
     */
    public Player(int points, CatanColor color, ResourceCardBank rCrdBnk,
                  DevelopmentCardBank devCrdBnk, StructureBank sBnk,
                  int index, Name name) throws InvalidPlayerException {
        this.victoryPoints = points;
        this.color = color;
        this.resourceCardBank = rCrdBnk;
        this.developmentCardBank = devCrdBnk;
        this.structureBank = sBnk;
        this.name = name;
        this.playerIndex = index;
    }

    //IPlayer Interface Methods
    //========================================================
    /**
     * Determine if Player can build a road
     * Checks resource cards
     *
     * @return True if Player can build a road
     */
    @Override
    public boolean canBuildRoad() {
        return resourceCardBank.canBuildRoad();
    }

    /**
     * Action - Player builds a road
     */
    @Override
    public void buildRoad() {
        resourceCardBank.buildRoad();
    }

    /**
     * Determine if Player can build a settlement
     * Checks resource cards
     *
     * @return True if Player can build a settlement
     */
    @Override
    public boolean canBuildSettlement() {
        return resourceCardBank.canBuildSettlement();
    }

    /**
     * Action - Player builds a settlement
     */
    @Override
    public void buildSettlement() {
        //Delegate action
        resourceCardBank.buildSettlement();
        //Increment points
        incrementPoints();
    }

    /**
     * Determine if Player can build a city
     * Checks resource cards
     *
     * @return True if Player can build a city
     */
    @Override
    public boolean canBuildCity() {
        return resourceCardBank.canBuildCity();
    }

    /**
     * Action - Player builds a city
     */
    @Override
    public void buildCity() {
        //Delegate action
        resourceCardBank.buildCity();
        //Increment points
        incrementPoints(); //Note: only have to increment by 1 since we are replacing a settlement
    }

    //Helper Methods
    //======================================================
    /**
     * Increments the player's points by 1
     */
    private void incrementPoints() {
        incrementPoints(1);
    }

    /**
     * Increments the player's points by value of increment
     *
     * @param increment Number of points to add to the player's score
     */
    private void incrementPoints(int increment) {
        this.victoryPoints += increment;
    }

    // TODO: 1/30/2016 figure out if card methods are needed???
    /**
     * Adds a dev card to developmentCardBank
     *
     * @param cardToAdd
     */
    public void addDevCard(DevelopmentCard cardToAdd) {
        developmentCardBank.addDevCard(cardToAdd);
    }

    /**
     * Adds a resource card to resourceCardBank
     *
     * @param cardToAdd
     */
    public void addResourceCard(ResourceCard cardToAdd) {
        resourceCardBank.addResource(cardToAdd);
    }

     //Override Default Methods
     //========================================================
    @Override
    public boolean equals(Object other){
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof Player))return false;

        Player otherPlayer = (Player)other;
        return otherPlayer._id == this._id;
    }

    @Override
    public int compareTo(Player otherPlayer) {
        if (this._id > otherPlayer._id) {
            return 1;
        } else if (this._id < otherPlayer._id) {
            return -1;
        } else {
            return 0;
        }
    }

    //Serialization/Deserialization
    //=================================================
    /**
     * Converts the object to JSON
     *
     * @return a JSON representation of the object
     */
    public JsonObject toJSON() {
        return null;
    }

    /**
     * Construct a Player object from a JSON blob
     *
     * @param json The JSON being used to construct this object
     */
    public Player(JsonObject json) {

    }

    //Getters/Setters
    //============================================

    public int get_id() {
        return _id;
    }

    public Name getName() {
        return name;
    }

    public boolean hasDiscarded() {
        return discarded;
    }

    public void setDiscarded(boolean discarded) {
        this.discarded = discarded;
    }

    public int getMonuments() {
        return monuments;
    }

    public void setMonuments(int monuments) {
        this.monuments = monuments;
    }

    public int getPlayerIndex() {
        return playerIndex;
    }

    public void setPlayerIndex(int playerIndex) {
        this.playerIndex = playerIndex;
    }

    public boolean hasPlayedDevCard() {
        return playedDevCard;
    }

    public void setPlayedDevCard(boolean playedDevCard) {
        this.playedDevCard = playedDevCard;
    }

    public int getVictoryPoints() {
        return victoryPoints;
    }

    public CatanColor getColor() {
        return color;
    }
}
