package shared.model.player;

import com.google.gson.JsonObject;
import shared.exceptions.InvalidNameException;
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
public class Player implements Comparable<Player>{
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
     * Construct a Player object from a JSON blob
     *
     * @param json The JSON being used to construct this object
     */
    public Player(JsonObject json) {
        switch(json.get("color").getAsString()) {
            case "red":
                this.color = CatanColor.RED;
                break;
            case "blue":
                this.color = CatanColor.BLUE;
                break;
            case "green":
                this.color = CatanColor.GREEN;
                break;
            case "brown":
                this.color = CatanColor.BROWN;
                break;
            case "orange":
                this.color = CatanColor.ORANGE;
                break;
            case "puce":
                this.color = CatanColor.PUCE;
                break;
            case "purple":
                this.color = CatanColor.PURPLE;
                break;
            case "white":
                this.color = CatanColor.WHITE;
                break;
        }

        try {
            this.name = new Name(json.get("name").getAsString());
        } catch (InvalidNameException e) {
            e.printStackTrace();
        }

        this._id = json.get("id").getAsInt();
    }

    /**
     * New Player Constructor
     * @param points    Initial points (should be 2 for Catan)
     * @param color     Player Color
     * @param name      Player Name
     * @param index     Player Index
     * @throws InvalidPlayerException
     */
    public Player(int points, CatanColor color, int index, Name name) throws InvalidPlayerException {
        assert points > 0;
        assert name != null;
        this.victoryPoints = points;
        this.color = color;
        this.resourceCardBank = new ResourceCardBank(this);
        this.developmentCardBank = new DevelopmentCardBank(this);
        this.structureBank = new StructureBank();
        this.name = name;
        this.playerIndex = index;
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

    /**
     * Increments the player's points by 1
     */
    public void incrementPoints() {
        incrementPoints(1);
    }

    /**
     * Increments the player's points by value of increment
     *
     * @param increment Number of points to add to the player's score
     */
    public void incrementPoints(int increment) {
        this.victoryPoints += increment;
    }

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

    /*==========================================
                   Override Default Methods
     ============================================*/
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

    /**
     * Converts the object to JSON
     *
     * @return a JSON representation of the object
     */
    public JsonObject toJSON() {
        return null;
    }

    /*===========================================
                   Getters/Setters
     ============================================*/
    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
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

    public boolean isPlayedDevCard() {
        return playedDevCard;
    }

    public void setPlayedDevCard(boolean playedDevCard) {
        this.playedDevCard = playedDevCard;
    }

    public int getVictoryPoints() {
        return victoryPoints;
    }

    public void setVictoryPoints(int victoryPoints) {
        this.victoryPoints = victoryPoints;
    }

    public CatanColor getColor() {
        return color;
    }

    public void setColor(CatanColor color) {
        this.color = color;
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

    public StructureBank getStructureBank() {
        return structureBank;
    }

    public void setStructureBank(StructureBank structureBank) {
        this.structureBank = structureBank;
    }
}
